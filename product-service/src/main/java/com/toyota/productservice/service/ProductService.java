package com.toyota.productservice.service;

import com.toyota.productservice.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);

    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long id);

    ProductDto updateProduct(Long id, ProductDto productDto);

    void softDeleteProduct(Long id);
}
