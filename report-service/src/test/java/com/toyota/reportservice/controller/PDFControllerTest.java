package com.toyota.reportservice.controller;

import com.toyota.reportservice.dto.SaleDto;
import com.toyota.reportservice.dto.SoldProductDto;
import com.toyota.reportservice.dto.PaymentType;
import com.toyota.reportservice.service.PdfGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PDFControllerTest {

    private PdfGenerator pdfGenerator;
    private RestTemplate restTemplate;
    private PDFController pdfController;
    private SaleDto saleDto;

    @BeforeEach
    void setUp() {
        pdfGenerator = Mockito.mock(PdfGenerator.class);
        restTemplate = Mockito.mock(RestTemplate.class);
        pdfController = new PDFController(pdfGenerator, restTemplate);

        SoldProductDto soldProduct1 = new SoldProductDto(1L, 1L, "Product 1", 10.0, 100, 5.0, 50.0, 5, false);
        SoldProductDto soldProduct2 = new SoldProductDto(2L, 2L, "Product 2", 20.0, 200, 10.0, 100.0, 5, false);

        saleDto = new SaleDto(1L, LocalDateTime.now(), PaymentType.CREDIT_CARD, 150.0, Arrays.asList(soldProduct1, soldProduct2));
    }

    @Test
    void generatePDF_Success() throws IOException {
        when(restTemplate.getForObject(anyString(), eq(SaleDto.class))).thenReturn(saleDto);
        when(pdfGenerator.generatePDF(any(SaleDto.class))).thenReturn(new byte[]{1, 2, 3});

        ResponseEntity<byte[]> response = pdfController.generatePDF(1L);

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(SaleDto.class));
        verify(pdfGenerator, times(1)).generatePDF(any(SaleDto.class));
    }

    @Test
    void generatePDF_Exception() throws IOException {
        when(restTemplate.getForObject(anyString(), eq(SaleDto.class))).thenReturn(saleDto);
        when(pdfGenerator.generatePDF(any(SaleDto.class))).thenThrow(new IOException());

        ResponseEntity<byte[]> response = pdfController.generatePDF(1L);

        assertEquals(500, response.getStatusCodeValue());
    }
}
