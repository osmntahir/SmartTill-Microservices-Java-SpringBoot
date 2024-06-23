package com.toyota.saleservice.service;

import com.toyota.saleservice.dao.CampaignRepository;
import com.toyota.saleservice.domain.Campaign;
import com.toyota.saleservice.domain.CampaignProduct;
import com.toyota.saleservice.dto.CampaignDto;
import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.exception.CampaignAlreadyExistsException;
import com.toyota.saleservice.exception.CampaignNotFoundException;
import com.toyota.saleservice.service.common.MapUtil;
import com.toyota.saleservice.service.impl.CampaignServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignServiceImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private MapUtil mapUtil;

    @InjectMocks
    private CampaignServiceImpl campaignService;

    private Campaign campaign;
    private CampaignDto campaignDto;

    @BeforeEach
    void setUp() {
        campaign = new Campaign();
        campaign.setId(1L);
        campaign.setName("Test Campaign");
        campaign.setDiscount(10);
        campaign.setDescription("Test description");
        campaign.setDeleted(false);

        campaignDto = new CampaignDto();
        campaignDto.setId(1L);
        campaignDto.setName("Test Campaign");
        campaignDto.setDiscount(10);
        campaignDto.setDescription("Test description");
        campaignDto.setDeleted(false);
    }
    @Test
    public void testGetAllCampaigns() {
        // Mock data
        List<Campaign> mockCampaigns = new ArrayList<>();
        mockCampaigns.add(campaign);
        Campaign campaign1 = new Campaign();
        campaign1.setId(1L);
        campaign1.setName("Test Campaign");
        campaign1.setDiscount(10);
        campaign1.setDescription("Test description");
        campaign1.setDeleted(false);
        mockCampaigns.add(campaign1);

        PageImpl<Campaign> mockPage = new PageImpl<>(mockCampaigns, PageRequest.of(0, 10), mockCampaigns.size());

        when(campaignRepository.getCampaignsFiltered(anyString(), anyDouble(), anyDouble(), anyBoolean(), any()))
                .thenReturn(mockPage);

        // Call service method
        PaginationResponse<CampaignDto> response = campaignService.getCampaignsFiltered(
                0, 10, "", 0.0, 100.0, false, Collections.emptyList(), "ASC");


        // Assertions
        assertEquals(mockCampaigns.size(), response.getContent().size());
        assertEquals(mockPage.getTotalPages(), response.getPageable().getTotalPages());
        assertEquals(mockPage.getTotalElements(), response.getPageable().getTotalElements());
    }
    @Test
    void testAddCampaign() {
        // Mock data
        when(campaignRepository.existsByName(anyString())).thenReturn(false);
        when(mapUtil.convertCampaignDtoToCampaign(any())).thenReturn(campaign);
        when(campaignRepository.save(any())).thenReturn(campaign);
        when(mapUtil.convertCampaignToCampaignDto(any())).thenReturn(campaignDto);

        // Call service method
        CampaignDto addedCampaign = campaignService.addCampaign(campaignDto);

        // Assertions
        assertNotNull(addedCampaign);
        assertEquals("Test Campaign", addedCampaign.getName());
    }

    @Test
    void testAddCampaignAlreadyExists() {
        // Mock data
        when(campaignRepository.existsByName(anyString())).thenReturn(true);

        // Call service method and assert exception
        assertThrows(CampaignAlreadyExistsException.class, () -> campaignService.addCampaign(campaignDto));
    }

    @Test
    void testUpdateCampaign() {
        // Mock data
        when(campaignRepository.existsByName(anyString())).thenReturn(false);
        when(campaignRepository.findById(anyLong())).thenReturn(Optional.of(campaign));
        when(mapUtil.convertCampaignDtoToCampaign(any())).thenReturn(campaign);
        when(campaignRepository.save(any())).thenReturn(campaign);
        when(mapUtil.convertCampaignToCampaignDto(any())).thenReturn(campaignDto);

        // Call service method
        CampaignDto updatedCampaign = campaignService.updateCampaign(1L, campaignDto);

        // Assertions
        assertNotNull(updatedCampaign);
        assertEquals("Test Campaign", updatedCampaign.getName());
    }

    @Test
    void testUpdateCampaignNotFound() {
        // Mock data
        when(campaignRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call service method and assert exception
        assertThrows(CampaignNotFoundException.class, () -> campaignService.updateCampaign(1L, campaignDto));
    }


    @Test
    void testUpdateCampaignAlreadyExists() {
        // Mock data
        when(campaignRepository.existsByName(anyString())).thenReturn(true);

        // Call service method and assert exception
        assertThrows(CampaignAlreadyExistsException.class, () -> campaignService.updateCampaign(1L, campaignDto));
    }

    @Test
    void testDeleteCampaign() {
        // Mock data
        Campaign campaign = new Campaign();
        campaign.setId(1L);
        campaign.setName("Test Campaign");
        campaign.setDescription("Test Description");
        campaign.setDiscount(10);
        campaign.setDeleted(false);

        CampaignDto campaignDto = new CampaignDto();
        campaignDto.setId(campaign.getId());
        campaignDto.setName(campaign.getName());
        campaignDto.setDescription(campaign.getDescription());
        campaignDto.setDiscount(campaign.getDiscount());
        campaignDto.setDeleted(true); // Simulate the deleted flag being set to true

        when(campaignRepository.findById(anyLong())).thenReturn(Optional.of(campaign));
        when(campaignRepository.save(any())).thenReturn(campaign);
        when(mapUtil.convertCampaignToCampaignDto(any())).thenReturn(campaignDto);

        // Call service method
        CampaignDto deletedCampaign = campaignService.deleteCampaign(1L);

        // Assertions
        assertNotNull(deletedCampaign);
        assertTrue(deletedCampaign.isDeleted());
        assertEquals(campaign.getId(), deletedCampaign.getId());
        assertEquals(campaign.getName(), deletedCampaign.getName());
        assertEquals(campaign.getDescription(), deletedCampaign.getDescription());
        assertEquals(campaign.getDiscount(), deletedCampaign.getDiscount());

        // Verify repository method calls
        verify(campaignRepository, times(1)).findById(1L);
        verify(campaignRepository, times(1)).save(campaign);
        verify(mapUtil, times(1)).convertCampaignToCampaignDto(campaign);

    }
    @Test
    void testDeleteCampaignNotFound() {
        // Mock data
        when(campaignRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call service method and assert exception
        assertThrows(CampaignNotFoundException.class, () -> campaignService.deleteCampaign(1L));
    }
    @Test
    void testUpdateCampaignWithInactiveStatus()
    {
        // Mock data
        CampaignDto campaignDto = new CampaignDto();
        campaignDto.setId(1L);
        campaignDto.setName("Test Campaign");
        campaignDto.setDescription("Test Description");
        campaignDto.setDiscount(10);
        campaignDto.setDeleted(true); // Simulate the deleted flag being set to true

        Campaign campaign = new Campaign();
        campaign.setId(1L);
        campaign.setName("Test Campaign");
        campaign.setDescription("Test Description");
        campaign.setDiscount(10);
        campaign.setDeleted(false);

        when(campaignRepository.findById(anyLong())).thenReturn(Optional.of(campaign));

        // Call service method and assert exception
        assertThrows(IllegalArgumentException.class, () -> campaignService.updateCampaign(1L, campaignDto));
    }
}
