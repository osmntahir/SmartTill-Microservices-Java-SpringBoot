package com.toyota.saleservice.resource;


import com.toyota.saleservice.dto.CampaignDto;
import com.toyota.saleservice.dto.PaginationResponse;


import com.toyota.saleservice.service.abstracts.CampaignService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.Assert.*;

public class CampaignControllerTest {


    // Retrieve all campaigns with default pagination and sorting
    @Test
    public void test_get_all_campaigns_with_default_pagination_and_sorting() {
        // Arrange
        CampaignService campaignService = Mockito.mock(CampaignService.class);
        CampaignController campaignController = new CampaignController(campaignService);
        PaginationResponse<CampaignDto> expectedResponse = new PaginationResponse<>();
        Mockito.when(campaignService.getCampaignsFiltered(0, 10, "", 0.0, 100.0, false, Collections.emptyList(), "ASC"))
               .thenReturn(expectedResponse);

        // Act
        PaginationResponse<CampaignDto> actualResponse = campaignController.getAllCampaigns(0, 10, "", 0.0, 100.0, false, Collections.emptyList(), "ASC");

        // Assert
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    // Retrieve campaigns with non-existent page number
    @Test
    public void test_get_campaigns_with_non_existent_page_number() {
        // Arrange
        CampaignService campaignService = Mockito.mock(CampaignService.class);
        CampaignController campaignController = new CampaignController(campaignService);
        PaginationResponse<CampaignDto> expectedResponse = new PaginationResponse<>();
        Mockito.when(campaignService.getCampaignsFiltered(999, 10, "", 0.0, 100.0, false, Collections.emptyList(), "ASC"))
               .thenReturn(expectedResponse);

        // Act
        PaginationResponse<CampaignDto> actualResponse = campaignController.getAllCampaigns(999, 10, "", 0.0, 100.0, false, Collections.emptyList(), "ASC");

        // Assert
        Assertions.assertEquals(expectedResponse, actualResponse);
    }
    // Handles null input for the campaignDto and returns a 400 BAD REQUEST status
    @Test
    public void test_add_campaign_null_input() {
        CampaignService campaignService = Mockito.mock(CampaignService.class);
        CampaignController campaignController = new CampaignController(campaignService);

        Mockito.when(campaignService.addCampaign(null)).thenReturn(null);

        ResponseEntity<CampaignDto> response = campaignController.addCampaign(null);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }
    // Successfully adds a campaign and returns a 201 CREATED status with the campaign details
    @Test
    public void test_add_campaign_success() {
        CampaignService campaignService = Mockito.mock(CampaignService.class);
        CampaignController campaignController = new CampaignController(campaignService);

        CampaignDto campaignDto = new CampaignDto(1L, "Campaign Name", "Description", 10, null, false);
        Mockito.when(campaignService.addCampaign(Mockito.any(CampaignDto.class))).thenReturn(campaignDto);

        ResponseEntity<CampaignDto> response = campaignController.addCampaign(campaignDto);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Campaign Name", response.getBody().getName());
    }
    // Returns HTTP 400 status when CampaignDto is invalid
    @Test
    public void test_update_campaign_invalid_dto() {
        CampaignService campaignService = Mockito.mock(CampaignService.class);
        CampaignController campaignController = new CampaignController(campaignService);
        Long validId = 1L;
        CampaignDto invalidCampaignDto = new CampaignDto(validId, "", "Description", 10, null, false);
        Mockito.when(campaignService.updateCampaign(validId, invalidCampaignDto)).thenReturn(null);

        ResponseEntity<CampaignDto> response = campaignController.updateCampaign(validId, invalidCampaignDto);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }    // Successfully updates a campaign when valid ID and CampaignDto are provided
    @Test
    public void test_update_campaign_success() {
        CampaignService campaignService = Mockito.mock(CampaignService.class);
        CampaignController campaignController = new CampaignController(campaignService);
        Long validId = 1L;
        CampaignDto validCampaignDto = new CampaignDto(validId, "Campaign Name", "Description", 10, null, false);
        Mockito.when(campaignService.updateCampaign(validId, validCampaignDto)).thenReturn(validCampaignDto);

        ResponseEntity<CampaignDto> response = campaignController.updateCampaign(validId, validCampaignDto);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(validCampaignDto, response.getBody());
    }
    // Attempt to delete a campaign with a non-existent ID
    @Test
    public void test_delete_campaign_non_existent_id() {
        // Arrange
        Long nonExistentId = 999L;

        CampaignService campaignService = Mockito.mock(CampaignService.class);
        Mockito.when(campaignService.deleteCampaign(nonExistentId)).thenReturn(null);

        CampaignController campaignController = new CampaignController(campaignService);

        // Act
        ResponseEntity<CampaignDto> response = campaignController.deleteCampaign(nonExistentId);

        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }
    // Successfully delete a campaign when a valid ID is provided
    @Test
    public void test_delete_campaign_success() {
        // Arrange
        Long validId = 1L;
        CampaignDto campaignDto = new CampaignDto();
        campaignDto.setId(validId);
        campaignDto.setName("Test Campaign");
        campaignDto.setDiscount(10);
        campaignDto.setDeleted(true);

        CampaignService campaignService = Mockito.mock(CampaignService.class);
        Mockito.when(campaignService.deleteCampaign(validId)).thenReturn(campaignDto);

        CampaignController campaignController = new CampaignController(campaignService);

        // Act
        ResponseEntity<CampaignDto> response = campaignController.deleteCampaign(validId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(validId, response.getBody().getId());
    }
}