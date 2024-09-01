package com.toyota.saleservice.service.common;

import com.toyota.saleservice.domain.Campaign;
import com.toyota.saleservice.domain.CampaignProduct;
import com.toyota.saleservice.domain.Sale;
import com.toyota.saleservice.domain.SoldProduct;
import com.toyota.saleservice.dto.CampaignDto;
import com.toyota.saleservice.dto.CampaignProductDto;
import com.toyota.saleservice.dto.ProductDTO;
import com.toyota.saleservice.dto.SaleDto;
import com.toyota.saleservice.dto.SoldProductDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
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
        CampaignProductDto dto = modelMapper.map(campaignProduct, CampaignProductDto.class);
        dto.setProductId(campaignProduct.getProductId());
        dto.setCampaignId(campaignProduct.getCampaign().getId());
        return dto;
    }

    public CampaignProduct convertCampaignProductDtoToCampaignProduct(CampaignProductDto campaignProductDto) {
        CampaignProduct campaignProduct = modelMapper.map(campaignProductDto, CampaignProduct.class);
        campaignProduct.setProductId(campaignProductDto.getProductId());
        return campaignProduct;
    }

    public SoldProductDto convertSoldProductToSoldProductDto(SoldProduct soldProduct) {
        SoldProductDto dto = modelMapper.map(soldProduct, SoldProductDto.class);

        dto.setProductDto(convertProductIdToProductDto(soldProduct.getProductId()));
        return dto;
    }

    public SoldProduct convertSoldProductDtoToSoldProduct(SoldProductDto soldProductDto) {
        SoldProduct soldProduct = modelMapper.map(soldProductDto, SoldProduct.class);

        if (soldProductDto.getProductDto() != null) {
            soldProduct.setProductId(soldProductDto.getProductDto().getId());
        } else {
            throw new IllegalArgumentException("ProductDTO cannot be null when converting SoldProductDto to SoldProduct");
        }

        return soldProduct;
    }

    public SaleDto convertSaleToSaleDto(Sale sale) {
        return modelMapper.map(sale, SaleDto.class);
    }

    public Sale convertSaleDtoToSale(SaleDto saleDto) {
        return modelMapper.map(saleDto, Sale.class);
    }


    private ProductDTO convertProductIdToProductDto(Long productId) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productId);
        return productDTO;
    }
}
