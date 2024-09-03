package com.toyota.reportservice.service;

import com.toyota.reportservice.dto.SaleDto;
import com.toyota.reportservice.dto.SoldProductDto;
import com.toyota.reportservice.dto.PaymentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PdfGeneratorTest {

    private PdfGenerator pdfGenerator;
    private SaleDto saleDto;

    @BeforeEach
    void setUp() {
        pdfGenerator = new PdfGenerator();

        SoldProductDto soldProduct1 = new SoldProductDto(1L, 1L, "Product 1", 10.0, 100, 5.0, 50.0, 5, false);
        SoldProductDto soldProduct2 = new SoldProductDto(2L, 2L, "Product 2", 20.0, 200, 10.0, 100.0, 5, false);

        saleDto = new SaleDto(1L, LocalDateTime.now(), PaymentType.CREDIT_CARD, 150.0, Arrays.asList(soldProduct1, soldProduct2));
    }

    @Test
    void generatePDF_ShouldGenerateNonNullByteArray() throws IOException {
        // Ensure the directory exists
        File directory = new File("report-service/src/main/doc");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        byte[] pdfBytes = pdfGenerator.generatePDF(saleDto);
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
    }

    @Test
    void generatePDFs_ShouldGenerateNonNullByteArray() throws IOException {
        // Ensure the directory exists
        File directory = new File("report-service/src/main/doc");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        byte[] zipBytes = pdfGenerator.generatePDFs(Arrays.asList(saleDto));
        assertNotNull(zipBytes);
        assertTrue(zipBytes.length > 0);
    }
}
