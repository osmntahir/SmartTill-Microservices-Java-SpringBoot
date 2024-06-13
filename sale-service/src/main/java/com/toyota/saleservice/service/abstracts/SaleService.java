package com.toyota.saleservice.service.abstracts;

import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.dto.SaleDto;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleService {
    PaginationResponse<SaleDto> getSalesFiltered(int page, int size, List<String> sortBy, String sortOrder, double totalPrice, LocalDateTime date, String paymentType, boolean deleted);

    SaleDto addSale(SaleDto saleDto);

    SaleDto updateSale(Long id, SaleDto saleDto);

    SaleDto deleteSale(Long id);
}
