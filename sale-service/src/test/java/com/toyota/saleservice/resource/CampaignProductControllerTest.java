package com.toyota.saleservice.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyota.saleservice.domain.CampaignProduct;
import com.toyota.saleservice.dto.CampaignProductDto;
import com.toyota.saleservice.resource.CampaignProductController;
import com.toyota.saleservice.service.abstracts.CampaignProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CampaignProductControllerTest {

    @Mock
    private CampaignProductService campaignProductService;

    @InjectMocks
    private CampaignProductController campaignProductController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(campaignProductController).build();
    }

    @Test
    void testGetAllCampaignProducts() throws Exception {
        // Prepare mock data
        CampaignProductDto campaignProductDto = new CampaignProductDto();
        campaignProductDto.setId(1L);


        List<CampaignProductDto> mockCampaignProducts = Collections.singletonList(campaignProductDto);
        when(campaignProductService.getAllCampaignProducts()).thenReturn(mockCampaignProducts);

        // Perform GET request
        mockMvc.perform(get("/campaign-products/getAll"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));

    }

    @Test
    void testAddCampaignProduct() throws Exception {
        // Prepare mock data
        CampaignProductDto campaignProductDto = new CampaignProductDto();
        campaignProductDto.setId(1L);


        when(campaignProductService.addCampaignProduct(any(CampaignProductDto.class)))
                .thenReturn(campaignProductDto);

        // Perform POST request
        mockMvc.perform(post("/campaign-products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(campaignProductDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    void testUpdateCampaignProduct() throws Exception {
        // Prepare mock data
        CampaignProductDto campaignProductDto = new CampaignProductDto();
        campaignProductDto.setId(1L);


        when(campaignProductService.updateCampaignProduct(eq(1L), any(CampaignProductDto.class)))
                .thenReturn(campaignProductDto);

        // Perform PUT request
        mockMvc.perform(put("/campaign-products/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(campaignProductDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    void testDeleteCampaignProduct() throws Exception {
        // Prepare mock data
        CampaignProductDto campaignProductDto = new CampaignProductDto();
        campaignProductDto.setId(1L);


        when(campaignProductService.deleteCampaignProduct(eq(1L)))
                .thenReturn(campaignProductDto);

        // Perform DELETE request
        mockMvc.perform(delete("/campaign-products/delete/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    void testAddCampaignProduct_NullResponse() throws Exception {
        CampaignProductDto campaignProductDto = new CampaignProductDto();


        when(campaignProductService.addCampaignProduct(any(CampaignProductDto.class))).thenReturn(null);

        mockMvc.perform(post("/campaign-products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(campaignProductDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateCampaignProduct_NullResponse() throws Exception {
        CampaignProductDto campaignProductDto = new CampaignProductDto();
        campaignProductDto.setId(1L);


        when(campaignProductService.updateCampaignProduct(eq(1L), any(CampaignProductDto.class))).thenReturn(null);

        mockMvc.perform(put("/campaign-products/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(campaignProductDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteCampaignProduct_NullResponse() throws Exception {
        CampaignProductDto campaignProductDto = new CampaignProductDto();
        campaignProductDto.setId(1L);


        when(campaignProductService.deleteCampaignProduct(eq(1L))).thenReturn(null);

        mockMvc.perform(delete("/campaign-products/delete/{id}", 1))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllCampaignProducts_NoContent() {
        // Prepare mock data
        when(campaignProductService.getAllCampaignProducts()).thenReturn(Collections.emptyList());

        // Perform GET request
        try {
            mockMvc.perform(get("/campaign-products/getAll"))
                    .andExpect(status().isNoContent());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
