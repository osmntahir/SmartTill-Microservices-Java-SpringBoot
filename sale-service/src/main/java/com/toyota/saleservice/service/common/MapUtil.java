package com.toyota.saleservice.service.common;

import com.toyota.productservice.domain.Product;
import com.toyota.productservice.dto.ProductDto;
import com.toyota.saleservice.domain.Campaign;
import com.toyota.saleservice.domain.CampaignProduct;
import com.toyota.saleservice.domain.Sale;
import com.toyota.saleservice.domain.SoldProduct;
import com.toyota.saleservice.dto.CampaignDto;
import com.toyota.saleservice.dto.CampaignProductDto;
import com.toyota.saleservice.dto.SaleDto;
import com.toyota.saleservice.dto.SoldProductDto;
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

    public CampaignProductDto convertCampaignProductToCampaignProductDto(CampaignProduct campaignProduct) {
        return modelMapper.map(campaignProduct, CampaignProductDto.class);
    }

    public CampaignProduct convertCampaignProductDtoToCampaignProduct(CampaignProductDto campaignProductDto) {
        return modelMapper.map(campaignProductDto, CampaignProduct.class);
    }


    public SoldProductDto convertSoldProductToSoldProductDto(SoldProduct soldProduct) {
        return modelMapper.map(soldProduct, SoldProductDto.class);
    }

    public SoldProduct convertSoldProductDtoToSoldProduct(SoldProductDto soldProductDto) {
        return modelMapper.map(soldProductDto, SoldProduct.class);
    }

    public SaleDto convertSaleToSaleDto(Sale sale) {
        return modelMapper.map(sale, SaleDto.class);
    }
    public Sale convertSaleDtoToSale(SaleDto saleDto) {
        return modelMapper.map(saleDto, Sale.class);
    }

    public ProductDto convertProductToProductDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
}
