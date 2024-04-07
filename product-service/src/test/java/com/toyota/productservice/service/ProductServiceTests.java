package com.toyota.productservice.service;

import com.toyota.productservice.dao.ProductRepository;
import com.toyota.productservice.domain.Product;
import com.toyota.productservice.dto.ProductDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void TestCreateProduct() {
        // Arrange
        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setPrice(10.0);
        productDto.setDescription("Test Description");
        productDto.setInventory(10);

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName(productDto.getName());
        savedProduct.setPrice(productDto.getPrice());
        savedProduct.setDescription(productDto.getDescription());

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        ProductDto createdProduct = productService.createProduct(productDto);

        // Assert
        assertEquals(savedProduct.getId(), createdProduct.getId());
        assertEquals(savedProduct.getName(), createdProduct.getName());
        assertEquals(savedProduct.getPrice(), createdProduct.getPrice());
        assertEquals(savedProduct.getDescription(), createdProduct.getDescription());
        assertEquals(savedProduct.getInventory(), createdProduct.getInventory());
    }

    /**
     * Test for createProduct method with null name and price
     */
    @Test
    public void TestCreateProduct_NullName_ThrowsIllegalArgumentException() {
        // Arrange
        ProductDto productDto = new ProductDto();
        // productDto has no name. then it should throw IllegalArgumentException

        productDto.setPrice(10.0);
        productDto.setDescription("Test Description");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(productDto);
        });
    }

    /**
     * Test for createProduct method with null price
     */

    @Test
    void TestCreateProduct_NullPrice_ThrowsIllegalArgumentException() {
        // Arrange
        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        // productDto has no price. then it should throw IllegalArgumentException
        productDto.setDescription("Test Description");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(productDto);
        });
    }

    /**
     * Test for createProduct method with null name and price
     */
    @Test
    void TestCreateProduct_NullNameAndPrice_ThrowsIllegalArgumentException() {
        // Arrange
        ProductDto productDto = new ProductDto();
        productDto.setDescription("Test Description");
        // productDto has no name and price. then it should throw IllegalArgumentException

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(productDto);
        });
    }

    /**
     * Test for getProductById method
     */
    @Test
    public void TestGetProductById_ExistingId_ReturnsProductDto() {
        // Arrange
        Long id = 1L;
        Product product = new Product(id, "Test Product", 10.0, 10);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // Act
        ProductDto result = productService.getProductById(id);

        // Assert
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getPrice(), result.getPrice());
    }

    /**
     * Test for getProductById method with non-existing id
     */
    @Test
    public void TestGetProductById_NonExistingId_ThrowsEntityNotFoundException() {
        // Arrange
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            productService.getProductById(id);
        });
    }

    /**
     * Test for softDeleteProduct method
     */
    @Test
    public void testSoftDeleteProduct() {
        // Arrange
        Long id = 1L;
        Product product = new Product(id, "Test Product", 10.0, 10);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // Act
        productService.softDeleteProduct(id);

        // Assert
        verify(productRepository, times(1)).save(product);
    }

    /**
     * Test for softDeleteProduct method with non-existing id
     */
    @Test
    public void testSoftDeleteById_NonExistingId_ThrowsEntityNotFoundException() {
        // Arrange
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            productService.softDeleteProduct(id);
        });
    }

    /**
     * Test for updateProduct method
     */
    @Test
    public void testUpdateProduct() {
        // Arrange
        Long id = 1L;
        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setPrice(10.0);
        productDto.setDescription("Test Description");
        productDto.setActive(true);

        Product existingProduct = new Product();
        existingProduct.setId(id);
        existingProduct.setName("Existing Product");
        existingProduct.setPrice(20.0);
        existingProduct.setDescription("Existing Description");
        existingProduct.setActive(true);

        Product updatedProduct = new Product();
        updatedProduct.setId(id);
        updatedProduct.setName(productDto.getName());
        updatedProduct.setPrice(productDto.getPrice());
        updatedProduct.setDescription(productDto.getDescription());
        updatedProduct.setActive(true);

        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);

        // Act
        ProductDto result = productService.updateProduct(id, productDto);

        // Assert
        assertEquals(updatedProduct.getId(), result.getId());
        assertEquals(updatedProduct.getName(), result.getName());
        assertEquals(updatedProduct.getPrice(), result.getPrice());
        assertEquals(updatedProduct.getDescription(), result.getDescription());
    }

    /**
     * Test for updateProduct method with non-existing id
     */
    @Test
    public void testUpdateProduct_NonExistingId_ThrowsEntityNotFoundException() {
        // Arrange
        Long id = 1L;
        ProductDto productDto = new ProductDto();
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            productService.updateProduct(id, productDto);
        });
    }

    /**
     * Test for updateProduct method with inactive product
     */

    @Test
    public void testUpdateProduct_InactiveProduct_ThrowsIllegalArgumentException() {
        // Arrange
        Long id = 1L;
        ProductDto productDto = new ProductDto();
        productDto.setActive(false);
        Product existingProduct = new Product();
        existingProduct.setActive(true);
        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(id, productDto);
        });
    }

    /**
     * Test for updateProduct method with inactive product
     */
    @Test
    public void testUpdateProduct_InactiveProduct_DoesNotUpdateProduct() {
        // Arrange
        Long id = 1L;
        ProductDto productDto = new ProductDto();
        productDto.setActive(false);
        Product existingProduct = new Product();
        existingProduct.setActive(true);
        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        // Act
        try {
            productService.updateProduct(id, productDto);
        } catch (IllegalArgumentException e) {
            // Assert
            verify(productRepository, never()).save(any(Product.class));
        }
    }
}


