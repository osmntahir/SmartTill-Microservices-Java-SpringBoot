package com.toyota.reportservice.controller;



import com.toyota.reportservice.dto.SaleDto;
import com.toyota.reportservice.service.PdfGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class PDFController {
    private final PdfGenerator pdfGenerator;

    public PDFController(PdfGenerator pdfGenerator) {
        this.pdfGenerator = pdfGenerator;
    }

    @PostMapping("/report/sales")
    public ResponseEntity<byte[]> generateMultiplePDFs(@RequestBody List<SaleDto> sales) {
        try {
            byte[] zipContent = pdfGenerator.generatePDFs(sales);
            return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=sales_reports.zip")
                    .body(zipContent);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
