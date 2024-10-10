package com.toyota.reportservice.config;

import com.toyota.reportservice.dto.PaginationResponse;
import com.toyota.reportservice.dto.SaleDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleClientService {

    private final RestTemplate restTemplate;

    private final String saleServiceUrl = "http://sale-service/sale";

    public SaleClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PaginationResponse<SaleDto> getSales(int page, int size, double minTotalPrice, double maxTotalPrice,
                                                double minTotalDiscountAmount, double maxTotalDiscountAmount,
                                                double minTotalDiscountedPrice, double maxTotalDiscountedPrice,
                                                LocalDateTime startDate, LocalDateTime endDate, String paymentType,
                                                String cashierName, boolean deleted, List<String> sortBy, String sortOrder) {
        String url = UriComponentsBuilder.fromHttpUrl(saleServiceUrl + "/getAll")
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("minTotalPrice", minTotalPrice)
                .queryParam("maxTotalPrice", maxTotalPrice)
                .queryParam("minTotalDiscountAmount", minTotalDiscountAmount)
                .queryParam("maxTotalDiscountAmount", maxTotalDiscountAmount)
                .queryParam("minTotalDiscountedPrice", minTotalDiscountedPrice)
                .queryParam("maxTotalDiscountedPrice", maxTotalDiscountedPrice)
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .queryParam("paymentType", paymentType)
                .queryParam("cashierName", cashierName)
                .queryParam("deleted", deleted)
                .queryParam("sortBy", String.join(",", sortBy))
                .queryParam("sortOrder", sortOrder)
                .toUriString();

        ResponseEntity<PaginationResponse<SaleDto>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<PaginationResponse<SaleDto>>() {});

        return response.getBody();
    }
}
