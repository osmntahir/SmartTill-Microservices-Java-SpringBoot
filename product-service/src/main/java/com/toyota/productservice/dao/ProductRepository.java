package com.toyota.productservice.dao;

import com.toyota.productservice.domain.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Retrieves a page of products filtered by name, price range, and active status.
     *
     * @param name       the name of the product to filter by (can be partial, case-insensitive)
     * @param minPrice   the minimum price of the product to filter by
     * @param maxPrice   the maximum price of the product to filter by
     * @param isActive   the active status of the product to filter by
     * @param pageable   pagination information
     * @return a page of products that match the filter criteria
     */
    @Query("SELECT p FROM Product p " +
            "WHERE (:name IS NULL OR LOWER(p.name) LIKE %:name%) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:isActive IS NULL OR p.active = :isActive)")
    Page<Product> getProductsFiltered(
            @Param("name") String name,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("isActive") Boolean isActive,
            Pageable pageable);
}