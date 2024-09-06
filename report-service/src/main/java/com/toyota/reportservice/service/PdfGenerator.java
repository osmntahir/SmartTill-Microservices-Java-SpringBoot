package com.toyota.reportservice.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.toyota.reportservice.dto.SaleDto;
import com.toyota.reportservice.dto.SoldProductDto;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PdfGenerator {

    public byte[] generatePDF(SaleDto saleDto) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Add store and company information
        addStoreInfo(document);

        // Add sale details
        addSaleDetails(document, saleDto);

        // Add sold products details
        addSoldProductsDetails(document, saleDto.getSoldProducts());

        // Add total and payment info
        addTotalInfo(document, saleDto);

        document.close();

        // Save the PDF to a file
        savePDFToFile(outputStream.toByteArray(), saleDto.getId());

        return outputStream.toByteArray();
    }

    private void savePDFToFile(byte[] pdfContent, Long saleId) throws IOException {
        String directoryPath = "reports";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = directoryPath + "/sale_report_" + saleId + ".pdf";
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfContent);
            fos.flush();
        }

        System.out.println("PDF saved at: " + filePath);
    }

    private void addStoreInfo(Document document) {
        Paragraph storeName = new Paragraph("32 BIT")
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(14)
                .setMarginBottom(2);
        document.add(storeName);

        Paragraph storeAddress = new Paragraph("DEMIRCİKARA MAH. 1431 SOK. NO:12\n0242 311 41 21\nANTALYA")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10)
                .setMarginBottom(2);
        document.add(storeAddress);

        document.add(new Paragraph("\n").setMarginBottom(2));  // Reduce space after store info
    }

    private void addSaleDetails(Document document, SaleDto saleDto) {
        String formattedDate = saleDto.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String formattedTime = saleDto.getDate().format(DateTimeFormatter.ofPattern("HH:mm"));

        Paragraph saleDetails = new Paragraph("TARIH: " + formattedDate + "    SAAT: " + formattedTime)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10)
                .setMarginBottom(2);
        document.add(saleDetails);

        Paragraph saleInfo = new Paragraph("SATIS NO: " + saleDto.getId() + "    KASIYER: " + saleDto.getCashierName())
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10)
                .setMarginBottom(2);
        document.add(saleInfo);

        document.add(new Paragraph("------------------------------------")
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(2));
    }

    private void addSoldProductsDetails(Document document, List<SoldProductDto> soldProducts) {
        for (SoldProductDto product : soldProducts) {
            // Product name, quantity, and unit price
            String productInfo = product.getProductName() + " (" + product.getQuantity() + " ADET x " + String.format("%.2f", product.getPrice()) + " TL)";
            Paragraph productParagraph = new Paragraph(productInfo)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(2);
            document.add(productParagraph);

            // Total price and discounted price with proper spacing
            String totalInfo = "Toplam Fiyat: " + String.format("%.2f", product.getQuantity() * product.getPrice()) + " TL";
            String discountInfo = "Indirim: " + String.format("%.2f", product.getDiscountAmount()) + " TL";
            String finalPriceInfo = "Indirimli Fiyat: " + String.format("%.2f", product.getFinalPriceAfterDiscount()) + " TL";

            Paragraph pricesParagraph = new Paragraph(totalInfo + "    " + discountInfo + "    " + finalPriceInfo)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(2);
            document.add(pricesParagraph);

            // Add separation line between products
            document.add(new Paragraph("------------------------------------")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(2));
        }
    }

    private void addTotalInfo(Document document, SaleDto saleDto) {
        Paragraph totalPrice = new Paragraph("GENEL TOPLAM: " + String.format("%.2f", saleDto.getTotalPrice()) + " TL")
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10)
                .setMarginBottom(2);
        document.add(totalPrice);

        if (saleDto.getTotalDiscountAmount() > 0) {
            Paragraph discountedTotal = new Paragraph("INDIRIMLI TOPLAM: " + String.format("%.2f", saleDto.getTotalDiscountedPrice()) + " TL")
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(10)
                    .setMarginBottom(2);
            document.add(discountedTotal);
        }

        document.add(new Paragraph("------------------------------------")
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(2));

        document.add(new Paragraph("KDV FISI DEGILDIR")
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10)
                .setMarginBottom(2));
    }
}
