package com.toyota.reportservice.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.toyota.reportservice.dto.SaleDto;
import com.toyota.reportservice.dto.SoldProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedConstruction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PdfGeneratorTest {
    @Mock
    private Document document;

    @InjectMocks
    private PdfGenerator pdfGenerator;

    private SaleDto saleDto;
    private List<SoldProductDto> soldProductList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // Mocking SaleDto and SoldProductDto objects
        saleDto = new SaleDto();
        saleDto.setId(1L);
        saleDto.setCashierName("Test Cashier");
        saleDto.setDate(LocalDateTime.now());
        saleDto.setTotalPrice(100.00);
        saleDto.setTotalDiscountAmount(10.00);
        saleDto.setTotalDiscountedPrice(90.00);

        soldProductList = new ArrayList<>();
        SoldProductDto soldProduct = new SoldProductDto();
        soldProduct.setProductName("Test Product");
        soldProduct.setPrice(20.00);
        soldProduct.setQuantity(5);
        soldProduct.setDiscountAmount(10.00);
        soldProduct.setFinalPriceAfterDiscount(90.00);
        soldProductList.add(soldProduct);

        saleDto.setSoldProducts(soldProductList);
    }

    @Test
    @Disabled
    void testGeneratePDF() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (MockedConstruction<PdfWriter> mockedWriter = mockConstruction(PdfWriter.class);
             MockedConstruction<PdfDocument> mockedDocument = mockConstruction(PdfDocument.class);
             MockedConstruction<Document> mockedDoc = mockConstruction(Document.class)) {

            byte[] pdfContent = pdfGenerator.generatePDF(saleDto);

            assertThat(pdfContent).isNotNull();
            assertThat(pdfContent.length).isGreaterThan(0);

            verify(document, times(1)).close();
        }
    }

    @Test
    void testSavePDFToFile() throws IOException {
        byte[] pdfContent = new byte[]{1, 2, 3};
        String directoryPath = "reports";
        String filePath = directoryPath + "/sale_report_" + saleDto.getId() + ".pdf";
        File file = new File(filePath);

        pdfGenerator.savePDFToFile(pdfContent, saleDto.getId());

        assertThat(file.exists()).isTrue();
        assertThat(file.length()).isGreaterThan(0);

        // Cleanup after test
        file.delete();
        assertThat(file.exists()).isFalse();
    }

    @Test
    void testAddStoreInfo() throws Exception {
        // Use reflection to access private method
        java.lang.reflect.Method method = PdfGenerator.class.getDeclaredMethod("addStoreInfo", Document.class);
        method.setAccessible(true);
        method.invoke(pdfGenerator, document);

        // We expect 3 invocations since the store name, address, and spacing are added
        verify(document, times(3)).add(any(Paragraph.class));
    }

    @Test
    void testAddSaleDetails() throws Exception {
        // Use reflection to access private method
        java.lang.reflect.Method method = PdfGenerator.class.getDeclaredMethod("addSaleDetails", Document.class, SaleDto.class);
        method.setAccessible(true);
        method.invoke(pdfGenerator, document, saleDto);

        // We expect 3 invocations since the date, sale number, and line separator are added
        verify(document, times(3)).add(any(Paragraph.class));
    }

    @Test
    @Disabled
    void testAddSoldProductsDetails() throws Exception {
        // Use reflection to access private method
        java.lang.reflect.Method method = PdfGenerator.class.getDeclaredMethod("addSoldProductsDetails", Document.class, List.class);
        method.setAccessible(true);
        method.invoke(pdfGenerator, document, soldProductList);

        // We expect 4 invocations for the product details (name, total, discount, final price) and separator
        verify(document, times(4)).add(any(Paragraph.class));
    }

    @Test
    @Disabled
    void testAddTotalInfo() throws Exception {
        // Use reflection to access private method
        java.lang.reflect.Method method = PdfGenerator.class.getDeclaredMethod("addTotalInfo", Document.class, SaleDto.class);
        method.setAccessible(true);
        method.invoke(pdfGenerator, document, saleDto);

        // We expect 3 invocations since the total price, discounted total (if applicable), and separator are added
        verify(document, times(3)).add(any(Paragraph.class));
    }
}
