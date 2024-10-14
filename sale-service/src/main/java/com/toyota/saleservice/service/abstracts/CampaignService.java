package com.toyota.saleservice.service.abstracts;


import com.toyota.saleservice.dto.CampaignDto;
import com.toyota.saleservice.dto.PaginationResponse;

import java.util.List;
import java.util.Optional;

public interface CampaignService {
    PaginationResponse<CampaignDto> getCampaignsFiltered(
            int page, int size, String name, Double minDiscount,
            Double maxDiscount, boolean deleted,
            List<String> sortBy, String sortDirection);


    CampaignDto addCampaign(CampaignDto campaignDto);

    CampaignDto updateCampaign(Long id ,CampaignDto campaignDto);

    CampaignDto deleteCampaign(Long id);

    Optional<Long> getDiscountForProduct(Long productId);

    CampaignDto addProductsToCampaign(Long campaignId, List<Long> productIds);

    CampaignDto removeProductsFromCampaign(Long campaignId, List<Long> productIds);

    CampaignDto removeAllProductsFromCampaign(Long campaignId);

    String getCampaignNameForProduct(Long productId);
}
