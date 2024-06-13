package com.toyota.saleservice.service.abstracts;

import com.toyota.saleservice.dto.SoldProductDto;

public interface SoldProductService {
    SoldProductDto addSoldProduct(Long productId, SoldProductDto soldProductDto);
}
