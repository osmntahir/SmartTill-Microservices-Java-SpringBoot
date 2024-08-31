package com.toyota.productservice.Mapper;

import com.toyota.productservice.domain.Product;
import com.toyota.productservice.dto.ProductDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for the ProductMapper utility class.
 */
public class ProductMapperTest {

    /**
     * Test mapping from Product to ProductDto.
     */
    @Test
    public void testMapToDto() {
        // Create a Product
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(10.0);
        product.setInventory(100);
        product.setActive(true);

        // Map Product to ProductDto
        ProductDto productDto = ProductMapper.mapToDto(product);

        // Assert mapping
        assertNotNull(productDto);
        assertEquals(product.getId(), productDto.getId());
        assertEquals(product.getName(), productDto.getName());
        assertEquals(product.getDescription(), productDto.getDescription());
        assertEquals(product.getPrice(), productDto.getPrice());
        assertEquals(product.getInventory(), productDto.getInventory());
        assertEquals(product.isActive(), productDto.isActive());
    }

    /**
     * Test mapping from ProductDto to Product.
     */
    @Test
    public void testMapToEntity() {
        // Create a ProductDto
        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");
        productDto.setPrice(10.0);
        productDto.setInventory(100);
        productDto.setActive(true);

        // Map ProductDto to Product
        Product product = ProductMapper.mapToEntity(productDto);

        // Assert mapping
        assertNotNull(product);
        assertEquals(productDto.getName(), product.getName());
        assertEquals(productDto.getDescription(), product.getDescription());
        assertEquals(productDto.getPrice(), product.getPrice());
        assertEquals(productDto.getInventory(), product.getInventory());
        assertEquals(productDto.isActive(), product.isActive());
    }
}
