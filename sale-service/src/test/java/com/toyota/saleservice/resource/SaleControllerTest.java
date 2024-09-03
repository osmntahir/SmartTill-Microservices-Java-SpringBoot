package com.toyota.saleservice.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
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

    /**
     * Method under test: {@link SaleController#getSale(Long)}
     */
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
                        .string("{\"id\":null,\"date\":null,\"paymentType\":null,\"totalPrice\":0.0,\"soldProducts\":null}"));
    }

    /**
     * Method under test: {@link SaleController#getSale(Long)}
     */
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

    /**
     * Method under test: {@link SaleController#addSale(SaleDto)}
     */
    @Test
    void testAddSale() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        SaleService saleService = mock(SaleService.class);
        SaleDto saleDto = new SaleDto();
        when(saleService.addSale(Mockito.<SaleDto>any())).thenReturn(saleDto);
        SaleController saleController = new SaleController(saleService);

        // Act
        ResponseEntity<SaleDto> actualAddSaleResult = saleController.addSale(new SaleDto());

        // Assert
        verify(saleService).addSale(isA(SaleDto.class));
        HttpStatusCode statusCode = actualAddSaleResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals(201, actualAddSaleResult.getStatusCodeValue());
        assertEquals(HttpStatus.CREATED, statusCode);
        assertTrue(actualAddSaleResult.hasBody());
        assertTrue(actualAddSaleResult.getHeaders().isEmpty());
        assertSame(saleDto, actualAddSaleResult.getBody());
    }

    /**
     * Method under test: {@link SaleController#addSale(SaleDto)}
     */
    @Test
    void testAddSale2() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        SaleService saleService = mock(SaleService.class);
        when(saleService.addSale(Mockito.<SaleDto>any())).thenReturn(null);
        SaleController saleController = new SaleController(saleService);

        // Act
        ResponseEntity<SaleDto> actualAddSaleResult = saleController.addSale(new SaleDto());

        // Assert
        verify(saleService).addSale(isA(SaleDto.class));
        HttpStatusCode statusCode = actualAddSaleResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertNull(actualAddSaleResult.getBody());
        assertEquals(400, actualAddSaleResult.getStatusCodeValue());
        assertEquals(HttpStatus.BAD_REQUEST, statusCode);
        assertFalse(actualAddSaleResult.hasBody());
        assertTrue(actualAddSaleResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link SaleController#updateSale(Long, SaleDto)}
     */
    @Test
    void testUpdateSale() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        Sale sale = new Sale();
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalPrice(10.0d);
        Optional<Sale> ofResult = Optional.of(sale);

        Sale sale2 = new Sale();
        LocalDate ofResult2 = LocalDate.of(1970, 1, 1);
        sale2.setDate(ofResult2.atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
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
        assertNull(saleController.getSale(1L).getBody().getPaymentType());
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

    /**
     * Method under test: {@link SaleController#updateSale(Long, SaleDto)}
     */
    @Test
    void testUpdateSale2() {
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

    /**
     * Method under test: {@link SaleController#deleteSale(Long)}
     */
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
                        .string("{\"id\":null,\"date\":null,\"paymentType\":null,\"totalPrice\":0.0,\"soldProducts\":null}"));
    }

    /**
     * Method under test: {@link SaleController#deleteSale(Long)}
     */
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


}
