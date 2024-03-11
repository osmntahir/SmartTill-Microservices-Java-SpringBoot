package com.toyota.productservice.service;

import com.toyota.productservice.domain.Product;
import com.toyota.productservice.dto.ProductDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);

    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long id);

    ProductDto updateProduct(Long id, ProductDto productDto);

    void softDeleteProduct(Long id);

    public Page<Product> getAllProductsByFiltering(String firstNameFilter, int page, int size, List<String> sortList, String sortOrder) ;
}
