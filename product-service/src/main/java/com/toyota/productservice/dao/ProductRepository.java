package com.toyota.productservice.dao;

import com.toyota.productservice.domain.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    static final String FILTER_PRODUCTS_ON_NAME = "SELECT p FROM Product p WHERE p.name = :name";

    @Query(FILTER_PRODUCTS_ON_NAME)
    Page<Product> findProductsByName(String name, Pageable pageable);

}
