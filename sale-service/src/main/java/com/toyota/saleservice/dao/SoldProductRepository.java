package com.toyota.saleservice.dao;

import com.toyota.saleservice.domain.SoldProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoldProductRepository extends JpaRepository<SoldProduct, Long> {
}
