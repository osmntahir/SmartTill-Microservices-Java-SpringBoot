package com.toyota.saleservice.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SoldProductDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testSoldProductDto_AllArgsConstructor() {
        SoldProductDto dto = new SoldProductDto(1L, 2L, "Product Name", 100.0, 10, 10.0, 200.0, 5, false);

        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getProductId());
        assertEquals("Product Name", dto.getProductName());
        assertEquals(100.0, dto.getPrice());
        assertEquals(10, dto.getInventory());
        assertEquals(10.0, dto.getDiscount());
        assertEquals(200.0, dto.getTotal());
        assertEquals(5, dto.getQuantity());
        assertFalse(dto.isDeleted());
    }

    @Test
    public void testSoldProductDto_NoArgsConstructor() {
        SoldProductDto dto = new SoldProductDto();

        assertNull(dto.getId());
        assertNull(dto.getProductId());
        assertNull(dto.getProductName());
        assertEquals(0.0, dto.getPrice());
        assertEquals(0, dto.getInventory());
        assertEquals(0.0, dto.getDiscount());
        assertEquals(0.0, dto.getTotal());
        assertEquals(0, dto.getQuantity());
        assertFalse(dto.isDeleted());
    }

    @Test
    public void testSoldProductDto_Setters() {
        SoldProductDto dto = new SoldProductDto();
        dto.setId(1L);
        dto.setProductId(2L);
        dto.setProductName("Product Name");
        dto.setPrice(100.0);
        dto.setInventory(10);
        dto.setDiscount(10.0);
        dto.setTotal(200.0);
        dto.setQuantity(5);
        dto.setDeleted(true);

        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getProductId());
        assertEquals("Product Name", dto.getProductName());
        assertEquals(100.0, dto.getPrice());
        assertEquals(10, dto.getInventory());
        assertEquals(10.0, dto.getDiscount());
        assertEquals(200.0, dto.getTotal());
        assertEquals(5, dto.getQuantity());
        assertTrue(dto.isDeleted());
    }

    @Test
    public void testSoldProductDto_ValidQuantity() {
        SoldProductDto dto = new SoldProductDto();
        dto.setQuantity(1);

        Set<ConstraintViolation<SoldProductDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testSoldProductDto_InvalidQuantity() {
        SoldProductDto dto = new SoldProductDto();
        dto.setQuantity(0); // Invalid quantity

        Set<ConstraintViolation<SoldProductDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());

        ConstraintViolation<SoldProductDto> violation = violations.iterator().next();
        assertEquals("Quantity must be greater than or equal to 1", violation.getMessage());
    }
}
