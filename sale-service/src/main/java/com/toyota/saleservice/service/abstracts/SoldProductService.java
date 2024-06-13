package com.toyota.saleservice.service.abstracts;

import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.dto.SoldProductDto;

public interface SoldProductService {
    SoldProductDto addSoldProduct(Long productId,Long SaleId, SoldProductDto soldProductDto);

    SoldProductDto deleteSoldProduct(Long id);

    SoldProductDto updateSoldProduct(Long id, SoldProductDto soldProductDto);

    PaginationResponse<SoldProductDto> getSoldProducts(int page, int size, String name, Double minPrice, Double maxPrice, boolean isActive, String sortBy, String sortDirection);
}
