package com.toyota.saleservice.resource;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyota.saleservice.dto.CampaignProductDto;
import com.toyota.saleservice.service.abstracts.CampaignProductService;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {CampaignProductController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CampaignProductControllerTest {
    @Autowired
    private CampaignProductController campaignProductController;

    @MockBean
    private CampaignProductService campaignProductService;


    @Test
    void testGetAllCampaignProducts() throws Exception {
        // Arrange
        when(campaignProductService.getAllCampaignProducts()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/campaign-product/getAll");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(campaignProductController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    @Test
    void testGetAllCampaignProducts2() throws Exception {
        // Arrange
        ArrayList<CampaignProductDto> campaignProductDtoList = new ArrayList<>();
        campaignProductDtoList.add(new CampaignProductDto());
        when(campaignProductService.getAllCampaignProducts()).thenReturn(campaignProductDtoList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/campaign-product/getAll");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(campaignProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("[{\"id\":null,\"campaignId\":null,\"productId\":null,\"deleted\":false}]"));
    }

    @Test
    void testAddCampaignProduct() throws Exception {
        // Arrange
        when(campaignProductService.addCampaignProduct(Mockito.<CampaignProductDto>any()))
                .thenReturn(new CampaignProductDto());

        CampaignProductDto campaignProductDto = new CampaignProductDto();
        campaignProductDto.setCampaignId(1L);
        campaignProductDto.setDeleted(true);
        campaignProductDto.setId(1L);
        campaignProductDto.setProductId(1L);
        String content = (new ObjectMapper()).writeValueAsString(campaignProductDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/campaign-product/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(campaignProductController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":null,\"campaignId\":null,\"productId\":null,\"deleted\":false}"));
    }


    @Test
    void testUpdateCampaignProduct() throws Exception {
        // Arrange
        when(campaignProductService.updateCampaignProduct(Mockito.<Long>any(), Mockito.<CampaignProductDto>any()))
                .thenReturn(new CampaignProductDto());

        CampaignProductDto campaignProductDto = new CampaignProductDto();
        campaignProductDto.setCampaignId(1L);
        campaignProductDto.setDeleted(true);
        campaignProductDto.setId(1L);
        campaignProductDto.setProductId(1L);
        String content = (new ObjectMapper()).writeValueAsString(campaignProductDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/campaign-product/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(campaignProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":null,\"campaignId\":null,\"productId\":null,\"deleted\":false}"));
    }


    @Test
    void testUpdateCampaignProduct2() throws Exception {
        // Arrange
        when(campaignProductService.updateCampaignProduct(Mockito.<Long>any(), Mockito.<CampaignProductDto>any()))
                .thenReturn(null);

        CampaignProductDto campaignProductDto = new CampaignProductDto();
        campaignProductDto.setCampaignId(1L);
        campaignProductDto.setDeleted(true);
        campaignProductDto.setId(1L);
        campaignProductDto.setProductId(1L);
        String content = (new ObjectMapper()).writeValueAsString(campaignProductDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/campaign-product/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(campaignProductController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }


    @Test
    void testDeleteCampaignProduct() throws Exception {
        // Arrange
        when(campaignProductService.deleteCampaignProduct(Mockito.<Long>any())).thenReturn(new CampaignProductDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/campaign-product/delete/{id}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(campaignProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":null,\"campaignId\":null,\"productId\":null,\"deleted\":false}"));
    }


    @Test
    void testDeleteCampaignProduct2() throws Exception {
        // Arrange
        when(campaignProductService.deleteCampaignProduct(Mockito.<Long>any())).thenReturn(null);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/campaign-product/delete/{id}", 1L);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(campaignProductController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
    @Test
    void addCampaignProductWithFailure()
    {
        CampaignProductDto campaignProductDto = new CampaignProductDto();
        campaignProductDto.setCampaignId(1L);
        campaignProductDto.setDeleted(true);
        campaignProductDto.setId(1L);
        campaignProductDto.setProductId(1L);
        when(this.campaignProductService.addCampaignProduct((CampaignProductDto) Mockito.any())).thenReturn(null);
        ResponseEntity<CampaignProductDto> actualAddCampaignProductResult = this.campaignProductController.addCampaignProduct(campaignProductDto);
        HttpStatus actualAddCampaignProductResultHttpStatus = (HttpStatus) actualAddCampaignProductResult.getStatusCode();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualAddCampaignProductResultHttpStatus);
    }
}
