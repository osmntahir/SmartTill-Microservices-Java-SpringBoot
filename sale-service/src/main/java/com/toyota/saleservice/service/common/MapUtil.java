package com.toyota.saleservice.service.common;

import com.toyota.saleservice.config.ProductServiceClient;
import com.toyota.saleservice.domain.Campaign;
import com.toyota.saleservice.domain.CampaignProduct;
import com.toyota.saleservice.domain.Sale;
import com.toyota.saleservice.domain.SoldProduct;
import com.toyota.saleservice.dto.CampaignDto;
import com.toyota.saleservice.dto.CampaignProductDto;
import com.toyota.saleservice.dto.ProductDTO;
import com.toyota.saleservice.dto.SaleDto;
import com.toyota.saleservice.dto.SoldProductDto;
import com.toyota.saleservice.exception.ProductNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapUtil {

    private final ModelMapper modelMapper;
    private final ProductServiceClient productServiceClient;
    private static final Logger logger = LogManager.getLogger(MapUtil.class);

    public MapUtil(ModelMapper modelMapper, ProductServiceClient productServiceClient) {
        this.modelMapper = modelMapper;
        this.productServiceClient = productServiceClient;
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

        ProductDTO productDTO = productServiceClient.getProductById(soldProduct.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + soldProduct.getProductId()));

        dto.setProductName(productDTO.getName());
        dto.setPrice(productDTO.getPrice());
        dto.setInventory(productDTO.getInventory());
        return dto;
    }

    public SoldProduct convertSoldProductDtoToSoldProduct(SoldProductDto soldProductDto) {
        SoldProduct soldProduct = modelMapper.map(soldProductDto, SoldProduct.class);

        if (soldProductDto.getProductId() != null) {
            soldProduct.setProductId(soldProductDto.getProductId());
        } else {
            throw new IllegalArgumentException("ProductId cannot be null when converting SoldProductDto to SoldProduct");
        }

        return soldProduct;
    }

    public SaleDto convertSaleToSaleDto(Sale sale) {
        SaleDto saleDto = modelMapper.map(sale, SaleDto.class);


        List<SoldProductDto> soldProductDtos = sale.getSoldProducts().stream()
                .map(this::convertSoldProductToSoldProductDto)
                .collect(Collectors.toList());

        saleDto.setSoldProducts(soldProductDtos);

        return saleDto;
    }


    public Sale convertSaleDtoToSale(SaleDto saleDto) {
        return modelMapper.map(saleDto, Sale.class);
    }
}
