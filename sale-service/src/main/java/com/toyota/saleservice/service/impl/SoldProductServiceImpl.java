package com.toyota.saleservice.service.impl;


import com.toyota.productservice.dao.ProductRepository;
import com.toyota.productservice.domain.Product;

import com.toyota.saleservice.dao.SoldProductRepository;
import com.toyota.saleservice.domain.SoldProduct;
import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.dto.SoldProductDto;
import com.toyota.saleservice.exception.ProductNotFoundException;
import com.toyota.saleservice.service.abstracts.SoldProductService;
import com.toyota.saleservice.service.common.MapUtil;
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


    @Override
    public SoldProductDto addSoldProduct(Long productId, SoldProductDto soldProductDto) {
        logger.info("Adding sold product with productId: {}", productId);

        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isPresent()) {
            SoldProduct soldProduct = createSoldProduct(productId, soldProductDto);
            SoldProduct saved = soldProductRepository.save(soldProduct);
            logger.info("Sold product added with id: {}", saved.getId());
            return mapUtil.convertSoldProductToSoldProductDto(saved);
        } else {
            logger.error("Product not found with id: {}", productId);
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }
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
    // Yardımcı metodlar
    private SoldProduct createSoldProduct(Long productId, SoldProductDto soldProductDto) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            SoldProduct soldProduct = mapUtil.convertSoldProductDtoToSoldProduct(soldProductDto);
            soldProduct.setProduct(product);
            soldProduct.setQuantity(soldProductDto.getQuantity());
            soldProduct.setPrice(product.getPrice());
            double totalPrice = product.getPrice() * soldProduct.getQuantity();
            Optional<Long> discountOptional = campaignProductService.getDiscountForProduct(productId);
            double discountAmount = 0.0;
            if (discountOptional.isPresent() && discountOptional.get() > 0.0) {
                double discount = discountOptional.get();
                discountAmount = totalPrice * (discount / 100);
                totalPrice -= discountAmount;
                soldProduct.setDiscount((long) discount);
            } else {
                soldProduct.setDiscount(0L);
            }
            soldProduct.setTotal(totalPrice);
            soldProduct.setName(product.getName());
            return soldProduct;
        } else {
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }
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