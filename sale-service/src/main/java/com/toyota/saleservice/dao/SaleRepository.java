package com.toyota.saleservice.dao;

import com.toyota.saleservice.domain.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
public interface SaleRepository extends JpaRepository<Sale, Long>{

    @Query("SELECT s FROM Sale s WHERE " +
            "(:totalPrice IS NULL OR s.totalPrice >= :totalPrice) AND " +
            "(:date IS NULL OR s.date = :date) AND " +
            "(:paymentType IS NULL OR s.paymentType = :paymentType) AND " +
            "(:deleted IS NULL OR s.deleted = :deleted)")
    Page<Sale> getSalesFiltered(double totalPrice, LocalDateTime date, String paymentType, boolean deleted, Pageable pageable);

    boolean existsByIdAndDeletedIsFalse(Long id);
}
