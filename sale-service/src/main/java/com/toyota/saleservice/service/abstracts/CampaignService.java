package com.toyota.saleservice.service.abstracts;


import com.toyota.saleservice.dto.CampaignDto;
import com.toyota.saleservice.dto.PaginationResponse;

import java.util.List;

public interface CampaignService {
    PaginationResponse<CampaignDto> getCampaignsFiltered(
            int page, int size, String name, Double minDiscount,
            Double maxDiscount, boolean deleted,
            List<String> sortBy, String sortDirection);


    CampaignDto addCampaign(CampaignDto campaignDto);

    CampaignDto updateCampaign(Long id ,CampaignDto campaignDto);

    CampaignDto deleteCampaign(Long id);
}
