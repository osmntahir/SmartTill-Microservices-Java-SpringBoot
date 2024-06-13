package com.toyota.saleservice.service.impl;

import com.toyota.saleservice.dao.CampaignProductRepository;
import com.toyota.saleservice.dao.CampaignRepository;
import com.toyota.productservice.dao.ProductRepository;
import com.toyota.saleservice.domain.CampaignProduct;
import com.toyota.saleservice.dto.CampaignProductDto;
import com.toyota.saleservice.exception.CampaignNotFoundException;
import com.toyota.saleservice.exception.CampaignProductNotFoundException;
import com.toyota.saleservice.exception.ProductNotFoundException;
import com.toyota.saleservice.service.abstracts.CampaignProductService;
import com.toyota.saleservice.service.common.MapUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignProductServiceImpl implements CampaignProductService {

    private final Logger logger = LogManager.getLogger(CampaignProductService.class);
    private final MapUtil mapUtil;
    private final CampaignProductRepository campaignProductRepository;
    private final CampaignRepository campaignRepository;
    private final ProductRepository productRepository;

    @Override
    public List<CampaignProductDto> getAllCampaignProducts() {
        logger.info("Getting all campaign products");

        List<CampaignProductDto> campaignProductDtos = campaignProductRepository.findAll().stream()
                .map(mapUtil::convertCampaignProductToCampaignProductDto)
                .toList();
        logger.info("Retrieved {} campaign products", campaignProductDtos.size());

        return campaignProductDtos;
    }

    @Override
    public CampaignProductDto addCampaignProduct(CampaignProductDto campaignProductDto) {
        logger.info("Adding campaign product with id: {}", campaignProductDto.getId());

        // Check if the campaign exists
        if (!campaignRepository.existsById(campaignProductDto.getCampaignId())) {
            logger.error("Campaign not found with id: {}", campaignProductDto.getCampaignId());
            throw new CampaignNotFoundException("Campaign not found with id: " + campaignProductDto.getCampaignId());
        }

        // Check if the product exists
        if (!productRepository.existsById(campaignProductDto.getProductId())) {
            logger.error("Product not found with id: {}", campaignProductDto.getProductId());
          throw new ProductNotFoundException("Product not found with id : " + campaignProductDto.getProductId());
        }

        CampaignProduct campaignProduct = mapUtil.convertCampaignProductDtoToCampaignProduct(campaignProductDto);
        CampaignProduct saved = campaignProductRepository.save(campaignProduct);
        logger.info("Campaign product with id: {} added", saved.getId());

        return mapUtil.convertCampaignProductToCampaignProductDto(saved);
    }

    @Override
    public CampaignProductDto updateCampaignProduct(Long id, CampaignProductDto campaignProductDto) {
        logger.info("Updating campaign product with id: {}", id);

        Optional<CampaignProduct> optionalCampaignProduct = campaignProductRepository.findById(id);
        if (optionalCampaignProduct.isPresent()) {
            CampaignProduct existingCampaignProduct = optionalCampaignProduct.get();
            if (!campaignProductDto.isDeleted()) {
                // Check if the campaign exists
                if (!campaignRepository.existsById(campaignProductDto.getCampaignId())) {
                    logger.error("Campaign not found with id: {}", campaignProductDto.getCampaignId());
                    throw new EntityNotFoundException("Campaign not found with id: " + campaignProductDto.getCampaignId());
                }

                // Check if the product exists
                if (!productRepository.existsById(campaignProductDto.getProductId())) {
                    logger.error("Product not found with id: {}", campaignProductDto.getProductId());
                    throw new EntityNotFoundException("Product not found with id: " + campaignProductDto.getProductId());
                }

                CampaignProduct updatedCampaignProduct = mapUtil.convertCampaignProductDtoToCampaignProduct(campaignProductDto);
                updatedCampaignProduct.setId(existingCampaignProduct.getId());
                updatedCampaignProduct.setDeleted(false);
                logger.info("Campaign with id {} is updated", id);
                return mapUtil.convertCampaignProductToCampaignProductDto(campaignProductRepository.save(updatedCampaignProduct));
            } else {
                logger.warn("Attempted to update Campaign Product with inactive status");
                throw new IllegalArgumentException("Cannot update Campaign Product with inactive status");
            }
        } else {
            logger.error("Campaign Product not found with id: {}", id);
            throw new EntityNotFoundException("Campaign Product not found with id: " + id);
        }
    }

    @Override
    public CampaignProductDto deleteCampaignProduct(Long id) {
        logger.info("Deleting campaign product with id: {}", id);
        CampaignProduct campaignProduct = campaignProductRepository.findById(id).orElse(null);
        if (campaignProduct == null) {
            logger.warn("Campaign product delete failed due to non-existent campaign product with id: {}", id);
            throw new CampaignProductNotFoundException("Campaign product with id " + id + " not found!");
        }
        campaignProduct.setDeleted(true);
        CampaignProduct saved = campaignProductRepository.save(campaignProduct);
        logger.info("Campaign product with id {} deleted successfully.", id);
        return mapUtil.convertCampaignProductToCampaignProductDto(saved);
    }

    @Override
    public Optional<Long> getDiscountForProduct(Long productId) {
        List<CampaignProduct> campaignProducts = campaignProductRepository.findByProductId(productId);

        if (campaignProducts.isEmpty()) {
            return Optional.of(0L);
        }

        // Örneğin, ilk kampanyanın indirimini alabilirsiniz:
        return Optional.of(campaignProducts.get(0).getCampaign().getDiscount());
    }



    @Override
    public List<CampaignProductDto> getCampaignProductsByProductId(Long productId) {
        List<CampaignProduct> campaignProducts = campaignProductRepository.findByProductIdAndDeletedFalse(productId);
        return campaignProducts.stream()
                .map(mapUtil::convertCampaignProductToCampaignProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampaignProductDto> getCampaignProductsByCampaignId(Long campaignId) {
        List<CampaignProduct> campaignProducts = campaignProductRepository.findByCampaignIdAndDeletedFalse(campaignId);
        return campaignProducts.stream()
                .map(mapUtil::convertCampaignProductToCampaignProductDto)
                .collect(Collectors.toList());
    }
}
