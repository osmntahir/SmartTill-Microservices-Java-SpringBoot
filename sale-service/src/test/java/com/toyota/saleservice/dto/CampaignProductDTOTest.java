package com.toyota.saleservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CampaignProductDTOTest {

    @Test
    void testDataAnnotation() {
        // Given
        Long id = 1L;
        Long campaignId = 10L;
        Long productId = 100L;
        boolean deleted = false;

        // When
        CampaignProductDto dto = new CampaignProductDto();
        dto.setId(id);
        dto.setCampaignId(campaignId);
        dto.setProductId(productId);
        dto.setDeleted(deleted);

        // Then
        assertEquals(id, dto.getId(), "Id should be set correctly");
        assertEquals(campaignId, dto.getCampaignId(), "CampaignId should be set correctly");
        assertEquals(productId, dto.getProductId(), "ProductId should be set correctly");
        assertEquals(deleted, dto.isDeleted(), "Deleted should be set correctly");
    }

    @Test
    void testNoArgsConstructor() {
        // When
        CampaignProductDto dto = new CampaignProductDto();

        // Then
        assertNotNull(dto, "NoArgsConstructor should initialize CampaignProductDto object");
        assertNull(dto.getId(), "Id should be null");
        assertNull(dto.getCampaignId(), "CampaignId should be null");
        assertNull(dto.getProductId(), "ProductId should be null");
        assertFalse(dto.isDeleted(), "Deleted should be false");
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        Long id = 1L;
        Long campaignId = 10L;
        Long productId = 100L;
        boolean deleted = true;

        // When
        CampaignProductDto dto = new CampaignProductDto(id, campaignId, productId, deleted);

        // Then
        assertEquals(id, dto.getId(), "Id should be set correctly");
        assertEquals(campaignId, dto.getCampaignId(), "CampaignId should be set correctly");
        assertEquals(productId, dto.getProductId(), "ProductId should be set correctly");
        assertEquals(deleted, dto.isDeleted(), "Deleted should be set correctly");
    }

    @Test
    void testDefaultDeletedValue() {
        // When
        CampaignProductDto dto = new CampaignProductDto();

        // Then
        assertFalse(dto.isDeleted(), "Default value for deleted should be false");
    }
}
