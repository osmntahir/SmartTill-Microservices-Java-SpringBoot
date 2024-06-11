package com.toyota.saleservice.dao;

import com.toyota.saleservice.domain.CampaignProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignProductRepository extends JpaRepository<CampaignProduct, Long> {
}
