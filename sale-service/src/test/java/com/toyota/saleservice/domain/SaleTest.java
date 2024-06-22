package com.toyota.saleservice.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SaleTest {

    @Test
    public void testNoArgsConstructor() {
        Sale sale = new Sale();
        assertThat(sale).isNotNull();
    }

    @Test
    public void testAllArgsConstructor() {
        List<SoldProduct> soldProducts = new ArrayList<>();
        Sale sale = new Sale(1L, 100.0, LocalDateTime.now(), PaymentType.CASH, false, soldProducts);

        assertThat(sale.getId()).isEqualTo(1L);
        assertThat(sale.getTotalPrice()).isEqualTo(100.0);
        assertThat(sale.getDate()).isNotNull();
        assertThat(sale.getPaymentType()).isEqualTo(PaymentType.CASH);
        assertThat(sale.isDeleted()).isFalse();
        assertThat(sale.getSoldProducts()).isEqualTo(soldProducts);
    }

    @Test
    public void testSettersAndGetters() {
        Sale sale = new Sale();
        List<SoldProduct> soldProducts = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        sale.setId(1L);
        sale.setTotalPrice(100.0);
        sale.setDate(now);
        sale.setPaymentType(PaymentType.CREDIT_CARD);
        sale.setDeleted(false);
        sale.setSoldProducts(soldProducts);

        assertThat(sale.getId()).isEqualTo(1L);
        assertThat(sale.getTotalPrice()).isEqualTo(100.0);
        assertThat(sale.getDate()).isEqualTo(now);
        assertThat(sale.getPaymentType()).isEqualTo(PaymentType.CREDIT_CARD);
        assertThat(sale.isDeleted()).isFalse();
        assertThat(sale.getSoldProducts()).isEqualTo(soldProducts);
    }

    @Test
    public void testOnCreate() {
        Sale sale = new Sale();
        sale.onCreate();

        assertThat(sale.getDate()).isNotNull();
        assertThat(sale.getDate()).isBeforeOrEqualTo(LocalDateTime.now());
    }
}
