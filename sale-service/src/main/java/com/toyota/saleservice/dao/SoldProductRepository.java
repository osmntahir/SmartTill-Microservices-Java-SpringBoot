package com.toyota.saleservice.dao;

import com.toyota.saleservice.domain.SoldProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SoldProductRepository extends JpaRepository<SoldProduct, Long> {


    @Query("SELECT s FROM SoldProduct s WHERE " +
            "(:name is null or s.product.name like %:name%) and " +
            "(:minPrice is null or s.product.price >= :minPrice) and " +
            "(:maxPrice is null or s.product.price <= :maxPrice) and " +
            "(:deleted is null or s.deleted = :deleted)")

    Page<SoldProduct> getSoldProductsFiltered(String name, Double minPrice, Double maxPrice, boolean deleted, Pageable pageable);
}
