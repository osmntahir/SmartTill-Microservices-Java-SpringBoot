package com.toyota.reportservice.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;

import com.toyota.reportservice.dto.SaleDto;
import com.toyota.reportservice.service.PdfGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@ContextConfiguration(classes = {PDFController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class PDFControllerTest {
    @Autowired
    private PDFController pDFController;

    @MockBean
    private PdfGenerator pdfGenerator;

    @MockBean
    private RestTemplate restTemplate;

    /**
     * Method under test: {@link PDFController#generatePDF(Long)}
     */
    @Test
    void testGeneratePDF() throws Exception {
        // Arrange
        when(pdfGenerator.generatePDF(Mockito.<SaleDto>any())).thenReturn("AXAXAXAX".getBytes("UTF-8"));
        when(restTemplate.getForObject(Mockito.<String>any(), Mockito.<Class<SaleDto>>any(), isA(Object[].class)))
                .thenReturn(new SaleDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/report/sale/{saleId}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(pDFController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/octet-stream"))
                .andExpect(MockMvcResultMatchers.content().string("AXAXAXAX"));
    }
    @Test
    public void testGeneratePDFIOException() throws Exception {
        // Arrange
        Long saleId = 1L;

        // Simulate an IOException being thrown by the PDF generator
        when(restTemplate.getForObject(anyString(), eq(SaleDto.class))).thenReturn(new SaleDto());
        when(pdfGenerator.generatePDF(any(SaleDto.class))).thenThrow(new IOException("Simulated IO Exception"));

        // Act
        ResponseEntity<byte[]> response = pDFController.generatePDF(saleId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}
