package com.toyota.productservice.service;

import com.toyota.productservice.dto.ProductDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);

    ProductDto getProductById(Long id);

    ProductDto updateProduct(Long id, ProductDto productDto);

    void softDeleteProduct(Long id);

    Page<ProductDto> getProducts(int page, int size, String name, Double minPrice, Double maxPrice, boolean isActive, String sortBy, String sortDirection);

    Iterable<ProductDto> getProductsByIds(List<Long> productIds);
}
