package com.toyota.saleservice.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class SaleServiceImplTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private SoldProductRepository soldProductRepository;

    @Mock
    private ProductServiceClient productServiceClient;

    @Mock
    private MapUtil mapUtil;

    @InjectMocks
    private SaleServiceImpl saleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddSaleSuccess() {
        // Arrange
        SaleDto saleDto = new SaleDto();
        saleDto.setSoldProducts(new ArrayList<>());

        Sale sale = new Sale();
        sale.setId(1L);
        sale.setTotalPrice(100.0);

        when(saleRepository.existsByIdAndDeletedIsFalse(anyLong())).thenReturn(false);
        when(mapUtil.convertSaleDtoToSale(any(SaleDto.class))).thenReturn(sale);
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);
        when(mapUtil.convertSaleToSaleDto(any(Sale.class))).thenReturn(saleDto);

        // Act
        SaleDto result = saleService.addSale(saleDto, "CashierName");

        // Assert
        assertSame(saleDto, result);
        verify(saleRepository).save(any(Sale.class));
        verify(mapUtil).convertSaleDtoToSale(saleDto);
        verify(mapUtil).convertSaleToSaleDto(sale);
    }

    @Test
    void testAddSaleAlreadyExistsException() {
        // Arrange
        SaleDto saleDto = new SaleDto();
        saleDto.setId(1L);

        when(saleRepository.existsByIdAndDeletedIsFalse(anyLong())).thenReturn(true);

        // Act and Assert
        assertThrows(SaleAlreadyExistsException.class, () -> saleService.addSale(saleDto, "CashierName"));
        verify(saleRepository).existsByIdAndDeletedIsFalse(1L);
    }

    @Test
    void testUpdateSaleSuccess() {
        // Arrange
        Sale existingSale = new Sale();
        existingSale.setId(1L);
        existingSale.setTotalPrice(100.0);
        existingSale.setSoldProducts(new ArrayList<>());

        SaleDto saleDto = new SaleDto();
        saleDto.setSoldProducts(new ArrayList<>());

        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(existingSale));
        when(saleRepository.save(any(Sale.class))).thenReturn(existingSale);
        when(mapUtil.convertSaleToSaleDto(any(Sale.class))).thenReturn(saleDto);

        // Act
        SaleDto result = saleService.updateSale(1L, saleDto);

        // Assert
        assertSame(saleDto, result);
        verify(saleRepository).save(existingSale);
    }

    @Test
    void testUpdateSaleNotFoundException() {
        // Arrange
        SaleDto saleDto = new SaleDto();
        when(saleRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(SaleNotFoundException.class, () -> saleService.updateSale(1L, saleDto));
    }

    @Test
    void testDeleteSaleSuccess() {
        // Arrange
        Sale existingSale = new Sale();
        existingSale.setId(1L);
        existingSale.setDeleted(false);

        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(existingSale));
        when(saleRepository.save(any(Sale.class))).thenReturn(existingSale);
        when(mapUtil.convertSaleToSaleDto(any(Sale.class))).thenReturn(new SaleDto());

        // Act
        SaleDto result = saleService.deleteSale(1L);

        // Assert
        assertNotNull(result);
        verify(saleRepository).save(existingSale);
    }

    @Test
    void testDeleteSaleNotFoundException() {
        // Arrange
        when(saleRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(SaleNotFoundException.class, () -> saleService.deleteSale(1L));
    }

    @Test
    void testGetSaleSuccess() {
        // Arrange
        Sale sale = new Sale();
        sale.setId(1L);
        sale.setTotalPrice(100.0);

        List<SoldProduct> soldProducts = new ArrayList<>();
        SoldProduct soldProduct = new SoldProduct();
        soldProduct.setId(1L);
        soldProducts.add(soldProduct);

        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));
        when(soldProductRepository.findAllBySaleId(anyLong())).thenReturn(soldProducts);
        when(mapUtil.convertSaleToSaleDto(any(Sale.class))).thenReturn(new SaleDto());
        when(mapUtil.convertSoldProductToSoldProductDto(any(SoldProduct.class))).thenReturn(new SoldProductDto());

        // Act
        SaleDto result = saleService.getSale(1L);

        // Assert
        assertNotNull(result);
        verify(saleRepository).findById(1L);
        verify(soldProductRepository).findAllBySaleId(1L);
    }

    @Test
    void testGetSaleNotFoundException() {
        // Arrange
        when(saleRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(SaleNotFoundException.class, () -> saleService.getSale(1L));
    }
}
