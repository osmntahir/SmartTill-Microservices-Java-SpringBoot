package com.toyota.saleservice.service.abstracts;

import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.dto.SoldProductDto;

public interface SoldProductService {
    SoldProductDto addSoldProduct(Long productId,Long SaleId, SoldProductDto soldProductDto);

    SoldProductDto deleteSoldProduct(Long id);

    SoldProductDto updateSoldProduct(Long id, SoldProductDto soldProductDto);

    PaginationResponse<SoldProductDto> getSoldProducts(int page, int size, String name,
                                                       Double minPrice, Double maxPrice,
                                                       Integer minQuantity, Integer maxQuantity,
                                                       Double minDiscountPercentage,
                                                       Double maxDiscountPercentage,
                                                       Double minDiscountAmount,
                                                       Double maxDiscountAmount,
                                                       Double minFinalPriceAfterDiscount,
                                                       Double maxFinalPriceAfterDiscount,
                                                       Double minTotalPrice,
                                                       Double maxTotalPrice,
                                                       boolean deleted, String sortBy,
                                                       String sortDirection);
}
