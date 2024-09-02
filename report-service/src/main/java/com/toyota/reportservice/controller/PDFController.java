package com.toyota.reportservice.controller;

import com.toyota.reportservice.dto.SaleDto;
import com.toyota.reportservice.service.PdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;

@RestController
public class PDFController {

    private final PdfGenerator pdfGenerator;
    private final RestTemplate restTemplate;  // Use RestTemplate or FeignClient to call the Sale Service

    @Autowired
    public PDFController(PdfGenerator pdfGenerator, RestTemplate restTemplate) {
        this.pdfGenerator = pdfGenerator;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/report/sale/{saleId}")
    public ResponseEntity<byte[]> generatePDF(@PathVariable Long saleId) {
        try {
            // Call the Sale Service to get the SaleDto
            SaleDto saleDto = restTemplate.getForObject("http://sale-service/sale/get/" + saleId, SaleDto.class);


            // Generate PDF from the retrieved SaleDto
            byte[] pdfContent = pdfGenerator.generatePDF(saleDto);

            return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=sale_report_" + saleId + ".pdf")
                    .body(pdfContent);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
