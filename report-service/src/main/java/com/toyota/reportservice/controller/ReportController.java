package com.toyota.reportservice.controller;

import com.toyota.reportservice.config.SaleClientService;
import com.toyota.reportservice.dto.PaginationResponse;
import com.toyota.reportservice.dto.SaleDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final SaleClientService saleClientService;

    public ReportController(SaleClientService saleClientService) {
        this.saleClientService = saleClientService;
    }

    @GetMapping("/sales")
    public PaginationResponse<SaleDto> getSalesReport(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "0.0") Double minTotalPrice,
            @RequestParam(defaultValue = "" + Double.MAX_VALUE) Double maxTotalPrice,
            @RequestParam(defaultValue = "2023-01-01T00:00:00") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now().toString()}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "") String paymentType,
            @RequestParam(defaultValue = "false") boolean deleted,
            @RequestParam(defaultValue = "") List<String> sortBy,
            @RequestParam(defaultValue = "ASC") String sortOrder) {

        return saleClientService.getSales(page, size, minTotalPrice, maxTotalPrice, startDate, endDate, paymentType, deleted, sortBy, sortOrder);
    }
}