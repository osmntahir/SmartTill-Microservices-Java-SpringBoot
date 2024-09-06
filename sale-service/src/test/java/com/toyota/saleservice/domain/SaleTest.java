package com.toyota.saleservice.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SaleTest {

    @Test
    public void testAllArgsConstructor() {
        Sale sale = new Sale(1L, 100.0, 10.0, 90.0, LocalDateTime.now(), PaymentType.CREDIT_CARD, false, "John Doe", new ArrayList<>());

        assertEquals(1L, sale.getId());
        assertEquals(100.0, sale.getTotalPrice());
        assertEquals(10.0, sale.getTotalDiscountAmount());
        assertEquals(90.0, sale.getTotalDiscountedPrice());
        assertNotNull(sale.getDate());
        assertEquals(PaymentType.CREDIT_CARD, sale.getPaymentType());
        assertFalse(sale.isDeleted());
        assertEquals("John Doe", sale.getCashierName());
        assertNotNull(sale.getSoldProducts());
    }

    @Test
    public void testNoArgsConstructor() {
        Sale sale = new Sale();

        assertNull(sale.getId());
        assertEquals(0.0, sale.getTotalPrice());
        assertEquals(0.0, sale.getTotalDiscountAmount());
        assertEquals(0.0, sale.getTotalDiscountedPrice());
        assertNull(sale.getDate());
        assertNull(sale.getPaymentType());
        assertFalse(sale.isDeleted());
        assertNull(sale.getCashierName());
        assertNull(sale.getSoldProducts());
    }

    @Test
    public void testSettersAndGetters() {
        Sale sale = new Sale();
        sale.setId(1L);
        sale.setTotalPrice(150.0);
        sale.setTotalDiscountAmount(15.0);
        sale.setTotalDiscountedPrice(135.0);
        sale.setDate(LocalDateTime.now());
        sale.setPaymentType(PaymentType.CASH);
        sale.setDeleted(true);
        sale.setCashierName("Jane Doe");
        sale.setSoldProducts(new ArrayList<>());

        assertEquals(1L, sale.getId());
        assertEquals(150.0, sale.getTotalPrice());
        assertEquals(15.0, sale.getTotalDiscountAmount());
        assertEquals(135.0, sale.getTotalDiscountedPrice());
        assertNotNull(sale.getDate());
        assertEquals(PaymentType.CASH, sale.getPaymentType());
        assertTrue(sale.isDeleted());
        assertEquals("Jane Doe", sale.getCashierName());
        assertNotNull(sale.getSoldProducts());
    }

    @Test
    public void testPrePersist() {
        Sale sale = new Sale();
        sale.setDate(null);
        sale.onCreate();

        assertNotNull(sale.getDate());
    }
}
