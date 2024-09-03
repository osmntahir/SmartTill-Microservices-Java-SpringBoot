package com.toyota.saleservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.toyota.saleservice.config.ProductServiceClient;
import com.toyota.saleservice.dao.SaleRepository;
import com.toyota.saleservice.dao.SoldProductRepository;
import com.toyota.saleservice.domain.PaymentType;
import com.toyota.saleservice.domain.Sale;
import com.toyota.saleservice.domain.SoldProduct;
import com.toyota.saleservice.dto.ProductDTO;
import com.toyota.saleservice.dto.SaleDto;
import com.toyota.saleservice.dto.SoldProductDto;
import com.toyota.saleservice.exception.ProductNotFoundException;
import com.toyota.saleservice.exception.SaleAlreadyExistsException;
import com.toyota.saleservice.exception.SaleNotFoundException;
import com.toyota.saleservice.service.common.MapUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SaleServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class SaleServiceImplTest {
    @MockBean
    private MapUtil mapUtil;

    @MockBean
    private ProductServiceClient productServiceClient;

    @MockBean
    private SaleRepository saleRepository;

    @Autowired
    private SaleServiceImpl saleServiceImpl;

    @MockBean
    private SoldProductRepository soldProductRepository;

    /**
     * Method under test:
     * {@link SaleServiceImpl#getSalesFiltered(int, int, List, String, double, double, LocalDateTime, LocalDateTime, String, boolean)}
     */
    @Test
    void testGetSalesFiltered() {
        // Arrange
        ArrayList<String> sortBy = new ArrayList<>();
        LocalDateTime startDate = LocalDate.of(1970, 1, 1).atStartOfDay();

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> saleServiceImpl.getSalesFiltered(1, 3, sortBy, "asc", 10.0d,
                10.0d, startDate, LocalDate.of(1970, 1, 1).atStartOfDay(), "Payment Type Str", true));
    }

    /**
     * Method under test:
     * {@link SaleServiceImpl#getSalesFiltered(int, int, List, String, double, double, LocalDateTime, LocalDateTime, String, boolean)}
     */
    @Test
    void testGetSalesFiltered2() {
        // Arrange
        ArrayList<String> sortBy = new ArrayList<>();
        sortBy.add("Getting sales with filters");
        LocalDateTime startDate = LocalDate.of(1970, 1, 1).atStartOfDay();

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> saleServiceImpl.getSalesFiltered(1, 3, sortBy, "asc", 10.0d,
                10.0d, startDate, LocalDate.of(1970, 1, 1).atStartOfDay(), "Payment Type Str", true));
    }

    /**
     * Method under test:
     * {@link SaleServiceImpl#getSalesFiltered(int, int, List, String, double, double, LocalDateTime, LocalDateTime, String, boolean)}
     */
    @Test
    void testGetSalesFiltered3() {
        // Arrange
        ArrayList<String> sortBy = new ArrayList<>();
        sortBy.add("Asc");
        sortBy.add("Getting sales with filters");
        LocalDateTime startDate = LocalDate.of(1970, 1, 1).atStartOfDay();

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> saleServiceImpl.getSalesFiltered(1, 3, sortBy, "asc", 10.0d,
                10.0d, startDate, LocalDate.of(1970, 1, 1).atStartOfDay(), "Payment Type Str", true));
    }

    /**
     * Method under test:
     * {@link SaleServiceImpl#getSalesFiltered(int, int, List, String, double, double, LocalDateTime, LocalDateTime, String, boolean)}
     */
    @Test
    void testGetSalesFiltered4() {
        // Arrange
        Sale sale = new Sale();
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalPrice(10.0d);

        ArrayList<Sale> content = new ArrayList<>();
        content.add(sale);
        PageImpl<Sale> pageImpl = new PageImpl<>(content);
        when(saleRepository.getSalesFiltered(anyDouble(), anyDouble(), Mockito.<LocalDateTime>any(),
                Mockito.<LocalDateTime>any(), Mockito.<PaymentType>any(), anyBoolean(), Mockito.<Pageable>any()))
                .thenReturn(pageImpl);
        when(mapUtil.convertSaleToSaleDto(Mockito.<Sale>any())).thenThrow(new IllegalArgumentException("foo"));
        ArrayList<String> sortBy = new ArrayList<>();
        LocalDateTime startDate = LocalDate.of(1970, 1, 1).atStartOfDay();

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> saleServiceImpl.getSalesFiltered(1, 3, sortBy, "asc", 10.0d,
                10.0d, startDate, LocalDate.of(1970, 1, 1).atStartOfDay(), null, true));
        verify(saleRepository).getSalesFiltered(eq(10.0d), eq(10.0d), isA(LocalDateTime.class), isA(LocalDateTime.class),
                isNull(), eq(true), isA(Pageable.class));
        verify(mapUtil).convertSaleToSaleDto(isA(Sale.class));
    }

    /**
     * Method under test:
     * {@link SaleServiceImpl#getSalesFiltered(int, int, List, String, double, double, LocalDateTime, LocalDateTime, String, boolean)}
     */
    @Test
    void testGetSalesFiltered5() {
        // Arrange
        Sale sale = new Sale();
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalPrice(10.0d);

        ArrayList<Sale> content = new ArrayList<>();
        content.add(sale);
        PageImpl<Sale> pageImpl = new PageImpl<>(content);
        when(saleRepository.getSalesFiltered(anyDouble(), anyDouble(), Mockito.<LocalDateTime>any(),
                Mockito.<LocalDateTime>any(), Mockito.<PaymentType>any(), anyBoolean(), Mockito.<Pageable>any()))
                .thenReturn(pageImpl);
        when(mapUtil.convertSaleToSaleDto(Mockito.<Sale>any())).thenThrow(new IllegalArgumentException("foo"));
        ArrayList<String> sortBy = new ArrayList<>();
        LocalDateTime startDate = LocalDate.of(1970, 1, 1).atStartOfDay();

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> saleServiceImpl.getSalesFiltered(1, 3, sortBy, "asc", 10.0d,
                10.0d, startDate, LocalDate.of(1970, 1, 1).atStartOfDay(), "", true));
        verify(saleRepository).getSalesFiltered(eq(10.0d), eq(10.0d), isA(LocalDateTime.class), isA(LocalDateTime.class),
                isNull(), eq(true), isA(Pageable.class));
        verify(mapUtil).convertSaleToSaleDto(isA(Sale.class));
    }

    /**
     * Method under test: {@link SaleServiceImpl#addSale(SaleDto)}
     */
    @Test
    void testAddSale() {
        // Arrange
        when(saleRepository.existsByIdAndDeletedIsFalse(Mockito.<Long>any())).thenReturn(true);

        // Act and Assert
        assertThrows(SaleAlreadyExistsException.class, () -> saleServiceImpl.addSale(new SaleDto()));
        verify(saleRepository).existsByIdAndDeletedIsFalse(isNull());
    }

    /**
     * Method under test: {@link SaleServiceImpl#addSale(SaleDto)}
     */
    @Test
    void testAddSale2() {
        // Arrange
        when(saleRepository.existsByIdAndDeletedIsFalse(Mockito.<Long>any()))
                .thenThrow(new IllegalArgumentException("Adding sale with id {}"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> saleServiceImpl.addSale(new SaleDto()));
        verify(saleRepository).existsByIdAndDeletedIsFalse(isNull());
    }

    /**
     * Method under test: {@link SaleServiceImpl#addSale(SaleDto)}
     */
    @Test
    void testAddSale3() {
        // Arrange
        Sale sale = new Sale();
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalPrice(10.0d);
        when(saleRepository.save(Mockito.<Sale>any())).thenReturn(sale);
        when(saleRepository.existsByIdAndDeletedIsFalse(Mockito.<Long>any())).thenReturn(false);

        Sale sale2 = new Sale();
        sale2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
        sale2.setTotalPrice(10.0d);
        when(mapUtil.convertSaleDtoToSale(Mockito.<SaleDto>any())).thenReturn(sale2);
        SaleDto saleDto = new SaleDto();
        when(mapUtil.convertSaleToSaleDto(Mockito.<Sale>any())).thenReturn(saleDto);

        ArrayList<SoldProductDto> soldProducts = new ArrayList<>();
        soldProducts.add(new SoldProductDto());

        SaleDto saleDto2 = new SaleDto();
        saleDto2.setSoldProducts(soldProducts);

        // Act
        SaleDto actualAddSaleResult = saleServiceImpl.addSale(saleDto2);

        // Assert
        verify(saleRepository).existsByIdAndDeletedIsFalse(isNull());
        verify(mapUtil).convertSaleDtoToSale(isA(SaleDto.class));
        verify(mapUtil).convertSaleToSaleDto(isA(Sale.class));
        verify(saleRepository).save(isA(Sale.class));
        assertSame(saleDto, actualAddSaleResult);
    }

    /**
     * Method under test: {@link SaleServiceImpl#addSale(SaleDto)}
     */
    @Test
    void testAddSale4() {
        // Arrange
        when(saleRepository.existsByIdAndDeletedIsFalse(Mockito.<Long>any())).thenReturn(false);
        when(mapUtil.convertSaleDtoToSale(Mockito.<SaleDto>any()))
                .thenThrow(new ProductNotFoundException("An error occurred"));

        ArrayList<SoldProductDto> soldProducts = new ArrayList<>();
        soldProducts.add(new SoldProductDto());

        SaleDto saleDto = new SaleDto();
        saleDto.setSoldProducts(soldProducts);

        // Act and Assert
        assertThrows(ProductNotFoundException.class, () -> saleServiceImpl.addSale(saleDto));
        verify(saleRepository).existsByIdAndDeletedIsFalse(isNull());
        verify(mapUtil).convertSaleDtoToSale(isA(SaleDto.class));
    }

    /**
     * Method under test: {@link SaleServiceImpl#addSale(SaleDto)}
     */
    @Test
    void testAddSale5() {
        // Arrange
        when(saleRepository.existsByIdAndDeletedIsFalse(Mockito.<Long>any())).thenReturn(false);
        SoldProductDto soldProductDto = mock(SoldProductDto.class);
        when(soldProductDto.getPrice()).thenThrow(new ProductNotFoundException("An error occurred"));

        ArrayList<SoldProductDto> soldProducts = new ArrayList<>();
        soldProducts.add(soldProductDto);

        SaleDto saleDto = new SaleDto();
        saleDto.setSoldProducts(soldProducts);

        // Act and Assert
        assertThrows(ProductNotFoundException.class, () -> saleServiceImpl.addSale(saleDto));
        verify(saleRepository).existsByIdAndDeletedIsFalse(isNull());
        verify(soldProductDto).getPrice();
    }

    /**
     * Method under test: {@link SaleServiceImpl#updateSale(Long, SaleDto)}
     */
    @Test
    void testUpdateSale() {
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
        sale2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
        sale2.setTotalPrice(10.0d);
        when(saleRepository.save(Mockito.<Sale>any())).thenReturn(sale2);
        when(saleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        SaleDto saleDto = new SaleDto();
        when(mapUtil.convertSaleToSaleDto(Mockito.<Sale>any())).thenReturn(saleDto);

        // Act
        SaleDto actualUpdateSaleResult = saleServiceImpl.updateSale(1L, new SaleDto());

        // Assert
        verify(mapUtil).convertSaleToSaleDto(isA(Sale.class));
        verify(saleRepository).findById(eq(1L));
        verify(saleRepository).save(isA(Sale.class));
        assertSame(saleDto, actualUpdateSaleResult);
    }

    /**
     * Method under test: {@link SaleServiceImpl#updateSale(Long, SaleDto)}
     */
    @Test
    void testUpdateSale2() {
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
        sale2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
        sale2.setTotalPrice(10.0d);
        when(saleRepository.save(Mockito.<Sale>any())).thenReturn(sale2);
        when(saleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(mapUtil.convertSaleToSaleDto(Mockito.<Sale>any()))
                .thenThrow(new IllegalArgumentException("Updating sale with id: {}"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> saleServiceImpl.updateSale(1L, new SaleDto()));
        verify(mapUtil).convertSaleToSaleDto(isA(Sale.class));
        verify(saleRepository).findById(eq(1L));
        verify(saleRepository).save(isA(Sale.class));
    }

    /**
     * Method under test: {@link SaleServiceImpl#updateSale(Long, SaleDto)}
     */
    @Test
    void testUpdateSale3() {
        // Arrange
        Optional<Sale> emptyResult = Optional.empty();
        when(saleRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(SaleNotFoundException.class, () -> saleServiceImpl.updateSale(1L, new SaleDto()));
        verify(saleRepository).findById(eq(1L));
    }

    /**
     * Method under test: {@link SaleServiceImpl#updateSale(Long, SaleDto)}
     */
    @Test
    void testUpdateSale4() {
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
        sale2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
        sale2.setTotalPrice(10.0d);
        when(saleRepository.save(Mockito.<Sale>any())).thenReturn(sale2);
        when(saleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(soldProductRepository).deleteAll(Mockito.<Iterable<SoldProduct>>any());
        SaleDto saleDto = new SaleDto();
        when(mapUtil.convertSaleToSaleDto(Mockito.<Sale>any())).thenReturn(saleDto);
        LocalDateTime date = LocalDate.of(1970, 1, 1).atStartOfDay();

        // Act
        SaleDto actualUpdateSaleResult = saleServiceImpl.updateSale(1L,
                new SaleDto(1L, date, PaymentType.CASH, 10.0d, new ArrayList<>()));

        // Assert
        verify(mapUtil).convertSaleToSaleDto(isA(Sale.class));
        verify(soldProductRepository).deleteAll(isA(Iterable.class));
        verify(saleRepository).findById(eq(1L));
        verify(saleRepository).save(isA(Sale.class));
        assertSame(saleDto, actualUpdateSaleResult);
    }

    /**
     * Method under test: {@link SaleServiceImpl#updateSale(Long, SaleDto)}
     */
    @Test
    void testUpdateSale5() {
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
        sale2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
        sale2.setTotalPrice(10.0d);
        when(saleRepository.save(Mockito.<Sale>any())).thenReturn(sale2);
        when(saleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(soldProductRepository).deleteAll(Mockito.<Iterable<SoldProduct>>any());

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setInventory(1);
        productDTO.setName("Name");
        productDTO.setPrice(10.0d);
        Optional<ProductDTO> ofResult2 = Optional.of(productDTO);
        when(productServiceClient.getProductById(Mockito.<Long>any())).thenReturn(ofResult2);

        Sale sale3 = new Sale();
        sale3.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale3.setDeleted(true);
        sale3.setId(1L);
        sale3.setPaymentType(PaymentType.CASH);
        sale3.setSoldProducts(new ArrayList<>());
        sale3.setTotalPrice(10.0d);

        SoldProduct soldProduct = new SoldProduct();
        soldProduct.setDeleted(true);
        soldProduct.setDiscount(3L);
        soldProduct.setId(1L);
        soldProduct.setName("Name");
        soldProduct.setPrice(10.0d);
        soldProduct.setProductId(1L);
        soldProduct.setQuantity(1);
        soldProduct.setSale(sale3);
        soldProduct.setTotal(10.0d);
        when(mapUtil.convertSoldProductDtoToSoldProduct(Mockito.<SoldProductDto>any())).thenReturn(soldProduct);
        SaleDto saleDto = new SaleDto();
        when(mapUtil.convertSaleToSaleDto(Mockito.<Sale>any())).thenReturn(saleDto);

        ArrayList<SoldProductDto> soldProducts = new ArrayList<>();
        soldProducts.add(new SoldProductDto());

        // Act
        SaleDto actualUpdateSaleResult = saleServiceImpl.updateSale(1L,
                new SaleDto(1L, LocalDate.of(1970, 1, 1).atStartOfDay(), PaymentType.CASH, 10.0d, soldProducts));

        // Assert
        verify(productServiceClient).getProductById(isNull());
        verify(mapUtil).convertSaleToSaleDto(isA(Sale.class));
        verify(mapUtil).convertSoldProductDtoToSoldProduct(isA(SoldProductDto.class));
        verify(soldProductRepository).deleteAll(isA(Iterable.class));
        verify(saleRepository).findById(eq(1L));
        verify(saleRepository).save(isA(Sale.class));
        assertSame(saleDto, actualUpdateSaleResult);
    }

    /**
     * Method under test: {@link SaleServiceImpl#updateSale(Long, SaleDto)}
     */
    @Test
    void testUpdateSale6() {
        // Arrange
        Sale sale = new Sale();
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalPrice(10.0d);
        Optional<Sale> ofResult = Optional.of(sale);
        when(saleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(soldProductRepository).deleteAll(Mockito.<Iterable<SoldProduct>>any());

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setInventory(1);
        productDTO.setName("Name");
        productDTO.setPrice(10.0d);
        Optional<ProductDTO> ofResult2 = Optional.of(productDTO);
        when(productServiceClient.getProductById(Mockito.<Long>any())).thenReturn(ofResult2);
        when(mapUtil.convertSoldProductDtoToSoldProduct(Mockito.<SoldProductDto>any()))
                .thenThrow(new IllegalArgumentException("Updating sale with id: {}"));

        ArrayList<SoldProductDto> soldProducts = new ArrayList<>();
        soldProducts.add(new SoldProductDto());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> saleServiceImpl.updateSale(1L,
                new SaleDto(1L, LocalDate.of(1970, 1, 1).atStartOfDay(), PaymentType.CASH, 10.0d, soldProducts)));
        verify(productServiceClient).getProductById(isNull());
        verify(mapUtil).convertSoldProductDtoToSoldProduct(isA(SoldProductDto.class));
        verify(soldProductRepository).deleteAll(isA(Iterable.class));
        verify(saleRepository).findById(eq(1L));
    }

    /**
     * Method under test: {@link SaleServiceImpl#deleteSale(Long)}
     */
    @Test
    void testDeleteSale() {
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
        sale2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
        sale2.setTotalPrice(10.0d);
        when(saleRepository.save(Mockito.<Sale>any())).thenReturn(sale2);
        when(saleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        SaleDto saleDto = new SaleDto();
        when(mapUtil.convertSaleToSaleDto(Mockito.<Sale>any())).thenReturn(saleDto);

        // Act
        SaleDto actualDeleteSaleResult = saleServiceImpl.deleteSale(1L);

        // Assert
        verify(mapUtil).convertSaleToSaleDto(isA(Sale.class));
        verify(saleRepository).findById(eq(1L));
        verify(saleRepository).save(isA(Sale.class));
        assertSame(saleDto, actualDeleteSaleResult);
    }

    /**
     * Method under test: {@link SaleServiceImpl#deleteSale(Long)}
     */
    @Test
    void testDeleteSale2() {
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
        sale2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
        sale2.setTotalPrice(10.0d);
        when(saleRepository.save(Mockito.<Sale>any())).thenReturn(sale2);
        when(saleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(mapUtil.convertSaleToSaleDto(Mockito.<Sale>any()))
                .thenThrow(new IllegalArgumentException("Deleting sale with id: {}"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> saleServiceImpl.deleteSale(1L));
        verify(mapUtil).convertSaleToSaleDto(isA(Sale.class));
        verify(saleRepository).findById(eq(1L));
        verify(saleRepository).save(isA(Sale.class));
    }

    /**
     * Method under test: {@link SaleServiceImpl#deleteSale(Long)}
     */
    @Test
    void testDeleteSale3() {
        // Arrange
        Optional<Sale> emptyResult = Optional.empty();
        when(saleRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(SaleNotFoundException.class, () -> saleServiceImpl.deleteSale(1L));
        verify(saleRepository).findById(eq(1L));
    }

    /**
     * Method under test: {@link SaleServiceImpl#getSale(Long)}
     */
    @Test
    void testGetSale() {
        // Arrange
        Sale sale = new Sale();
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalPrice(10.0d);
        Optional<Sale> ofResult = Optional.of(sale);
        when(saleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(soldProductRepository.findAllBySaleId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        SaleDto saleDto = new SaleDto();
        when(mapUtil.convertSaleToSaleDto(Mockito.<Sale>any())).thenReturn(saleDto);

        // Act
        SaleDto actualSale = saleServiceImpl.getSale(1L);

        // Assert
        verify(soldProductRepository).findAllBySaleId(eq(1L));
        verify(mapUtil).convertSaleToSaleDto(isA(Sale.class));
        verify(saleRepository).findById(eq(1L));
        assertSame(saleDto, actualSale);
    }

    /**
     * Method under test: {@link SaleServiceImpl#getSale(Long)}
     */
    @Test
    void testGetSale2() {
        // Arrange
        Sale sale = new Sale();
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalPrice(10.0d);
        Optional<Sale> ofResult = Optional.of(sale);
        when(saleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(soldProductRepository.findAllBySaleId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(mapUtil.convertSaleToSaleDto(Mockito.<Sale>any()))
                .thenThrow(new IllegalArgumentException("Getting sale with id: {}"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> saleServiceImpl.getSale(1L));
        verify(soldProductRepository).findAllBySaleId(eq(1L));
        verify(mapUtil).convertSaleToSaleDto(isA(Sale.class));
        verify(saleRepository).findById(eq(1L));
    }

    /**
     * Method under test: {@link SaleServiceImpl#getSale(Long)}
     */
    @Test
    void testGetSale3() {
        // Arrange
        Optional<Sale> emptyResult = Optional.empty();
        when(saleRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(SaleNotFoundException.class, () -> saleServiceImpl.getSale(1L));
        verify(saleRepository).findById(eq(1L));
    }

    /**
     * Method under test: {@link SaleServiceImpl#getSale(Long)}
     */
    @Test
    void testGetSale4() {
        // Arrange
        Sale sale = new Sale();
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalPrice(10.0d);
        Optional<Sale> ofResult = Optional.of(sale);
        when(saleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Sale sale2 = new Sale();
        sale2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
        sale2.setTotalPrice(10.0d);

        SoldProduct soldProduct = new SoldProduct();
        soldProduct.setDeleted(true);
        soldProduct.setDiscount(3L);
        soldProduct.setId(1L);
        soldProduct.setName("Getting sale with id: {}");
        soldProduct.setPrice(10.0d);
        soldProduct.setProductId(1L);
        soldProduct.setQuantity(1);
        soldProduct.setSale(sale2);
        soldProduct.setTotal(10.0d);

        ArrayList<SoldProduct> soldProductList = new ArrayList<>();
        soldProductList.add(soldProduct);
        when(soldProductRepository.findAllBySaleId(Mockito.<Long>any())).thenReturn(soldProductList);
        when(mapUtil.convertSoldProductToSoldProductDto(Mockito.<SoldProduct>any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> saleServiceImpl.getSale(1L));
        verify(soldProductRepository).findAllBySaleId(eq(1L));
        verify(mapUtil).convertSoldProductToSoldProductDto(isA(SoldProduct.class));
        verify(saleRepository).findById(eq(1L));
    }
}
