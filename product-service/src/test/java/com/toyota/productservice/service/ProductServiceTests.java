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
        assertEquals(savedProduct.getInventory() , createdProduct.getInventory());
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
     *
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
     *
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
     *
     * Test for getAllProducts method
     */
    @Test
    public void testGetAllProducts() {
        // Mock data
        List<Product> mockProducts = Arrays.asList(
                new Product( "Product 1", 10),
                new Product( "Product 2", 20),
                new Product( "Product 3", 30)
        );

        // Mocking behavior
        when(productRepository.findAll()).thenReturn(mockProducts);

        // Call the method
        List<ProductDto> result = productService.getAllProducts();

        // Assertions
        assertEquals(mockProducts.size(), result.size()); // Check if the size matches
        for (int i = 0; i < mockProducts.size(); i++) {
            assertEquals(mockProducts.get(i).getName(), result.get(i).getName()); // Check if names match
            assertEquals(mockProducts.get(i).getPrice(), result.get(i).getPrice()); // Check if prices match
            assertEquals(mockProducts.get(i).getInventory(), result.get(i).getInventory()); // Check if inventory match
        }
        // Verify that productRepository.findAll() is called once
        verify(productRepository, times(1)).findAll();
    }
    /**
     *
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
     *
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
     *
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
     *
     * Test for softDeleteProduct method with non-existing id
     */
    @Test
    public void testSoftDeleteById_NonExistingId_ThrowsEntityNotFoundException()
    {
        // Arrange
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            productService.softDeleteProduct(id);
        });
    }
    /**
     *
     * Test for updateProduct method
     */
    @Test
    public void testUpdateProduct()
    {
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
/** Test for updateProduct method with non-existing id
     */
    @Test
    public void testUpdateProduct_NonExistingId_ThrowsEntityNotFoundException()
    {
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
    public void testUpdateProduct_InactiveProduct_ThrowsIllegalArgumentException()
    {
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
    public void testUpdateProduct_InactiveProduct_DoesNotUpdateProduct()
    {
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
// burda kaldim
    @Test
    public void testGetAllProductsByFiltering() {
        // Test data
        String firstNameFilter = "filter";
        int page = 0;
        int size = 10;
        List<String> sortList = Arrays.asList("fieldName1", "fieldName2");
        String sortOrder = "ASC";

        // Mocking
        List<Product> products = Arrays.asList(
                new Product( "Product 1",15),
                new Product( "Product 2",20),
                new Product( "Product 3",30)
        );
        Page<Product> productPage = new PageImpl<>(products);
        Sort sort = Sort.by(Sort.Direction.ASC, "fieldName1", "fieldName2");
        Pageable pageable = PageRequest.of(page, size, sort);
        when(productRepository.findProductsByName(firstNameFilter, pageable)).thenReturn(productPage);

        // Test
        Page<Product> result = productService.getAllProductsByFiltering(firstNameFilter, page, size, sortList, sortOrder);

        // Assertions
        assertEquals(productPage, result);
        verify(productRepository, times(1)).findProductsByName(firstNameFilter, pageable);
    }

    @Test
    public void testCreateSortOrder_PriceDescending() {
        // Test data
        List<String> sortList = Arrays.asList("price");
        String sortOrder = "DESC";

        // Call the method
        Sort sort = createSortOrder(sortList, sortOrder);

        // Assertions
        assertEquals(Sort.Order.desc("price"), sort.getOrderFor("price"));
    }

    @Test
    public void testCreateSortOrder_PriceAscending() {
        // Test data
        List<String> sortList = Arrays.asList("price");
        String sortOrder = "ASC";

        // Call the method
        Sort sort = createSortOrder(sortList, sortOrder);

        // Assertions
        assertEquals(Sort.Order.asc("price"), sort.getOrderFor("price"));
    }

    @Test
    public void testCreateSortOrder_OtherFieldDescending() {
        // Test data
        List<String> sortList = Arrays.asList("otherField");
        String sortOrder = "DESC";

        // Call the method
        Sort sort = createSortOrder(sortList, sortOrder);

        // Assertions
        assertEquals(Sort.Order.desc("otherField"), sort.getOrderFor("otherField"));
    }

    @Test
    public void testCreateSortOrder_OtherFieldAscending() {
        // Test data
        List<String> sortList = Arrays.asList("otherField");
        String sortOrder = "ASC";

        // Call the method
        Sort sort = createSortOrder(sortList, sortOrder);

        // Assertions
        assertEquals(Sort.Order.asc("otherField"), sort.getOrderFor("otherField"));
    }

    private Sort createSortOrder(List<String> sortList, String sortOrder) {
        List<Sort.Order> orders = sortList.stream()
                .map(field -> {
                    // Sıralama yapılacak alanı kontrol edelim
                    if (field.equalsIgnoreCase("price")) {
                        // Eğer sıralama yapılacak alan price ise, price alanına göre sıralama yapalım
                        return sortOrder.equalsIgnoreCase("DESC") ? Sort.Order.desc("price") : Sort.Order.asc("price");
                    } else {
                        // Diğer alanlar için mevcut mantığı kullanalım
                        return sortOrder.equalsIgnoreCase("DESC") ? Sort.Order.desc(field) : Sort.Order.asc(field);
                    }
                })
                .collect(Collectors.toList());
        return Sort.by(orders);
    }
}


