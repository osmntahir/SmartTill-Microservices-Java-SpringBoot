package com.toyota.saleservice.service.abstracts;

import com.toyota.saleservice.domain.CampaignProduct;
import com.toyota.saleservice.dto.CampaignProductDto;

import java.util.List;
import java.util.Optional;

public interface CampaignProductService {
    List<CampaignProductDto> getAllCampaignProducts();

    CampaignProductDto addCampaignProduct(CampaignProductDto campaignProductDto);

    CampaignProductDto updateCampaignProduct(Long id, CampaignProductDto campaignProductDto);

    CampaignProductDto deleteCampaignProduct(Long id);

    public Optional<Long> getDiscountForProduct(Long productId);

    public List<CampaignProductDto> getCampaignProductsByCampaignId(Long campaignId);

    public List<CampaignProductDto> getCampaignProductsByProductId(Long productId);
}
