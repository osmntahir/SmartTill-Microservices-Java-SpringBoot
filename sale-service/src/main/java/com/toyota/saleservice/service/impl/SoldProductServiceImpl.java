package com.toyota.saleservice.service.impl;


import com.toyota.productservice.dao.ProductRepository;
import com.toyota.productservice.domain.Product;
import com.toyota.saleservice.dao.CampaignProductRepository;
import com.toyota.saleservice.dao.SoldProductRepository;
import com.toyota.saleservice.domain.SoldProduct;
import com.toyota.saleservice.dto.SoldProductDto;
import com.toyota.saleservice.exception.ProductNotFoundException;
import com.toyota.saleservice.service.abstracts.SoldProductService;
import com.toyota.saleservice.service.common.MapUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

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
            SoldProduct soldProduct = mapUtil.convertSoldProductDtoToSoldProduct(soldProductDto);
            Product product = productOptional.get();

            soldProduct.setProduct(product);
            soldProduct.setQuantity(soldProductDto.getQuantity());

            double originalPrice = product.getPrice();
            soldProduct.setPrice(originalPrice);

            double totalPrice = originalPrice * soldProduct.getQuantity();
            Optional<Long> discountOptional = campaignProductService.getDiscountForProduct(productId);

            double discountAmount = 0.0;
            if (discountOptional.isPresent() && discountOptional.get() > 0.0) {
                double discount = discountOptional.get();
                discountAmount = totalPrice * (discount / 100);
                totalPrice -= discountAmount;
                soldProduct.setDiscount((long) discount); // Discount percentage as long
            } else {
                soldProduct.setDiscount(0L);
            }
            soldProduct.setTotal(totalPrice);
            soldProduct.setName(product.getName());

            SoldProduct saved = soldProductRepository.save(soldProduct);
            logger.info("Sold product added with id: {}", saved.getId());
            return mapUtil.convertSoldProductToSoldProductDto(saved);
        } else {
            logger.error("Product not found with id: {}", productId);
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }
    }
}