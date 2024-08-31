package com.toyota.saleservice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SoldProductTest {

    @Test
    public void testNoArgsConstructor() {
        SoldProduct soldProduct = new SoldProduct();
        assertThat(soldProduct).isNotNull();
    }

    @Test
    public void testAllArgsConstructor() {
        Sale sale = new Sale();
        Long productId = 1L;
        SoldProduct soldProduct = new SoldProduct(1L, sale, productId, "Product Name", 100.0, 2, 200.0, 10L, false);

        assertThat(soldProduct.getId()).isEqualTo(1L);
        assertThat(soldProduct.getSale()).isEqualTo(sale);
        assertThat(soldProduct.getProductId()).isEqualTo(productId);
        assertThat(soldProduct.getName()).isEqualTo("Product Name");
        assertThat(soldProduct.getPrice()).isEqualTo(100.0);
        assertThat(soldProduct.getQuantity()).isEqualTo(2);
        assertThat(soldProduct.getTotal()).isEqualTo(200.0);
        assertThat(soldProduct.getDiscount()).isEqualTo(10L);
        assertThat(soldProduct.isDeleted()).isFalse();
    }

    @Test
    public void testSettersAndGetters() {
        SoldProduct soldProduct = new SoldProduct();
        Sale sale = new Sale();
        Long productId = 1L;

        soldProduct.setId(1L);
        soldProduct.setSale(sale);
        soldProduct.setProductId(productId);
        soldProduct.setName("Product Name");
        soldProduct.setPrice(100.0);
        soldProduct.setQuantity(2);
        soldProduct.setTotal(200.0);
        soldProduct.setDiscount(10L);
        soldProduct.setDeleted(false);

        assertThat(soldProduct.getId()).isEqualTo(1L);
        assertThat(soldProduct.getSale()).isEqualTo(sale);
        assertThat(soldProduct.getProductId()).isEqualTo(productId);
        assertThat(soldProduct.getName()).isEqualTo("Product Name");
        assertThat(soldProduct.getPrice()).isEqualTo(100.0);
        assertThat(soldProduct.getQuantity()).isEqualTo(2);
        assertThat(soldProduct.getTotal()).isEqualTo(200.0);
        assertThat(soldProduct.getDiscount()).isEqualTo(10L);
        assertThat(soldProduct.isDeleted()).isFalse();
    }
}
