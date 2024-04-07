package com.toyota.productservice.Mapper;

import com.toyota.productservice.domain.Product;
import com.toyota.productservice.dto.ProductDto;

/**
 * Utility class for mapping between Product and ProductDto objects.
 */
public class ProductMapper {

    /**
     * Maps a Product object to a ProductDto object.
     *
     * @param product the Product object to map
     * @return a ProductDto object mapped from the given Product object
     */
    public static ProductDto mapToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setInventory(product.getInventory());
        productDto.setActive(product.isActive());
        return productDto;
    }

    /**
     * Maps a ProductDto object to a Product object.
     *
     * @param productDto the ProductDto object to map
     * @return a Product object mapped from the given ProductDto object
     */
    public static Product mapToEntity(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setInventory(productDto.getInventory());
        product.setActive(productDto.isActive());
        return product;
    }
}
