package com.toyota.saleservice.service.common;

import com.toyota.saleservice.domain.Campaign;
import com.toyota.saleservice.domain.CampaignProduct;
import com.toyota.saleservice.dto.CampaignDto;
import org.modelmapper.ModelMapper;

public class MapUtil {
    private final ModelMapper modelMapper;

    public MapUtil(ModelMapper modelMapper) {

        this.modelMapper = modelMapper;
    }

   public Campaign convertCampaignDtoToCampaign(CampaignDto campaignDto) {
        return modelMapper.map(campaignDto, Campaign.class);
    }

    public CampaignDto convertCampaignToCampaignDto(Campaign campaign) {
        return modelMapper.map(campaign, CampaignDto.class);
    }
/*

    public CampaignProductDto convertCampaignProductToCampaignProductDto(CampaignProduct campaignProduct) {
        return modelMapper.map(campaignProduct, CampaignProductDto.class);
    }

    public CampaignProduct convertCampaignProductDtoToCampaignProduct(CampaignProductDto campaignProductDto) {
        return modelMapper.map(campaignProductDto, CampaignProduct.class);
    }

*/





}
