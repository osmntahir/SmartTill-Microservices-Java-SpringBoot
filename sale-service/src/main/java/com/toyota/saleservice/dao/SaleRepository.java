package com.toyota.saleservice.dao;

import com.toyota.saleservice.domain.PaymentType;
import com.toyota.saleservice.domain.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT DISTINCT s FROM Sale s LEFT JOIN FETCH s.soldProducts sp WHERE s.totalPrice >= :minTotalPrice " +
            "AND s.totalPrice <= :maxTotalPrice " +
            "AND s.date BETWEEN :startDate AND :endDate " +
            "AND (:paymentType IS NULL OR s.paymentType = :paymentType) " +
            "AND s.deleted = :deleted")
    Page<Sale> getSalesFiltered(double minTotalPrice,
                                double maxTotalPrice,
                                LocalDateTime startDate,
                                LocalDateTime endDate,
                                PaymentType paymentType,
                                boolean deleted,
                                Pageable pageable);


    boolean existsByIdAndDeletedIsFalse(Long id);
}
