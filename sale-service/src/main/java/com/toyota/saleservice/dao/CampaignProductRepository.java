package com.toyota.saleservice.dao;

import com.toyota.saleservice.domain.CampaignProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignProductRepository extends JpaRepository<CampaignProduct, Long> {
    List<CampaignProduct> findByProductIdAndDeletedFalse(Long productId);

    List<CampaignProduct> findByCampaignIdAndDeletedFalse(Long campaignId);
}
