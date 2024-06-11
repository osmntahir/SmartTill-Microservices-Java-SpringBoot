package com.toyota.saleservice.service.abstracts;

import com.toyota.saleservice.domain.CampaignProduct;
import com.toyota.saleservice.dto.CampaignProductDto;

import java.util.List;

public interface CampaignProductService {
    List<CampaignProductDto> getAllCampaignProducts();

    CampaignProductDto addCampaignProduct(CampaignProductDto campaignProductDto);

    CampaignProductDto updateCampaignProduct(Long id, CampaignProductDto campaignProductDto);

    CampaignProductDto deleteCampaignProduct(Long id);
}
