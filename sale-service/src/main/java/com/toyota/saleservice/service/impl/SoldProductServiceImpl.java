package com.toyota.saleservice.service.impl;


import com.toyota.productservice.dao.ProductRepository;
import com.toyota.productservice.domain.Product;

import com.toyota.saleservice.dao.SaleRepository;
import com.toyota.saleservice.dao.SoldProductRepository;
import com.toyota.saleservice.domain.Sale;
import com.toyota.saleservice.domain.SoldProduct;
import com.toyota.saleservice.dto.PaginationResponse;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class SoldProductServiceImpl  implements SoldProductService {

    private final SoldProductRepository soldProductRepository;
    private final MapUtil mapUtil;
    private final Logger logger = LogManager.getLogger(SoldProductService.class);

    private final ProductRepository productRepository;
    private final CampaignProductServiceImpl campaignProductService;
    private final SaleRepository saleRepository;



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

    public SoldProductDto addSoldProduct(Long productId, Long saleId, @NotNull SoldProductDto soldProductDto) {
        logger.info("Adding sold product with productId: {}", productId);

        Product product = getProductById(productId);
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

        // Convert to DTO and return
        SoldProductDto resultDto = mapUtil.convertSoldProductToSoldProductDto(existingSoldProduct);
        resultDto.setProductDto(mapUtil.convertProductToProductDto(product));
        return resultDto;
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
    }

    private Sale getSaleById(Long saleId) {
        return saleRepository.findById(saleId)
                .orElseThrow(() -> new ProductNotFoundException("Sale not found with id: " + saleId));
    }

    private void updateExistingSoldProduct(SoldProduct existingSoldProduct, SoldProductDto soldProductDto,
                                           Product product, Sale sale) {
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

    private void createNewSoldProduct(Long productId, Long saleId, SoldProductDto soldProductDto,
                                      Product product, Sale sale) {
        // Check if there is enough stock
        checkAndUpdateInventory(product, soldProductDto.getQuantity());

        SoldProduct soldProduct = mapUtil.convertSoldProductDtoToSoldProduct(soldProductDto);
        soldProduct.setProduct(product);
        soldProduct.setQuantity(soldProductDto.getQuantity());
        soldProduct.setPrice(product.getPrice());

        // Calculate total price
        double totalPrice = product.getPrice() * soldProduct.getQuantity();
        applyDiscountIfNeeded(soldProduct, totalPrice, productId);

        soldProduct.setTotal(totalPrice);
        soldProduct.setSale(sale);
        soldProduct.setName(product.getName());

        // Update sale's total price
        sale.setTotalPrice(sale.getTotalPrice() + totalPrice);

        // Save new sold product
        soldProductRepository.save(soldProduct);
    }

    private void applyDiscountIfNeeded(SoldProduct soldProduct, double totalPrice, Long productId) {
        // Assume campaignProductService is properly implemented and provides discount
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

    private void checkAndUpdateInventory(Product product, int quantity) {
        if (product.getInventory() < quantity) {
            throw new ProductQuantityShortageException("Not enough stock available for product: " + product.getName());
        }
        product.setInventory(product.getInventory() - quantity);
        productRepository.save(product); // Update product inventory
    }



    private void updateSoldProductDetails(SoldProduct existingSoldProduct, SoldProductDto soldProductDto) {
        existingSoldProduct.setQuantity(soldProductDto.getQuantity());
        double originalPrice = existingSoldProduct.getProduct().getPrice();
        existingSoldProduct.setPrice(originalPrice);
        existingSoldProduct.setTotal(originalPrice * existingSoldProduct.getQuantity());
        Optional<Long> discountOptional = campaignProductService.getDiscountForProduct(existingSoldProduct.getProduct().getId());
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