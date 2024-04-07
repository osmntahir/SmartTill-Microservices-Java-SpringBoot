package com.toyota.productservice.resource;

import com.toyota.productservice.domain.Product;
import com.toyota.productservice.dto.ProductDto;
import com.toyota.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    /**
     * Constructor based injection
     * @param productService
     */
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    /**
     * Method to get all products
     * @return List of ProductDto
     */

    /**
     * Retrieves a page of active products with optional filtering, sorting, and pagination.
     *
     * @param page           the page number (default: 0)
     * @param size           the size of the page (default: 5)
     * @param name           the name of the product to filter by (optional)
     * @param minPrice       the minimum price of the product to filter by (optional, default: 0)
     * @param maxPrice       the maximum price of the product to filter by (optional, default: Double.MAX_VALUE)
     * @param isActive       the active status of the product to filter by (default: true)
     * @param sortBy         the field to sort by (default: "name")
     * @param sortDirection  the sort direction, either "ASC" (ascending) or "DESC" (descending) (default: "ASC")
     * @return               a page of ProductDto objects representing the active products
     */
    @GetMapping("/getAll")
    public Page<ProductDto> getActiveProducts(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "5") int size,
                                              @RequestParam(defaultValue = "") String name,
                                              @RequestParam(required = false, defaultValue = "0") Double minPrice,
                                              @RequestParam(required = false, defaultValue = Double.MAX_VALUE + "") Double maxPrice,
                                              @RequestParam(defaultValue = "true") boolean isActive,
                                              @RequestParam(defaultValue = "name") String sortBy,
                                              @RequestParam(defaultValue = "ASC") String sortDirection) {
        return productService.getProducts(page, size, name, minPrice, maxPrice, isActive, sortBy, sortDirection);
    }

    /**
     * Method to get product by id
     * @param id
     * @return ProductDto
     */

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
/**
     * Method to create product
     * @param productDto
     * @return ProductDto
     */
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto createdProduct = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
/**
     * Method to update product
     * @param id
     * @param productDto
     * @return ProductDto
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        ProductDto updatedProduct = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(updatedProduct);
    }
/**         * Method to delete product
     * @param id
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.softDeleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
