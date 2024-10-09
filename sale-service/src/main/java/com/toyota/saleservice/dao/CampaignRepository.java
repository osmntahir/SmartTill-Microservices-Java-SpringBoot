package com.toyota.saleservice.dao;

import com.toyota.saleservice.domain.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    @Query("SELECT c FROM Campaign c WHERE " +
            "(:name IS NULL OR c.name LIKE %:name%) AND " +
            "(:minDiscount IS NULL OR c.discountPercentage >= :minDiscount) AND " +
            "(:maxDiscount IS NULL OR c.discountPercentage <= :maxDiscount) AND " +
            "(:deleted IS NULL OR c.deleted = :deleted)")
    Page<Campaign> getCampaignsFiltered(String name,
                                        Double minDiscount, Double maxDiscount,
                                        boolean deleted, Pageable pageable);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    @Query("SELECT c FROM Campaign c WHERE c.deleted = false")
    List<Campaign> findActiveCampaigns();

    @Query("SELECT c FROM Campaign c WHERE c.deleted = false AND :productId MEMBER OF c.productIds")
    List<Campaign> findActiveCampaignsByProductIdNative(Long productId);
}
