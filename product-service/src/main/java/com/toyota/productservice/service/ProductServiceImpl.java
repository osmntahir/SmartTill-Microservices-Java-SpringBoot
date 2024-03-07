package com.toyota.productservice.service;

import com.toyota.productservice.dao.ProductRepository;
import com.toyota.productservice.domain.Product;
import com.toyota.productservice.dto.ProductDto;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
   private final Logger logger = LogManager.getLogger(Product.class);

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = mapToEntity(productDto);
        Product savedProduct = productRepository.save(product);
        logger.info("Product {} is created", product.getName());

        return mapToDto(savedProduct);
    }
    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.map(this::mapToDto).orElse(null);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();
            Product updatedProduct = mapToEntity(productDto);
            updatedProduct.setId(existingProduct.getId());
        //    updatedProduct.setActive(existingProduct.isActive());
            return mapToDto(productRepository.save(updatedProduct));
        } else {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
    }

    @Override
    public void softDeleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
           // product.setActive(false); // Soft delete işareti
            productRepository.save(product);
           // logger.info("Product with id {} soft deleted", id);
        } else {
         //   logger.warn("Attempted to soft delete product with id {} but not found", id);
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
    }



    private ProductDto mapToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setInventory(product.getInventory());
        return productDto;
    }

    private Product mapToEntity(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setInventory(productDto.getInventory());
        return product;
    }
}
