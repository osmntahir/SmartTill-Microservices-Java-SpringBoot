package com.toyota.reportservice.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;

class DtoTests {

    @Test
    void soldProductDto_GettersSetters() {
        SoldProductDto soldProductDto = new SoldProductDto();
        soldProductDto.setId(1L);
        soldProductDto.setProductId(1L);
        soldProductDto.setProductName("Test Product");
        soldProductDto.setPrice(10.0);
        soldProductDto.setQuantity(5);
        soldProductDto.setTotal(50.0);
        soldProductDto.setInventory(100);
        soldProductDto.setDiscount(5.0);
        soldProductDto.setDeleted(false);

        assertEquals(1L, soldProductDto.getId());
        assertEquals(1L, soldProductDto.getProductId());
        assertEquals("Test Product", soldProductDto.getProductName());
        assertEquals(10.0, soldProductDto.getPrice());
        assertEquals(5, soldProductDto.getQuantity());
        assertEquals(50.0, soldProductDto.getTotal());
        assertEquals(100, soldProductDto.getInventory());
        assertEquals(5.0, soldProductDto.getDiscount());
        assertFalse(soldProductDto.isDeleted());
    }

    @Test
    void productDTO_GettersSetters() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Test Product");
        productDTO.setPrice(10.0);
        productDTO.setInventory(100);

        assertEquals(1L, productDTO.getId());
        assertEquals("Test Product", productDTO.getName());
        assertEquals(10.0, productDTO.getPrice());
        assertEquals(100, productDTO.getInventory());
    }

    @Test
    void saleDto_GettersSetters() {
        SoldProductDto soldProduct1 = new SoldProductDto(1L, 1L, "Product 1", 10.0, 100, 5.0, 50.0, 5, false);
        SoldProductDto soldProduct2 = new SoldProductDto(2L, 2L, "Product 2", 20.0, 200, 10.0, 100.0, 5, false);
        SaleDto saleDto = new SaleDto(1L, LocalDateTime.now(), PaymentType.CREDIT_CARD, 150.0, Arrays.asList(soldProduct1, soldProduct2));

        assertEquals(1L, saleDto.getId());
        assertEquals(150.0, saleDto.getTotalPrice());
        assertEquals(2, saleDto.getSoldProducts().size());
    }
}
