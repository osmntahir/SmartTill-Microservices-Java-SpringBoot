package com.toyota.saleservice.dao;

import com.toyota.saleservice.domain.SoldProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SoldProductRepository extends JpaRepository<SoldProduct, Long> {

    @Query("SELECT s FROM SoldProduct s WHERE " +
            "(:name is null or s.name like %:name%) and " +
            "(:minPrice is null or s.price >= :minPrice) and " +
            "(:maxPrice is null or s.price <= :maxPrice) and " +
            "(:minQuantity is null or s.quantity >= :minQuantity) and " +
            "(:maxQuantity is null or s.quantity <= :maxQuantity) and " +
            "(:minDiscountPercentage is null or s.discount >= :minDiscountPercentage) and " +
            "(:maxDiscountPercentage is null or s.discount <= :maxDiscountPercentage) and " +
            "(:minDiscountAmount is null or s.discountAmount >= :minDiscountAmount) and " +
            "(:maxDiscountAmount is null or s.discountAmount <= :maxDiscountAmount) and " +
            "(:minFinalPriceAfterDiscount is null or s.finalPriceAfterDiscount >= :minFinalPriceAfterDiscount) and " +
            "(:maxFinalPriceAfterDiscount is null or s.finalPriceAfterDiscount <= :maxFinalPriceAfterDiscount) and " +
            "(:minTotalPrice is null or s.total >= :minTotalPrice) and " +
            "(:maxTotalPrice is null or s.total <= :maxTotalPrice) and " +
            "(:deleted is null or s.deleted = :deleted)")
    Page<SoldProduct> getSoldProductsFiltered(String name,
                                              Double minPrice, Double maxPrice,
                                              Integer minQuantity, Integer maxQuantity,
                                              Double minDiscountPercentage, Double maxDiscountPercentage,
                                              Double minDiscountAmount, Double maxDiscountAmount,
                                              Double minFinalPriceAfterDiscount, Double maxFinalPriceAfterDiscount,
                                              Double minTotalPrice, Double maxTotalPrice,
                                              boolean deleted, Pageable pageable);


    Optional<SoldProduct> findBySaleIdAndProductId(Long saleId, Long productId);

    List<SoldProduct> findAllBySaleId(Long saleId);

    List<SoldProduct> findAllBySaleIdAndDeletedIsFalse(Long saleId);
}
