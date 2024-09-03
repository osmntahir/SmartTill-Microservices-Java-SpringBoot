package com.toyota.saleservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDTOTest {

    @Test
    public void testProductDTO_AllArgsConstructor() {
        ProductDTO dto = new ProductDTO();
        dto.setId(1L);
        dto.setName("Product Name");
        dto.setPrice(100.0);
        dto.setInventory(10);

        assertEquals(1L, dto.getId());
        assertEquals("Product Name", dto.getName());
        assertEquals(100.0, dto.getPrice());
        assertEquals(10, dto.getInventory());
    }

    @Test
    public void testProductDTO_NoArgsConstructor() {
        ProductDTO dto = new ProductDTO();

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertEquals(0.0, dto.getPrice());
        assertEquals(0, dto.getInventory());
    }

    @Test
    public void testProductDTO_Setters() {
        ProductDTO dto = new ProductDTO();
        dto.setId(1L);
        dto.setName("Product Name");
        dto.setPrice(100.0);
        dto.setInventory(10);

        assertEquals(1L, dto.getId());
        assertEquals("Product Name", dto.getName());
        assertEquals(100.0, dto.getPrice());
        assertEquals(10, dto.getInventory());
    }
}
