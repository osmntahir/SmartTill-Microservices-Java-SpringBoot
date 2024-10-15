package com.toyota.saleservice.service.abstracts;

import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.dto.SaleDto;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleService {
    PaginationResponse<SaleDto> getSalesFiltered(int page,
                                                 int size,
                                                 List<String> sortBy,
                                                 String sortOrder,
                                                 double minTotalPrice,
                                                 double maxTotalPrice,
                                                 double minTotalDiscountAmount,
                                                 double maxTotalDiscountAmount,
                                                 double minTotalDiscountedPrice,
                                                 double maxTotalDiscountedPrice,
                                                 LocalDateTime startDate,
                                                 LocalDateTime endDate,
                                                 String paymentType,
                                                 String cashierName,
                                                 boolean deleted);


    SaleDto addSale(SaleDto saleDto, String cashierName);

    SaleDto updateSale(Long id, SaleDto saleDto);

    SaleDto deleteSale(Long id);

    SaleDto getSale(Long id);

    List<SaleDto> getAllSalesForGeneratePdf();
}
