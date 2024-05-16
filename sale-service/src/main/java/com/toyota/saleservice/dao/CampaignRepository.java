package com.toyota.saleservice.dao;

import com.toyota.saleservice.domain.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
}
