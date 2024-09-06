package com.toyota.saleservice.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;

public class SoldProductDtoTest {

    @Test
    public void testAllArgsConstructor() {
        SoldProductDto dto = new SoldProductDto(1L, 100L, "Product 1", 50.0, 10, 5.0, 2.5, 47.5, 50.0, 2, false);

        assertEquals(1L, dto.getId());
        assertEquals(100L, dto.getProductId());
        assertEquals("Product 1", dto.getProductName());
        assertEquals(50.0, dto.getPrice());
        assertEquals(10, dto.getInventory());
        assertEquals(5.0, dto.getDiscount());
        assertEquals(2.5, dto.getDiscountAmount());
        assertEquals(47.5, dto.getFinalPriceAfterDiscount());
        assertEquals(50.0, dto.getTotal());
        assertEquals(2, dto.getQuantity());
        assertFalse(dto.isDeleted());
    }

    @Test
    public void testNoArgsConstructor() {
        SoldProductDto dto = new SoldProductDto();

        assertNull(dto.getId());
        assertNull(dto.getProductId());
        assertNull(dto.getProductName());
        assertEquals(0.0, dto.getPrice());
        assertEquals(0, dto.getInventory());
        assertEquals(0.0, dto.getDiscount());
        assertEquals(0.0, dto.getDiscountAmount());
        assertEquals(0.0, dto.getFinalPriceAfterDiscount());
        assertEquals(0.0, dto.getTotal());
        assertEquals(0, dto.getQuantity());
        assertFalse(dto.isDeleted());
    }

    @Test
    public void testSettersAndGetters() {
        SoldProductDto dto = new SoldProductDto();
        dto.setId(1L);
        dto.setProductId(100L);
        dto.setProductName("Product 1");
        dto.setPrice(50.0);
        dto.setInventory(10);
        dto.setDiscount(5.0);
        dto.setDiscountAmount(2.5);
        dto.setFinalPriceAfterDiscount(47.5);
        dto.setTotal(50.0);
        dto.setQuantity(2);
        dto.setDeleted(true);

        assertEquals(1L, dto.getId());
        assertEquals(100L, dto.getProductId());
        assertEquals("Product 1", dto.getProductName());
        assertEquals(50.0, dto.getPrice());
        assertEquals(10, dto.getInventory());
        assertEquals(5.0, dto.getDiscount());
        assertEquals(2.5, dto.getDiscountAmount());
        assertEquals(47.5, dto.getFinalPriceAfterDiscount());
        assertEquals(50.0, dto.getTotal());
        assertEquals(2, dto.getQuantity());
        assertTrue(dto.isDeleted());
    }

    @Test
    public void testQuantityValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        SoldProductDto dto = new SoldProductDto();
        dto.setQuantity(0); // Invalid case, quantity must be >= 1

        Set<ConstraintViolation<SoldProductDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());

        for (ConstraintViolation<SoldProductDto> violation : violations) {
            assertEquals("Quantity must be greater than or equal to 1", violation.getMessage());
        }
    }

    @Test
    public void testValidQuantity() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        SoldProductDto dto = new SoldProductDto();
        dto.setQuantity(1); // Valid case

        Set<ConstraintViolation<SoldProductDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}
