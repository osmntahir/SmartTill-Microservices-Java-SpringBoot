package com.toyota.saleservice.resource;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.toyota.saleservice.config.ProductServiceClient;
import com.toyota.saleservice.dao.SaleRepository;
import com.toyota.saleservice.dao.SoldProductRepository;
import com.toyota.saleservice.domain.PaymentType;
import com.toyota.saleservice.domain.Sale;
import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.dto.SaleDto;
import com.toyota.saleservice.service.abstracts.SaleService;
import com.toyota.saleservice.service.common.MapUtil;
import com.toyota.saleservice.service.impl.SaleServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

@ContextConfiguration(classes = {SaleController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class SaleControllerTest {
    @Autowired
    private SaleController saleController;

    @MockBean
    private SaleService saleService;

    @Test
    void testGetSale() throws Exception {
        // Arrange
        when(saleService.getSale(Mockito.<Long>any())).thenReturn(new SaleDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/sale/get/{id}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(saleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"date\":null,\"paymentType\":null,\"totalPrice\":0.0,\"totalDiscountAmount\":0.0,\"totalDiscountedPrice"
                                        + "\":0.0,\"cashierName\":null,\"soldProducts\":null}"));
    }


    @Test
    void testGetSale2() throws Exception {
        // Arrange
        when(saleService.getSale(Mockito.<Long>any())).thenReturn(null);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/sale/get/{id}", 1L);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(saleController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }


    @Test
    void testUpdateSale() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        Sale sale = new Sale();
        sale.setCashierName("Cashier Name");
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalDiscountAmount(10.0d);
        sale.setTotalDiscountedPrice(10.0d);
        sale.setTotalPrice(10.0d);
        Optional<Sale> ofResult = Optional.of(sale);

        Sale sale2 = new Sale();
        sale2.setCashierName("Cashier Name");
        LocalDate ofResult2 = LocalDate.of(1970, 1, 1);
        sale2.setDate(ofResult2.atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
        sale2.setTotalDiscountAmount(10.0d);
        sale2.setTotalDiscountedPrice(10.0d);
        sale2.setTotalPrice(10.0d);
        SaleRepository saleRepository = mock(SaleRepository.class);
        when(saleRepository.save(Mockito.<Sale>any())).thenReturn(sale2);
        when(saleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        SoldProductRepository soldProductRepository = mock(SoldProductRepository.class);
        ProductServiceClient productServiceClient = new ProductServiceClient(mock(RestTemplate.class));
        ModelMapper modelMapper = new ModelMapper();
        SaleController saleController = new SaleController(new SaleServiceImpl(saleRepository, soldProductRepository,
                productServiceClient, new MapUtil(modelMapper, new ProductServiceClient(mock(RestTemplate.class)))));

        // Act
        ResponseEntity<SaleDto> actualUpdateSaleResult = saleController.updateSale(1L, new SaleDto());

        // Assert
        verify(saleRepository).findById(eq(1L));
        verify(saleRepository).save(isA(Sale.class));
        HttpStatusCode statusCode = actualUpdateSaleResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        SaleDto body = actualUpdateSaleResult.getBody();
        LocalDateTime date = body.getDate();
        assertEquals("00:00", date.toLocalTime().toString());
        LocalDate toLocalDateResult = date.toLocalDate();
        assertEquals("1970-01-01", toLocalDateResult.toString());
        assertEquals("Cashier Name", body.getCashierName());
        assertNull(saleController.getSale(1L).getBody().getPaymentType());
        assertEquals(10.0d, body.getTotalDiscountAmount());
        assertEquals(10.0d, body.getTotalDiscountedPrice());
        assertEquals(10.0d, body.getTotalPrice());
        assertEquals(1L, body.getId().longValue());
        assertEquals(200, actualUpdateSaleResult.getStatusCodeValue());
        assertEquals(PaymentType.CASH, body.getPaymentType());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(body.getSoldProducts().isEmpty());
        assertTrue(actualUpdateSaleResult.hasBody());
        assertTrue(actualUpdateSaleResult.getHeaders().isEmpty());
        assertSame(ofResult2, toLocalDateResult);
    }


    @Test
    void testUpdateSale7() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        SaleService saleService = mock(SaleService.class);
        when(saleService.updateSale(Mockito.<Long>any(), Mockito.<SaleDto>any())).thenReturn(null);
        SaleController saleController = new SaleController(saleService);

        // Act
        ResponseEntity<SaleDto> actualUpdateSaleResult = saleController.updateSale(1L, new SaleDto());

        // Assert
        verify(saleService).updateSale(eq(1L), isA(SaleDto.class));
        HttpStatusCode statusCode = actualUpdateSaleResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertNull(actualUpdateSaleResult.getBody());
        assertEquals(400, actualUpdateSaleResult.getStatusCodeValue());
        assertEquals(HttpStatus.BAD_REQUEST, statusCode);
        assertFalse(actualUpdateSaleResult.hasBody());
        assertTrue(actualUpdateSaleResult.getHeaders().isEmpty());
    }




    @Test
    void testDeleteSale() throws Exception {
        // Arrange
        when(saleService.deleteSale(Mockito.<Long>any())).thenReturn(new SaleDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/sale/delete/{id}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(saleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"date\":null,\"paymentType\":null,\"totalPrice\":0.0,\"totalDiscountAmount\":0.0,\"totalDiscountedPrice"
                                        + "\":0.0,\"cashierName\":null,\"soldProducts\":null}"));
    }


    @Test
    void testDeleteSale2() throws Exception {
        // Arrange
        when(saleService.deleteSale(Mockito.<Long>any())).thenReturn(null);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/sale/delete/{id}", 1L);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(saleController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test:
     * {@link SaleController#getAllSales(int, int, Double, Double, LocalDateTime, LocalDateTime, String, boolean, List, String)}
     */
    @Test
    void testGetAllSales() throws Exception {
        // Arrange
        when(saleService.getSalesFiltered(anyInt(), anyInt(), Mockito.<List<String>>any(), Mockito.<String>any(),
                anyDouble(), anyDouble(), Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(), Mockito.<String>any(),
                anyBoolean())).thenReturn(new PaginationResponse<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/sale/getAll");
        MockHttpServletRequestBuilder paramResult = getResult.param("deleted", String.valueOf(true));
        MockHttpServletRequestBuilder paramResult2 = paramResult.param("endDate",
                String.valueOf(LocalDate.of(1970, 1, 1).atStartOfDay()));
        MockHttpServletRequestBuilder paramResult3 = paramResult2.param("maxTotalPrice", String.valueOf(10.0d));
        MockHttpServletRequestBuilder paramResult4 = paramResult3.param("minTotalPrice", String.valueOf(10.0d));
        MockHttpServletRequestBuilder paramResult5 = paramResult4.param("page", String.valueOf(1))
                .param("paymentType", "foo");
        MockHttpServletRequestBuilder paramResult6 = paramResult5.param("size", String.valueOf(1))
                .param("sortOrder", "foo");
        MockHttpServletRequestBuilder requestBuilder = paramResult6.param("startDate",
                String.valueOf(LocalDate.of(1970, 1, 1).atStartOfDay()));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(saleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"content\":null,\"pageable\":null}"));
    }


    @Test
    void testGetCashierNameFromToken2() {
        // Arrange
        String mockName = "John Doe";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", mockName);

        // Create a JWT-like token with Base64-encoded payload
        String token = "header." + Base64.getUrlEncoder().encodeToString(jsonObject.toString().getBytes()) + ".signature";

        // Act
        String extractedName = saleController.getCashierNameFromToken(token);

        // Assert
        assertEquals(mockName, extractedName);
    }

    /**
     * Method under test: {@link SaleController#getCashierNameFromToken(String)}
     * Testing invalid token structure
     */
    @Test
    void testGetCashierNameFromToken_InvalidToken() {
        // Arrange
        String invalidToken = "invalidTokenWithoutParts";

        // Act & Assert
        Exception exception = assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            saleController.getCashierNameFromToken(invalidToken);
        });

        // Ensure an exception is thrown
        assertNotNull(exception);
    }

    @Test
    void testUpdateSale2() throws Exception {
        // Arrange
        SaleDto updatedSaleDto = new SaleDto();
        updatedSaleDto.setCashierName("Updated Cashier");
        updatedSaleDto.setDate(LocalDateTime.now());
        updatedSaleDto.setId(1L);
        updatedSaleDto.setPaymentType(PaymentType.CASH);
        updatedSaleDto.setSoldProducts(new ArrayList<>());
        updatedSaleDto.setTotalDiscountAmount(20.0);
        updatedSaleDto.setTotalDiscountedPrice(80.0);
        updatedSaleDto.setTotalPrice(100.0);

        when(saleService.updateSale(eq(1L), any(SaleDto.class))).thenReturn(updatedSaleDto);

        // ObjectMapper setup for LocalDateTime
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String content = objectMapper.writeValueAsString(updatedSaleDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/sale/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(saleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
}
