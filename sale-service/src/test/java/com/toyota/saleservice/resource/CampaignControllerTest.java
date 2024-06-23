package com.toyota.saleservice.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyota.saleservice.dto.CampaignDto;
import com.toyota.saleservice.dto.CustomPageable;
import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.service.abstracts.CampaignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CampaignControllerTest {

    @Mock
    private CampaignService campaignService;

    @InjectMocks
    private CampaignController campaignController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(campaignController).build();
    }


    @Test
    void testGetAllCampaigns() {
        //given
        List<CampaignDto> mockVehicles = List.of(
                new CampaignDto(),
                new CampaignDto(),
                new CampaignDto()
        );
        CustomPageable customPageable = new CustomPageable(0, 5, 3, 10);
        PaginationResponse<CampaignDto> paginationResponse = new PaginationResponse<>(mockVehicles, customPageable);

        //when
        Mockito.when(campaignService.getCampaignsFiltered(anyInt(), anyInt(), anyString(), anyDouble(),
                        anyDouble(), anyBoolean(), anyList(), anyString()))
                .thenReturn(paginationResponse);
        PaginationResponse<CampaignDto> result = campaignController.getAllCampaigns(0, 5
                , "", 0.0, 100.0, false, Collections.emptyList()
                , "ASC");

        //then
        assertEquals(0, result.getPageable().getPageNumber());
        assertEquals(5, result.getPageable().getPageSize());
        assertEquals(3, result.getPageable().getTotalPages());
        assertEquals(10, result.getPageable().getTotalElements());
    }


    @Test
    void testAddCampaign() throws Exception {
        CampaignDto campaignDto = new CampaignDto();
        campaignDto.setId(1L);
        campaignDto.setName("Test Campaign");

        when(campaignService.addCampaign(any(CampaignDto.class)))
                .thenReturn(campaignDto);

        mockMvc.perform(post("/campaigns/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(campaignDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateCampaign() throws Exception {
        CampaignDto campaignDto = new CampaignDto();
        campaignDto.setId(1L);
        campaignDto.setName("Updated Campaign");

        when(campaignService.updateCampaign(eq(1L), any(CampaignDto.class)))
                .thenReturn(campaignDto);

        mockMvc.perform(put("/campaigns/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(campaignDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteCampaign() throws Exception {
        CampaignDto campaignDto = new CampaignDto();
        campaignDto.setId(1L);

        when(campaignService.deleteCampaign(eq(1L)))
                .thenReturn(campaignDto);

        mockMvc.perform(delete("/campaigns/delete/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    void testAddCampaignWhenServiceReturnsNull() throws Exception {
        CampaignDto campaignDto = new CampaignDto();
        campaignDto.setId(1L);
        campaignDto.setName("Test Campaign");

        // Mock the service to return null
        when(campaignService.addCampaign(any(CampaignDto.class)))
                .thenReturn(null);

        mockMvc.perform(post("/campaigns/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(campaignDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

    @Test
    void testUpdateCampaignWhenServiceReturnsNull() throws Exception {
        CampaignDto campaignDto = new CampaignDto();
        campaignDto.setId(1L);
        campaignDto.setName("Updated Campaign");

        // Mock the service to return null
        when(campaignService.updateCampaign(eq(1L), any(CampaignDto.class)))
                .thenReturn(null);

        mockMvc.perform(put("/campaigns/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(campaignDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

    @Test
    void testDeleteCampaignWhenServiceReturnsNull() throws Exception {
        // Mock the service to return null
        when(campaignService.deleteCampaign(eq(1L)))
                .thenReturn(null);

        mockMvc.perform(delete("/campaigns/delete/{id}", 1))
                .andExpect(status().isNotFound());
    }
}
