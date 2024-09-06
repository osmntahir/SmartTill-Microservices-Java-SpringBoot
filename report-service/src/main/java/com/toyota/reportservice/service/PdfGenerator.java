package com.toyota.reportservice.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.toyota.reportservice.dto.SaleDto;
import com.toyota.reportservice.dto.SoldProductDto;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PdfGenerator {

    // Generates PDF, saves to file, and returns byte array
    public byte[] generatePDF(SaleDto saleDto) throws IOException {
        // Using ByteArrayOutputStream to create the PDF in memory
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Add store and company information
        addStoreInfo(document);

        // Add sale details
        addSaleDetails(document, saleDto);

        // Add sold products table
        addSoldProductsTable(document, saleDto.getSoldProducts());

        // Add total and payment info
        addTotalInfo(document, saleDto);

        document.close();

        // Save the PDF to a file
        savePDFToFile(outputStream.toByteArray(), saleDto.getId());

        // Return the PDF as byte array
        return outputStream.toByteArray();
    }

    // Saves the PDF to the file system
    private void savePDFToFile(byte[] pdfContent, Long saleId) throws IOException {
        // Directory and filename
        String directoryPath = "reports";
        File directory = new File(directoryPath);

        // Create directory if it doesn't exist
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Construct the file path
        String filePath = directoryPath + "/sale_report_" + saleId + ".pdf";

        // Write to the file
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfContent);
            fos.flush();
        }

        System.out.println("PDF saved at: " + filePath);
    }

    // Adds store and company information
    private void addStoreInfo(Document document) {
        Paragraph storeName = new Paragraph("32 BIT ")
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16);
        document.add(storeName);

        Paragraph storeAddress = new Paragraph("DEMIRCIKARA MAH. 1431 SOK. NO:12\nAntalya")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(12);
        document.add(storeAddress);

        document.add(new Paragraph("\n")); // Add space
    }

    // Adds sale details
    private void addSaleDetails(Document document, SaleDto saleDto) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formattedDateTime = now.format(formatter);

        Paragraph saleDetails = new Paragraph("Date: " + formattedDateTime + "    Sale No: " + saleDto.getId() + "    Payment Type: " + saleDto.getPaymentType())
                .setTextAlignment(TextAlignment.LEFT)
                .setFontSize(12);
        document.add(saleDetails);

        Paragraph cashierInfo = new Paragraph("Cashier: " + saleDto.getCashierName())
                .setTextAlignment(TextAlignment.LEFT)
                .setFontSize(12);
        document.add(cashierInfo);

        document.add(new Paragraph("\n")); // Add space
    }

    // Adds sold products table
    private void addSoldProductsTable(Document document, List<SoldProductDto> soldProducts) {
        Table productTable = new Table(2);
        productTable.setWidth(UnitValue.createPercentValue(100));

        // Table headers
        productTable.addCell("Product Details");
        productTable.addCell("Price");

        // Product information
        for (SoldProductDto product : soldProducts) {
            productTable.addCell(product.getProductName() + " (" + product.getQuantity() + " pcs x " + product.getPrice() + ")");
            productTable.addCell(String.format("%.2f", product.getQuantity() * product.getPrice()) + " TL");
        }

        document.add(productTable);
        document.add(new Paragraph("\n"));
    }

    // Adds total and payment information
    private void addTotalInfo(Document document, SaleDto saleDto) {
        Paragraph totalPrice = new Paragraph("Total Price: " + String.format("%.2f", saleDto.getTotalPrice()) + " TL")
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(12);
        document.add(totalPrice);

        document.add(new Paragraph("\n"));

        Paragraph footer = new Paragraph("THIS IS NOT A TAX INVOICE")
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(12);
        document.add(footer);
    }
}
