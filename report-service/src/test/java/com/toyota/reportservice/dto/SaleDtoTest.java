package com.toyota.reportservice.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SaleDtoTest {

    private SaleDto saleDto;
    private Validator validator;

    @BeforeEach
    void setUp() {
        // Setting up the Validator to test validation annotations
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Instantiate SaleDto using the AllArgsConstructor
        saleDto = new SaleDto(
                1L,
                LocalDateTime.now(),
                PaymentType.CREDIT_CARD,
                150.0,
                Collections.emptyList() // assuming no products sold initially
        );
    }

    @Test
    void testAllArgsConstructor() {
        assertEquals(1L, saleDto.getId());
        assertNotNull(saleDto.getDate());
        assertEquals(PaymentType.CREDIT_CARD, saleDto.getPaymentType());
        assertEquals(150.0, saleDto.getTotalPrice());
        assertTrue(saleDto.getSoldProducts().isEmpty());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        SaleDto newSaleDto = new SaleDto();
        newSaleDto.setId(2L);
        newSaleDto.setDate(LocalDateTime.now());
        newSaleDto.setPaymentType(PaymentType.DEBIT_CARD);
        newSaleDto.setTotalPrice(200.0);
        newSaleDto.setSoldProducts(Collections.emptyList());

        assertEquals(2L, newSaleDto.getId());
        assertNotNull(newSaleDto.getDate());
        assertEquals(PaymentType.DEBIT_CARD, newSaleDto.getPaymentType());
        assertEquals(200.0, newSaleDto.getTotalPrice());
        assertTrue(newSaleDto.getSoldProducts().isEmpty());
    }

    @Test
    void testValidation_PositiveCase() {
        Set<ConstraintViolation<SaleDto>> violations = validator.validate(saleDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidation_NegativeCase() {
        SaleDto invalidSaleDto = new SaleDto(
                null, // Invalid: id is null
                null, // Invalid: date is null
                null, // Invalid: paymentType is null
                -1.0, // Invalid: totalPrice is negative
                Collections.emptyList()
        );

        Set<ConstraintViolation<SaleDto>> violations = validator.validate(invalidSaleDto);
        assertEquals(3, violations.size());
    }
}
