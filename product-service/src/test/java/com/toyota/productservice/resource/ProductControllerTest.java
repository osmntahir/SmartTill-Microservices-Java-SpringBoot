package com.toyota.productservice.resource;

import com.toyota.productservice.dto.ProductDto;
import com.toyota.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Test
    public void testGetActiveProducts() {
        // Mock the ProductService
        Page<ProductDto> mockProductPage = Page.empty();
        when(productService.getProducts(anyInt(), anyInt(), anyString(), anyDouble(), anyDouble(), anyBoolean(), anyString(), anyString()))
                .thenReturn(mockProductPage);

        // Call the controller method
        Page<ProductDto> result = productController.getActiveProducts(0, 5, "", 0.0, Double.MAX_VALUE, true, "name", "ASC");

        // Assertions
        assertNotNull(result);
        assertEquals(mockProductPage, result);
    }

    @Test
    public void testGetProductById() {
        // Mock the ProductService
        ProductDto mockProductDto = new ProductDto();
        mockProductDto.setId(1L);
        when(productService.getProductById(anyLong())).thenReturn(mockProductDto);

        // Call the controller method
        ProductDto result = productController.getProductById(1L).getBody();

        // Assertions
        assertNotNull(result);
        assertEquals(mockProductDto.getId(), result.getId());
    }

    @Test
    public void testCreateProduct() {
        // Mock the ProductService
        ProductDto mockProductDto = new ProductDto();
        mockProductDto.setId(1L);
        when(productService.createProduct(any(ProductDto.class))).thenReturn(mockProductDto);

        // Call the controller method
        ProductDto result = productController.createProduct(new ProductDto()).getBody();

        // Assertions
        assertNotNull(result);
        assertEquals(mockProductDto.getId(), result.getId());
    }

    @Test
    public void testUpdateProduct() {
        // Mock the ProductService
        ProductDto mockProductDto = new ProductDto();
        mockProductDto.setId(1L);
        when(productService.updateProduct(anyLong(), any(ProductDto.class))).thenReturn(mockProductDto);

        // Call the controller method
        ProductDto result = productController.updateProduct(1L, new ProductDto()).getBody();

        // Assertions
        assertNotNull(result);
        assertEquals(mockProductDto.getId(), result.getId());
    }

    @Test
    public void testDeleteProduct() {
        // Call the controller method
        ResponseEntity<Void> result = productController.deleteProduct(1L);

        // Assertions
        assertNotNull(result);
        assertEquals(204, result.getStatusCodeValue());
    }
}
