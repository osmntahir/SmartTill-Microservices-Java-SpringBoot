package com.toyota.saleservice.dto;

import com.toyota.saleservice.domain.PaymentType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SaleDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNoArgsConstructor() {
        // When
        SaleDto saleDto = new SaleDto();

        // Then
        assertNotNull(saleDto);
        assertNull(saleDto.getId(), "Id should be null");
        assertNull(saleDto.getDate(), "Date should be null");
        assertNull(saleDto.getPaymentType(), "PaymentType should be null");
        assertEquals(0.0, saleDto.getTotalPrice(), "TotalPrice should be 0.0");
        assertNull(saleDto.getSoldProducts(), "SoldProducts should be null");
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        PaymentType paymentType = PaymentType.CREDIT_CARD;
        List<SoldProductDto> soldProducts = new ArrayList<>();

        // When
        SaleDto saleDto = new SaleDto(1L, now, paymentType, 100.0, soldProducts);

        // Then
        assertNotNull(saleDto);
        assertEquals(1L, saleDto.getId(), "Id getter/setter issue");
        assertEquals(now, saleDto.getDate(), "Date getter/setter issue");
        assertEquals(paymentType, saleDto.getPaymentType(), "PaymentType getter/setter issue");
        assertEquals(100.0, saleDto.getTotalPrice(), 0.001, "TotalPrice getter/setter issue");
        assertEquals(soldProducts, saleDto.getSoldProducts(), "SoldProducts getter/setter issue");
    }

    @Test
    void testValidation_NullDate() {
        // Given
        SaleDto saleDto = new SaleDto();
        saleDto.setPaymentType(PaymentType.CASH);
        saleDto.setTotalPrice(50.0);
        saleDto.setSoldProducts(new ArrayList<>());

        // When
        Set<ConstraintViolation<SaleDto>> violations = validator.validate(saleDto);

        // Then
        assertFalse(violations.isEmpty(), "Validation should fail for null date");
        assertEquals(1, violations.size(), "Exactly one violation expected for null date");
        assertEquals("Creation date must be not null", violations.iterator().next().getMessage(), "Incorrect error message");
    }

    @Test
    void testValidation_NullPaymentType() {
        // Given
        SaleDto saleDto = new SaleDto();
        saleDto.setDate(LocalDateTime.now());
        saleDto.setTotalPrice(50.0);
        saleDto.setSoldProducts(new ArrayList<>());

        // When
        Set<ConstraintViolation<SaleDto>> violations = validator.validate(saleDto);

        // Then
        assertFalse(violations.isEmpty(), "Validation should fail for null payment type");
        assertEquals(1, violations.size(), "Exactly one violation expected for null payment type");
        assertEquals("Total price must be not null", violations.iterator().next().getMessage(), "Incorrect error message");
    }
    @Test
    void testValidation_ValidSaleDto() {
        // Given
        SaleDto saleDto = new SaleDto();
        saleDto.setDate(LocalDateTime.now());
        saleDto.setPaymentType(PaymentType.CASH);
        saleDto.setTotalPrice(50.0);
        saleDto.setSoldProducts(new ArrayList<>());

        // When
        Set<ConstraintViolation<SaleDto>> violations = validator.validate(saleDto);

        // Then
        assertTrue(violations.isEmpty(), "Validation should pass for valid SaleDto");
    }
    @Test
    void testInvalidTotalPrice() {
        // Given
        SaleDto saleDto = new SaleDto();
        saleDto.setDate(LocalDateTime.now());
        saleDto.setPaymentType(PaymentType.CASH);
        saleDto.setTotalPrice(-50.0);
        saleDto.setSoldProducts(new ArrayList<>());

        // When
        Set<ConstraintViolation<SaleDto>> violations = validator.validate(saleDto);

        // Then
        assertFalse(violations.isEmpty(), "Validation should fail for negative total price");
        assertEquals(1, violations.size(), "Exactly one violation expected for negative total price");
        Assertions.assertEquals("Total price must be greater than 0.0", violations.iterator().next().getMessage());

    }
}
