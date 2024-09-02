package com.toyota.saleservice.service.impl;

import com.toyota.saleservice.config.ProductServiceClient;
import com.toyota.saleservice.dao.SaleRepository;
import com.toyota.saleservice.dao.SoldProductRepository;
import com.toyota.saleservice.domain.Sale;
import com.toyota.saleservice.domain.SoldProduct;
import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.dto.ProductDTO;
import com.toyota.saleservice.dto.SoldProductDto;
import com.toyota.saleservice.exception.ProductNotFoundException;
import com.toyota.saleservice.exception.ProductQuantityShortageException;
import com.toyota.saleservice.exception.SaleNotFoundException;
import com.toyota.saleservice.service.abstracts.SoldProductService;
import com.toyota.saleservice.service.common.MapUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SoldProductServiceImpl implements SoldProductService {

    private final SoldProductRepository soldProductRepository;
    private final MapUtil mapUtil;
    private final Logger logger = LogManager.getLogger(SoldProductService.class);
    private final ProductServiceClient productServiceClient;
    private final CampaignProductServiceImpl campaignProductService;
    private final SaleRepository saleRepository;

    @Override
    public SoldProductDto updateSoldProduct(Long id, SoldProductDto soldProductDto) {
        logger.info("Updating sold product with id: {}", id);

        Optional<SoldProduct> optionalSoldProduct = soldProductRepository.findById(id);

        if (optionalSoldProduct.isPresent()) {
            SoldProduct existingSoldProduct = optionalSoldProduct.get();
            Sale sale = existingSoldProduct.getSale();

            // Calculate the difference in total price before and after the update
            double oldTotalPrice = existingSoldProduct.getTotal();

            updateSoldProductDetails(existingSoldProduct, soldProductDto);

            SoldProduct updatedSoldProduct = soldProductRepository.save(existingSoldProduct);

            // Calculate the new total price for the sale
            double newTotalPrice = sale.getTotalPrice() - oldTotalPrice + updatedSoldProduct.getTotal();
            sale.setTotalPrice(newTotalPrice);

            // Save the updated sale with the new total price
            saleRepository.save(sale);

            logger.info("Sold product with id {} is updated", id);
            return mapUtil.convertSoldProductToSoldProductDto(updatedSoldProduct);
        } else {
            logger.error("Sold Product not found with id: {}", id);
            throw new ProductNotFoundException("Sold Product not found with id: " + id);
        }
    }

    @Override
    public PaginationResponse<SoldProductDto> getSoldProducts(int page, int size, String name, Double minPrice, Double maxPrice, boolean deleted, String sortBy, String sortDirection) {
        logger.info("Getting sold products with filters");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
        Page<SoldProduct> pageResponse = soldProductRepository.getSoldProductsFiltered(name, minPrice, maxPrice, deleted, pageable);
        logger.debug("Retrieved {} sold products.", pageResponse.getContent().size());
        List<SoldProductDto> soldProductDtos = pageResponse.stream().map(
                mapUtil::convertSoldProductToSoldProductDto).toList();
        logger.info("Retrieved and converted {} sold products to dto.", soldProductDtos.size());

        return new PaginationResponse<>(soldProductDtos, pageResponse);
    }

    @Override
    public SoldProductDto addSoldProduct(Long productId, Long saleId, @NotNull SoldProductDto soldProductDto) {
        logger.info("Adding sold product with productId: {}", productId);

        // Fetch the product using the productId from the URL
        ProductDTO product = getProductById(productId);
        soldProductDto.setProductId(productId); // Set the product ID in SoldProductDto
        soldProductDto.setProductName(product.getName()); // Set product name
        soldProductDto.setPrice(product.getPrice()); // Set product price

        Sale sale = getSaleById(saleId);

        // Check if this product is already added to this sale
        SoldProduct existingSoldProduct = soldProductRepository.findBySaleIdAndProductId(saleId, productId)
                .orElse(null);

        if (existingSoldProduct != null) {
            // Product already exists in the sale, update quantity and total price
            updateExistingSoldProduct(existingSoldProduct, soldProductDto, product, sale);
        } else {
            // Product doesn't exist in the sale, create a new entry
            createNewSoldProduct(productId, saleId, soldProductDto, product, sale);
        }

        // Fetch existing sold product again if updated or created
        existingSoldProduct = soldProductRepository.findBySaleIdAndProductId(saleId, productId)
                .orElseThrow(() -> new IllegalStateException("Failed to fetch saved sold product"));

        return mapUtil.convertSoldProductToSoldProductDto(existingSoldProduct);
    }

    private void createNewSoldProduct(Long productId, Long saleId, SoldProductDto soldProductDto,
                                      ProductDTO product, Sale sale) {
        // Check if there is enough stock
        checkAndUpdateInventory(product, soldProductDto.getQuantity());

        SoldProduct soldProduct = mapUtil.convertSoldProductDtoToSoldProduct(soldProductDto);
        soldProduct.setProductId(productId); // productId set through URL
        soldProduct.setQuantity(soldProductDto.getQuantity());
        soldProduct.setPrice(product.getPrice());

        // Calculate total price
        double totalPrice = product.getPrice() * soldProduct.getQuantity();
        applyDiscountIfNeeded(soldProduct, totalPrice, productId);

        soldProduct.setSale(sale);
        soldProduct.setName(product.getName());

        // Update sale's total price
        sale.setTotalPrice(sale.getTotalPrice() + totalPrice);

        // Save new sold product
        soldProductRepository.save(soldProduct);
    }

    private ProductDTO getProductById(Long productId) {
        return productServiceClient.getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
    }

    private Sale getSaleById(Long saleId) {
        return saleRepository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found with id: " + saleId));
    }

    private void updateExistingSoldProduct(SoldProduct existingSoldProduct, SoldProductDto soldProductDto,
                                           ProductDTO product, Sale sale) {
        // Update quantity
        existingSoldProduct.setQuantity(existingSoldProduct.getQuantity() + soldProductDto.getQuantity());

        // Recalculate total price
        double totalPrice = existingSoldProduct.getPrice() * existingSoldProduct.getQuantity();
        applyDiscountIfNeeded(existingSoldProduct, totalPrice, product.getId());

        existingSoldProduct.setTotal(totalPrice);

        // Update sale's total price
        sale.setTotalPrice(sale.getTotalPrice() + totalPrice);

        // Update product stock quantity
        checkAndUpdateInventory(product, soldProductDto.getQuantity());

        // Save existing sold product
        soldProductRepository.save(existingSoldProduct);
    }

    private void applyDiscountIfNeeded(SoldProduct soldProduct, double totalPrice, Long productId) {
        // Check if there is a discount available for the product
        Optional<Long> discountOptional = campaignProductService.getDiscountForProduct(productId);
        if (discountOptional.isPresent() && discountOptional.get() > 0) {
            double discount = discountOptional.get();
            double discountAmount = totalPrice * (discount / 100);
            totalPrice -= discountAmount;
            soldProduct.setDiscount((long) discount);
        } else {
            soldProduct.setDiscount(0L);
        }
        soldProduct.setTotal(totalPrice);
    }

    private void checkAndUpdateInventory(ProductDTO product, int quantity) {
        if (product.getInventory() < quantity) {
            throw new ProductQuantityShortageException("Not enough stock available for product: " + product.getName());
        }
        product.setInventory(product.getInventory() - quantity);
        productServiceClient.updateProductInventory(product);
    }

    private void updateSoldProductDetails(SoldProduct existingSoldProduct, SoldProductDto soldProductDto) {
        existingSoldProduct.setQuantity(soldProductDto.getQuantity());
        double originalPrice = existingSoldProduct.getPrice();
        existingSoldProduct.setPrice(originalPrice);
        existingSoldProduct.setTotal(originalPrice * existingSoldProduct.getQuantity());
        Optional<Long> discountOptional = campaignProductService.getDiscountForProduct(existingSoldProduct.getProductId());
        if (discountOptional.isPresent() && discountOptional.get() > 0.0) {
            double discount = discountOptional.get();
            double discountAmount = existingSoldProduct.getTotal() * (discount / 100);
            existingSoldProduct.setDiscount((long) discount);
            existingSoldProduct.setTotal(existingSoldProduct.getTotal() - discountAmount);
        } else {
            existingSoldProduct.setDiscount(0L);
        }
    }

    @Override
    public SoldProductDto deleteSoldProduct(Long id) {
        logger.info("Deleting sold product with id: {}", id);

        Optional<SoldProduct> optionalSoldProduct = soldProductRepository.findById(id);

        if (optionalSoldProduct.isPresent()) {
            SoldProduct soldProduct = optionalSoldProduct.get();
            Sale sale = soldProduct.getSale();
            double totalPrice = sale.getTotalPrice() - soldProduct.getTotal();
            sale.setTotalPrice(totalPrice);
            saleRepository.save(sale);
            soldProductRepository.delete(soldProduct);
            logger.info("Sold product with id {} deleted successfully.", id);
            return mapUtil.convertSoldProductToSoldProductDto(soldProduct);
        } else {
            logger.warn("Sold product delete failed due to non-existent sold product with id: {}", id);
            throw new ProductNotFoundException("Sold product with id " + id + " not found!");
        }
    }
}