package com.toyota.reportservice.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.toyota.reportservice.dto.SaleDto;
import com.toyota.reportservice.dto.SoldProductDto;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class PdfGenerator {

    public byte[] generatePDF(SaleDto saleDto) throws IOException {
        // Directory where the PDF will be saved
        String directoryPath = "report-service/src/main/doc";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir(); // Create the directory if it doesn't exist
        }

        // File path
        String filePath = directoryPath + "/sale_report_" + saleDto.getId() + ".pdf";

        try (PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document document = new Document(pdfDocument)) {

            addTitleText(document, "Sales Report");
            addSaleDetails(document, saleDto);
            addSoldProductsTable(document, saleDto.getSoldProducts());

            System.out.println("PDF generated at: " + filePath); // Optional: log the file path
        }

        // Convert the generated file to a byte array (optional)
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            return outputStream.toByteArray();
        }
    }
    public byte[] generatePDFs(List<SaleDto> sales) throws IOException {
        ByteArrayOutputStream zipStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(zipStream)) {
            for (int i = 0; i < sales.size(); i++) {
                SaleDto sale = sales.get(i);
                byte[] pdfContent = generatePDF(sale);  // Assuming generatePDF(SaleDto sale) is defined in PdfGenerator
                zipOutputStream.putNextEntry(new ZipEntry("sale_report_" + (i + 1) + ".pdf"));
                zipOutputStream.write(pdfContent);
                zipOutputStream.closeEntry();
            }
        }
        return zipStream.toByteArray();
    }


    private void addTitleText(Document document, String titleText) {
        Paragraph paragraph = new Paragraph(titleText)
                .setBold()
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20);
        document.add(paragraph);
    }

    private void addSaleDetails(Document document, SaleDto saleDto) {
        Table saleDetailsTable = new Table(2);
        saleDetailsTable.addCell("Sale ID");
        saleDetailsTable.addCell(String.valueOf(saleDto.getId()));
        saleDetailsTable.addCell("Date");
        saleDetailsTable.addCell(saleDto.getDate().toString());
        saleDetailsTable.addCell("Payment Type");
        saleDetailsTable.addCell(saleDto.getPaymentType().toString());
        saleDetailsTable.addCell("Total Price");
        saleDetailsTable.addCell("$" + saleDto.getTotalPrice());
        document.add(saleDetailsTable);
    }

    private void addSoldProductsTable(Document document, List<SoldProductDto> soldProducts) {
        Table productTable = new Table(4);
        productTable.addCell("Product Name");
        productTable.addCell("Quantity");
        productTable.addCell("Unit Price");
        productTable.addCell("Total");

        for (SoldProductDto product : soldProducts) {
            productTable.addCell(product.getProductName());
            productTable.addCell(String.valueOf(product.getQuantity()));
            productTable.addCell("$" + product.getPrice());
            productTable.addCell("$" + product.getTotal());
        }

        document.add(productTable);
    }
}
