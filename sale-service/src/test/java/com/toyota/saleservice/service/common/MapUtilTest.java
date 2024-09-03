package com.toyota.saleservice.service.common;

import com.toyota.saleservice.config.ProductServiceClient;
import com.toyota.saleservice.domain.Campaign;
import com.toyota.saleservice.domain.CampaignProduct;
import com.toyota.saleservice.domain.Sale;
import com.toyota.saleservice.domain.SoldProduct;
import com.toyota.saleservice.dto.*;
import com.toyota.saleservice.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MapUtilTest {

    private MapUtil mapUtil;
    private ProductServiceClient productServiceClient;

    @BeforeEach
    void setUp() {
        productServiceClient = Mockito.mock(ProductServiceClient.class);
        mapUtil = new MapUtil(new ModelMapper(), productServiceClient);
    }

    @Test
    void convertCampaignDtoToCampaign_shouldMapCorrectly() {
        CampaignDto campaignDto = new CampaignDto();
        campaignDto.setId(1L);
        campaignDto.setName("Summer Sale");

        Campaign campaign = mapUtil.convertCampaignDtoToCampaign(campaignDto);

        assertEquals(campaignDto.getId(), campaign.getId());
        assertEquals(campaignDto.getName(), campaign.getName());
    }

    @Test
    void convertCampaignToCampaignDto_shouldMapCorrectly() {
        Campaign campaign = new Campaign();
        campaign.setId(1L);
        campaign.setName("Summer Sale");

        CampaignDto campaignDto = mapUtil.convertCampaignToCampaignDto(campaign);

        assertEquals(campaign.getId(), campaignDto.getId());
        assertEquals(campaign.getName(), campaignDto.getName());
    }

    @Test
    void convertSoldProductToSoldProductDto_shouldMapCorrectly() {
        SoldProduct soldProduct = new SoldProduct();
        soldProduct.setProductId(1L);
        soldProduct.setPrice(100.0);
        soldProduct.setQuantity(2);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Product 1");
        productDTO.setPrice(100.0);
        productDTO.setInventory(10);

        when(productServiceClient.getProductById(1L)).thenReturn(Optional.of(productDTO));

        SoldProductDto soldProductDto = mapUtil.convertSoldProductToSoldProductDto(soldProduct);

        assertEquals(soldProduct.getProductId(), soldProductDto.getProductId());
        assertEquals(productDTO.getName(), soldProductDto.getProductName());
        assertEquals(productDTO.getPrice(), soldProductDto.getPrice());
    }

    @Test
    void convertSoldProductToSoldProductDto_shouldThrowExceptionWhenProductNotFound() {
        SoldProduct soldProduct = new SoldProduct();
        soldProduct.setProductId(1L);

        when(productServiceClient.getProductById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            mapUtil.convertSoldProductToSoldProductDto(soldProduct);
        });
    }



    @Test
    void convertSaleDtoToSale_shouldMapCorrectly() {
        SaleDto saleDto = new SaleDto();
        saleDto.setId(1L);

        Sale sale = mapUtil.convertSaleDtoToSale(saleDto);

        assertEquals(saleDto.getId(), sale.getId());
    }

    @Test
    void convertSoldProductDtoToSoldProduct_shouldMapCorrectly() {
        SoldProductDto soldProductDto = new SoldProductDto();
        soldProductDto.setProductId(1L);
        soldProductDto.setQuantity(2);

        SoldProduct soldProduct = mapUtil.convertSoldProductDtoToSoldProduct(soldProductDto);

        assertEquals(soldProductDto.getProductId(), soldProduct.getProductId());
        assertEquals(soldProductDto.getQuantity(), soldProduct.getQuantity());
    }

    @Test
    void convertSoldProductDtoToSoldProduct_shouldThrowExceptionWhenProductIdIsNull() {
        SoldProductDto soldProductDto = new SoldProductDto();

        assertThrows(IllegalArgumentException.class, () -> {
            mapUtil.convertSoldProductDtoToSoldProduct(soldProductDto);
        });
    }
}
