package com.toyota.saleservice.service.common;

import com.toyota.saleservice.domain.Campaign;
import com.toyota.saleservice.domain.Sale;
import com.toyota.saleservice.domain.SoldProduct;
import com.toyota.saleservice.dto.CampaignDto;
import com.toyota.saleservice.dto.SaleDto;
import com.toyota.saleservice.dto.SoldProductDto;
import org.modelmapper.ModelMapper;

public class MapUtil {
    private final ModelMapper modelMapper;

    public MapUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public SaleDto convertToDTO(Sale sale) {
        return modelMapper.map(sale, SaleDto.class);
    }

    public Sale convertToEntity(SaleDto saleDto) {
        return modelMapper.map(saleDto, Sale.class);
    }

    public CampaignDto convertToDTO(Campaign campaign) {
        return modelMapper.map(campaign, CampaignDto.class);
    }

    public Campaign convertToEntity(CampaignDto campaignDto) {
        return modelMapper.map(campaignDto, Campaign.class);
    }

    public SoldProduct convertToEntity(SoldProductDto soldProductdto) {
        return modelMapper.map(soldProductdto, SoldProduct.class);
    }

    public SoldProductDto convertToDTO(SoldProduct soldProduct) {
        return modelMapper.map(soldProduct, SoldProductDto.class);
    }





}
