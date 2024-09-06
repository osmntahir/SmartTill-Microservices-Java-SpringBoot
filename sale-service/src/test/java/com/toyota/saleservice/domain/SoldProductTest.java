package com.toyota.saleservice.domain;

import com.toyota.saleservice.dto.SoldProductDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SoldProductTest {

    private SoldProduct soldProduct;

    @BeforeEach
    public void setUp() {
        soldProduct = new SoldProduct();
    }

    @Test
    public void testAllArgsConstructor() {
        Sale sale = new Sale();
        SoldProduct soldProduct = new SoldProduct(1L, sale, 101L, "Product 1", 100.0, 2, 200.0, 10L, 10.0, 190.0, false);

        assertEquals(1L, soldProduct.getId());
        assertEquals(sale, soldProduct.getSale());
        assertEquals(101L, soldProduct.getProductId());
        assertEquals("Product 1", soldProduct.getName());
        assertEquals(100.0, soldProduct.getPrice());
        assertEquals(2, soldProduct.getQuantity());
        assertEquals(200.0, soldProduct.getTotal());
        assertEquals(10L, soldProduct.getDiscount());
        assertEquals(10.0, soldProduct.getDiscountAmount());
        assertEquals(190.0, soldProduct.getFinalPriceAfterDiscount());
        assertFalse(soldProduct.isDeleted());
    }

    @Test
    public void testNoArgsConstructor() {
        SoldProduct soldProduct = new SoldProduct();

        assertNull(soldProduct.getId());
        assertNull(soldProduct.getSale());
        assertNull(soldProduct.getProductId());
        assertNull(soldProduct.getName());
        assertEquals(0.0, soldProduct.getPrice());
        assertEquals(0, soldProduct.getQuantity());
        assertEquals(0.0, soldProduct.getTotal());
        assertEquals(0L, soldProduct.getDiscount());
        assertEquals(0.0, soldProduct.getDiscountAmount());
        assertEquals(0.0, soldProduct.getFinalPriceAfterDiscount());
        assertFalse(soldProduct.isDeleted());
    }

    @Test
    public void testSettersAndGetters() {
        Sale sale = new Sale();
        soldProduct.setId(1L);
        soldProduct.setSale(sale);
        soldProduct.setProductId(101L);
        soldProduct.setName("Product 1");
        soldProduct.setPrice(100.0);
        soldProduct.setQuantity(2);
        soldProduct.setTotal(200.0);
        soldProduct.setDiscount(10L);
        soldProduct.setDiscountAmount(10.0);
        soldProduct.setFinalPriceAfterDiscount(190.0);
        soldProduct.setDeleted(true);

        assertEquals(1L, soldProduct.getId());
        assertEquals(sale, soldProduct.getSale());
        assertEquals(101L, soldProduct.getProductId());
        assertEquals("Product 1", soldProduct.getName());
        assertEquals(100.0, soldProduct.getPrice());
        assertEquals(2, soldProduct.getQuantity());
        assertEquals(200.0, soldProduct.getTotal());
        assertEquals(10L, soldProduct.getDiscount());
        assertEquals(10.0, soldProduct.getDiscountAmount());
        assertEquals(190.0, soldProduct.getFinalPriceAfterDiscount());
        assertTrue(soldProduct.isDeleted());
    }
}

