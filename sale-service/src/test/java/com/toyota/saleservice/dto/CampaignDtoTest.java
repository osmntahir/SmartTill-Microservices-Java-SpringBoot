package com.toyota.saleservice.dto;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CampaignDtoTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    void testValidCampaignDto() {
        // Given
        CampaignDto dto = new CampaignDto(null, "Test Campaign", "Test Description", 25L, false);

        // When
        Set<ConstraintViolation<CampaignDto>> violations = validator.validate(dto);

        // Then
        assertTrue(violations.isEmpty(), "Validation should pass with valid CampaignDto");
    }

    @Test
    void testInvalidName() {
        // Given
        CampaignDto dto = new CampaignDto(null, "", "Test Description", 25L, false);

        // When
        Set<ConstraintViolation<CampaignDto>> violations = validator.validate(dto);

        // Then
        assertEquals(1, violations.size(), "There should be 1 validation error for invalid name");
        ConstraintViolation<CampaignDto> violation = violations.iterator().next();
        assertEquals("Name must be not blank", violation.getMessage(), "Incorrect validation message for name");
    }

    @Test
    void testInvalidDiscountNegative() {
        // Given
        CampaignDto dto = new CampaignDto(null, "Test Campaign", "Test Description", -10L, false);

        // When
        Set<ConstraintViolation<CampaignDto>> violations = validator.validate(dto);

        // Then
        assertEquals(1, violations.size(), "There should be 1 validation error for negative discount");
        ConstraintViolation<CampaignDto> violation = violations.iterator().next();
        assertEquals("Discount must be greater than or equal to 0", violation.getMessage(), "Incorrect validation message for discount");
    }

    @Test
    void testInvalidDiscountTooHigh() {
        // Given
        CampaignDto dto = new CampaignDto(null, "Test Campaign", "Test Description", 110L, false);

        // When
        Set<ConstraintViolation<CampaignDto>> violations = validator.validate(dto);

        // Then
        assertEquals(1, violations.size(), "There should be 1 validation error for discount too high");
        ConstraintViolation<CampaignDto> violation = violations.iterator().next();
        assertEquals("Discount must be less than or equal to 100", violation.getMessage(), "Incorrect validation message for discount");
    }

    @Test
    void testDefaultDeletedValue() {
        // Given
        CampaignDto dto = new CampaignDto(null, "Test Campaign", "Test Description", 25L, false);

        // When
        boolean deleted = dto.isDeleted();

        // Then
        assertFalse(deleted, "Default value for deleted should be false");
    }

    @Test
    void testNoArgsConstructor() {
        // When
        CampaignDto dto = new CampaignDto();

        // Then
        assertNotNull(dto, "NoArgsConstructor should initialize CampaignDto object");
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        Long id = 1L;
        String name = "Test Campaign";
        String description = "Test Description";
        long discount = 50L;
        boolean deleted = true;

        // When
        CampaignDto dto = new CampaignDto(id, name, description, discount, deleted);

        // Then
        assertEquals(id, dto.getId(), "Id should be set correctly");
        assertEquals(name, dto.getName(), "Name should be set correctly");
        assertEquals(description, dto.getDescription(), "Description should be set correctly");
        assertEquals(discount, dto.getDiscount(), "Discount should be set correctly");
        assertEquals(deleted, dto.isDeleted(), "Deleted should be set correctly");
    }
    @Test
    void TestDataAnnotaion()
    {
        CampaignDto dto = new CampaignDto(null, "Test Campaign", "Test Description", 25L, false);
        dto.setId(1L);
        dto.setName("Test Campaign");
        dto.setDescription("Test Description");
        dto.setDiscount(25L);
        dto.setDeleted(false);
        assertEquals(1L,dto.getId());
        assertEquals("Test Campaign",dto.getName());
        assertEquals("Test Description",dto.getDescription());
        assertEquals(25L,dto.getDiscount());
        assertEquals(false,dto.isDeleted());
    }
}
