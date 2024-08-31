package com.toyota.productservice.service;

import com.toyota.productservice.dto.ProductDTO;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDto);

    ProductDTO getProductById(Long id);

    ProductDTO updateProduct(Long id, ProductDTO productDto);

    void softDeleteProduct(Long id);

    Page<ProductDTO> getProducts(int page, int size, String name, Double minPrice, Double maxPrice, boolean isActive, String sortBy, String sortDirection);
}
