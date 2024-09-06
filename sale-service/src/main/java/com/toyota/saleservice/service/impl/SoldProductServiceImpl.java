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
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.stream.Collectors;

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

        ProductDTO product = getProductById(productId);
        Sale sale = getSaleById(saleId);

        // Check if this product is already added to this sale
        Optional<SoldProduct> existingSoldProductOpt = soldProductRepository.findBySaleIdAndProductId(saleId, productId);

        if (existingSoldProductOpt.isPresent()) {
            // Product already exists in the sale, update quantity and total price
            SoldProduct existingSoldProduct = existingSoldProductOpt.get();
            updateExistingSoldProduct(existingSoldProduct, soldProductDto, product, sale);
        } else {
            // Create a new SoldProduct entry
            SoldProduct soldProduct = new SoldProduct();
            createNewSoldProduct(soldProductDto, product, sale, soldProduct);
        }


        double totalDiscountAmount = sale.getSoldProducts().stream()
                .mapToDouble(SoldProduct::getDiscountAmount)
                .sum();

        double totalDiscountedPrice = sale.getSoldProducts().stream()
                .mapToDouble(SoldProduct::getFinalPriceAfterDiscount)
                .sum();


        sale.setTotalDiscountAmount(totalDiscountAmount);
        sale.setTotalDiscountedPrice(totalDiscountedPrice);


        sale.setTotalPrice(sale.getSoldProducts().stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum());
        saleRepository.save(sale);

        // Convert the saved SoldProduct to DTO and return
        return soldProductRepository.findBySaleIdAndProductId(saleId, productId)
                .map(mapUtil::convertSoldProductToSoldProductDto)
                .orElseThrow(() -> new IllegalStateException("Sold product not found after save"));
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
        existingSoldProduct.setProductId(product.getId());
        existingSoldProduct.setName(product.getName());
        existingSoldProduct.setPrice(product.getPrice());

        // Update quantity
        existingSoldProduct.setQuantity(existingSoldProduct.getQuantity() + soldProductDto.getQuantity());

        // Recalculate total price
        double totalPrice = existingSoldProduct.getPrice() * existingSoldProduct.getQuantity();
        applyDiscountIfNeeded(existingSoldProduct, totalPrice, product.getId());

        // Update product stock quantity
        checkAndUpdateInventory(product, soldProductDto.getQuantity());

        // Save existing sold product
        soldProductRepository.save(existingSoldProduct);
    }

    void createNewSoldProduct(SoldProductDto soldProductDto,
                              ProductDTO product, Sale sale, SoldProduct soldProduct) {
        soldProduct.setProductId(product.getId());
        soldProduct.setName(product.getName());
        soldProduct.setPrice(product.getPrice());
        soldProduct.setQuantity(soldProductDto.getQuantity());

        // Check if there is enough stock
        checkAndUpdateInventory(product, soldProductDto.getQuantity());

        // Calculate total price
        double totalPrice = product.getPrice() * soldProduct.getQuantity();
        applyDiscountIfNeeded(soldProduct, totalPrice, product.getId());

        soldProduct.setSale(sale);

        // Save new sold product
        soldProductRepository.save(soldProduct);
    }

    private void applyDiscountIfNeeded(SoldProduct soldProduct, double totalPrice, Long productId) {
        Optional<Long> discountOptional = campaignProductService.getDiscountForProduct(productId);

        if (discountOptional.isPresent() && discountOptional.get() > 0) {
            double discount = discountOptional.get();
            double discountAmount = totalPrice * (discount / 100);
            double finalPrice = totalPrice - discountAmount;

            // Set the discount values
            soldProduct.setDiscount((long) discount);
            soldProduct.setDiscountAmount(discountAmount);
            soldProduct.setFinalPriceAfterDiscount(finalPrice);
            soldProduct.setTotal(finalPrice); // Final price after discount is applied
        } else {
            soldProduct.setDiscount(0L);
            soldProduct.setDiscountAmount(0);
            soldProduct.setFinalPriceAfterDiscount(totalPrice);
            soldProduct.setTotal(totalPrice);
        }
    }

    void checkAndUpdateInventory(ProductDTO product, int quantity) {
        if (product.getInventory() < quantity) {
            throw new ProductQuantityShortageException("Not enough stock available for product: " + product.getName());
        }
        product.setInventory(product.getInventory() - quantity);
        productServiceClient.updateProductInventory(product);
    }

    @Override
    public SoldProductDto updateSoldProduct(Long id, SoldProductDto soldProductDto) {
        logger.info("Updating sold product with id: {}", id);

        Optional<SoldProduct> optionalSoldProduct = soldProductRepository.findById(id);

        if (optionalSoldProduct.isPresent()) {
            SoldProduct existingSoldProduct = optionalSoldProduct.get();
            updateSoldProductDetails(existingSoldProduct, soldProductDto);
            SoldProduct updatedSoldProduct = soldProductRepository.save(existingSoldProduct);
            logger.info("Sold product with id {} is updated", id);
            return mapUtil.convertSoldProductToSoldProductDto(updatedSoldProduct);
        } else {
            logger.error("Sold Product not found with id: {}", id);
            throw new ProductNotFoundException("Sold Product not found with id: " + id);
        }
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
            soldProduct.setDeleted(true);
            SoldProduct saved = soldProductRepository.save(soldProduct);
            logger.info("Sold product deleted with id: {}", saved.getId());
            return mapUtil.convertSoldProductToSoldProductDto(saved);
        } else {
            logger.error("Sold product not found with id: {}", id);
            throw new ProductNotFoundException("Sold product not found with id: " + id);
        }
    }
}
