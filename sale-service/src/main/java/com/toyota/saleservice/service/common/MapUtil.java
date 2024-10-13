package com.toyota.saleservice.service.common;

import com.toyota.saleservice.config.ProductServiceClient;
import com.toyota.saleservice.domain.Campaign;
import com.toyota.saleservice.domain.Sale;
import com.toyota.saleservice.domain.SoldProduct;
import com.toyota.saleservice.dto.CampaignDto;
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
        if (campaignDto == null) {
            return null;
        }

        Campaign campaign = modelMapper.map(campaignDto, Campaign.class);


        if (campaignDto.getProducts() != null) {
            List<Long> productIds = campaignDto.getProducts().stream()
                    .map(ProductDTO::getId)
                    .collect(Collectors.toList());
            campaign.setProductIds(productIds);
        }



        return campaign;
    }




    public CampaignDto convertCampaignToCampaignDto(Campaign campaign) {
        if (campaign == null) {
            return null;
        }

        CampaignDto campaignDto = modelMapper.map(campaign, CampaignDto.class);


        if (campaign.getProductIds() != null && !campaign.getProductIds().isEmpty()) {
            List<Long> productIds = campaign.getProductIds().stream()
                    .collect(Collectors.toList());


            List<ProductDTO> products = productServiceClient.getProductsByIds(productIds);
            campaignDto.setProducts(products);
        } else {
            campaignDto.setProducts(null);
        }

        return campaignDto;
    }


    // Mapping SoldProduct and Sale

    public SoldProductDto convertSoldProductToSoldProductDto(SoldProduct soldProduct) {
        SoldProductDto dto = modelMapper.map(soldProduct, SoldProductDto.class);

        ProductDTO productDTO = productServiceClient.getProductById(soldProduct.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + soldProduct.getProductId()));

        dto.setProduct(productDTO); // Setting the entire ProductDTO object

        return dto;
    }

    public SoldProduct convertSoldProductDtoToSoldProduct(SoldProductDto soldProductDto) {
        SoldProduct soldProduct = modelMapper.map(soldProductDto, SoldProduct.class);

        if (soldProductDto.getProduct() != null) {
            soldProduct.setProductId(soldProductDto.getProduct().getId()); // Extracting productId from ProductDTO
        } else {
            throw new IllegalArgumentException("ProductDTO cannot be null when converting SoldProductDto to SoldProduct");
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
