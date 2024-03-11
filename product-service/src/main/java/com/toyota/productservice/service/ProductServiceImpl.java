package com.toyota.productservice.service;

import com.toyota.productservice.Mapper.ProductMapper;
import com.toyota.productservice.dao.ProductRepository;
import com.toyota.productservice.domain.Product;
import com.toyota.productservice.dto.ProductDto;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        Product product = ProductMapper.mapToEntity(productDto);
        Product savedProduct = productRepository.save(product);
        logger.info("Product {} is created", product.getName());

        return ProductMapper.mapToDto(savedProduct);
    }
    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductMapper::mapToDto).collect(Collectors.toList());
    }
    public Page<Product> getAllProductsByFiltering(String firstNameFilter, int page, int size, List<String> sortList, String sortOrder) {
        Sort sort = createSortOrder(sortList, sortOrder);
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findProductsByName(firstNameFilter, pageable);
    }

    private Sort createSortOrder(List<String> sortList, String sortOrder) {
        List<Sort.Order> orders = sortList.stream()
                .map(field -> sortOrder.equalsIgnoreCase("DESC") ? Sort.Order.desc(field) : Sort.Order.asc(field))
                .collect(Collectors.toList());
        return Sort.by(orders);
    }


    @Override
    public ProductDto getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            return ProductMapper.mapToDto(optionalProduct.get());
        } else {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
    }



    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();
            if (productDto.isActive()) {
                Product updatedProduct = ProductMapper.mapToEntity(productDto);
                updatedProduct.setId(existingProduct.getId());
                updatedProduct.setActive(true);
                logger.info("Product with id {} is updated", id);
                return ProductMapper.mapToDto(productRepository.save(updatedProduct));
            } else {
                logger.warn("Attempted to update product with inactive status");
                throw new IllegalArgumentException("Cannot update product with inactive status");
            }
        } else {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
    }

    @Override
    public void softDeleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setActive(false); // Soft delete
            productRepository.save(product);
            logger.info("Product with id {} soft deleted", id);
        } else {
            logger.warn("Attempted to soft delete product with id {} but not found", id);
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
    }




}
