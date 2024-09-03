package com.toyota.saleservice.service.impl;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.toyota.saleservice.config.ProductServiceClient;
import com.toyota.saleservice.dao.SaleRepository;
import com.toyota.saleservice.dao.SoldProductRepository;
import com.toyota.saleservice.domain.PaymentType;
import com.toyota.saleservice.domain.Sale;
import com.toyota.saleservice.domain.SoldProduct;
import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.dto.ProductDTO;
import com.toyota.saleservice.dto.SoldProductDto;
import com.toyota.saleservice.exception.ProductNotFoundException;
import com.toyota.saleservice.exception.ProductQuantityShortageException;
import com.toyota.saleservice.exception.SaleNotFoundException;
import com.toyota.saleservice.service.common.MapUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SoldProductServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class SoldProductServiceImplTest {
    @MockBean
    private CampaignProductServiceImpl campaignProductServiceImpl;

    @MockBean
    private MapUtil mapUtil;

    @MockBean
    private ProductServiceClient productServiceClient;

    @MockBean
    private SaleRepository saleRepository;

    @MockBean
    private SoldProductRepository soldProductRepository;

    @Autowired
    private SoldProductServiceImpl soldProductServiceImpl;

    /**
     * Method under test:
     * {@link SoldProductServiceImpl#updateSoldProduct(Long, SoldProductDto)}
     */
    @Test
    void testUpdateSoldProduct() {
        // Arrange
        Sale sale = new Sale();
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalPrice(10.0d);

        SoldProduct soldProduct = new SoldProduct();
        soldProduct.setDeleted(true);
        soldProduct.setDiscount(3L);
        soldProduct.setId(1L);
        soldProduct.setName("Name");
        soldProduct.setPrice(10.0d);
        soldProduct.setProductId(1L);
        soldProduct.setQuantity(1);
        soldProduct.setSale(sale);
        soldProduct.setTotal(10.0d);
        Optional<SoldProduct> ofResult = Optional.of(soldProduct);

        Sale sale2 = new Sale();
        sale2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
        sale2.setTotalPrice(10.0d);

        SoldProduct soldProduct2 = new SoldProduct();
        soldProduct2.setDeleted(true);
        soldProduct2.setDiscount(3L);
        soldProduct2.setId(1L);
        soldProduct2.setName("Name");
        soldProduct2.setPrice(10.0d);
        soldProduct2.setProductId(1L);
        soldProduct2.setQuantity(1);
        soldProduct2.setSale(sale2);
        soldProduct2.setTotal(10.0d);
        when(soldProductRepository.save(Mockito.<SoldProduct>any())).thenReturn(soldProduct2);
        when(soldProductRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        SoldProductDto soldProductDto = new SoldProductDto();
        when(mapUtil.convertSoldProductToSoldProductDto(Mockito.<SoldProduct>any())).thenReturn(soldProductDto);
        Optional<Long> ofResult2 = Optional.<Long>of(3L);
        when(campaignProductServiceImpl.getDiscountForProduct(Mockito.<Long>any())).thenReturn(ofResult2);

        // Act
        SoldProductDto actualUpdateSoldProductResult = soldProductServiceImpl.updateSoldProduct(1L, new SoldProductDto());

        // Assert
        verify(mapUtil).convertSoldProductToSoldProductDto(isA(SoldProduct.class));
        verify(campaignProductServiceImpl).getDiscountForProduct(eq(1L));
        verify(soldProductRepository).findById(eq(1L));
        verify(soldProductRepository).save(isA(SoldProduct.class));
        assertSame(soldProductDto, actualUpdateSoldProductResult);
    }

    /**
     * Method under test:
     * {@link SoldProductServiceImpl#updateSoldProduct(Long, SoldProductDto)}
     */
    @Test
    void testUpdateSoldProduct2() {
        // Arrange
        Sale sale = new Sale();
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalPrice(10.0d);

        SoldProduct soldProduct = new SoldProduct();
        soldProduct.setDeleted(true);
        soldProduct.setDiscount(3L);
        soldProduct.setId(1L);
        soldProduct.setName("Name");
        soldProduct.setPrice(10.0d);
        soldProduct.setProductId(1L);
        soldProduct.setQuantity(1);
        soldProduct.setSale(sale);
        soldProduct.setTotal(10.0d);
        Optional<SoldProduct> ofResult = Optional.of(soldProduct);
        when(soldProductRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(campaignProductServiceImpl.getDiscountForProduct(Mockito.<Long>any()))
                .thenThrow(new ProductNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ProductNotFoundException.class,
                () -> soldProductServiceImpl.updateSoldProduct(1L, new SoldProductDto()));
        verify(campaignProductServiceImpl).getDiscountForProduct(eq(1L));
        verify(soldProductRepository).findById(eq(1L));
    }

    /**
     * Method under test:
     * {@link SoldProductServiceImpl#updateSoldProduct(Long, SoldProductDto)}
     */
    @Test
    void testUpdateSoldProduct3() {
        // Arrange
        Sale sale = new Sale();
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalPrice(10.0d);
        SoldProduct soldProduct = mock(SoldProduct.class);
        when(soldProduct.getPrice()).thenReturn(10.0d);
        when(soldProduct.getTotal()).thenReturn(10.0d);
        when(soldProduct.getQuantity()).thenReturn(1);
        when(soldProduct.getProductId()).thenReturn(1L);
        doNothing().when(soldProduct).setDeleted(anyBoolean());
        doNothing().when(soldProduct).setDiscount(anyLong());
        doNothing().when(soldProduct).setId(Mockito.<Long>any());
        doNothing().when(soldProduct).setName(Mockito.<String>any());
        doNothing().when(soldProduct).setPrice(anyDouble());
        doNothing().when(soldProduct).setProductId(Mockito.<Long>any());
        doNothing().when(soldProduct).setQuantity(anyInt());
        doNothing().when(soldProduct).setSale(Mockito.<Sale>any());
        doNothing().when(soldProduct).setTotal(anyDouble());
        soldProduct.setDeleted(true);
        soldProduct.setDiscount(3L);
        soldProduct.setId(1L);
        soldProduct.setName("Name");
        soldProduct.setPrice(10.0d);
        soldProduct.setProductId(1L);
        soldProduct.setQuantity(1);
        soldProduct.setSale(sale);
        soldProduct.setTotal(10.0d);
        Optional<SoldProduct> ofResult = Optional.of(soldProduct);

        Sale sale2 = new Sale();
        sale2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
        sale2.setTotalPrice(10.0d);

        SoldProduct soldProduct2 = new SoldProduct();
        soldProduct2.setDeleted(true);
        soldProduct2.setDiscount(3L);
        soldProduct2.setId(1L);
        soldProduct2.setName("Name");
        soldProduct2.setPrice(10.0d);
        soldProduct2.setProductId(1L);
        soldProduct2.setQuantity(1);
        soldProduct2.setSale(sale2);
        soldProduct2.setTotal(10.0d);
        when(soldProductRepository.save(Mockito.<SoldProduct>any())).thenReturn(soldProduct2);
        when(soldProductRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        SoldProductDto soldProductDto = new SoldProductDto();
        when(mapUtil.convertSoldProductToSoldProductDto(Mockito.<SoldProduct>any())).thenReturn(soldProductDto);
        Optional<Long> ofResult2 = Optional.<Long>of(3L);
        when(campaignProductServiceImpl.getDiscountForProduct(Mockito.<Long>any())).thenReturn(ofResult2);

        // Act
        SoldProductDto actualUpdateSoldProductResult = soldProductServiceImpl.updateSoldProduct(1L, new SoldProductDto());

        // Assert
        verify(soldProduct).getPrice();
        verify(soldProduct).getProductId();
        verify(soldProduct).getQuantity();
        verify(soldProduct, atLeast(1)).getTotal();
        verify(soldProduct).setDeleted(eq(true));
        verify(soldProduct, atLeast(1)).setDiscount(eq(3L));
        verify(soldProduct).setId(eq(1L));
        verify(soldProduct).setName(eq("Name"));
        verify(soldProduct, atLeast(1)).setPrice(eq(10.0d));
        verify(soldProduct).setProductId(eq(1L));
        verify(soldProduct, atLeast(1)).setQuantity(anyInt());
        verify(soldProduct).setSale(isA(Sale.class));
        verify(soldProduct, atLeast(1)).setTotal(anyDouble());
        verify(mapUtil).convertSoldProductToSoldProductDto(isA(SoldProduct.class));
        verify(campaignProductServiceImpl).getDiscountForProduct(eq(1L));
        verify(soldProductRepository).findById(eq(1L));
        verify(soldProductRepository).save(isA(SoldProduct.class));
        assertSame(soldProductDto, actualUpdateSoldProductResult);
    }

    /**
     * Method under test:
     * {@link SoldProductServiceImpl#getSoldProducts(int, int, String, Double, Double, boolean, String, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetSoldProducts() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IllegalArgumentException: Invalid value 'Sort Direction' for orders given; Has to be either 'desc' or 'asc' (case insensitive)
        //       at com.toyota.saleservice.service.impl.SoldProductServiceImpl.getSoldProducts(SoldProductServiceImpl.java:62)
        //   java.lang.IllegalArgumentException: No enum constant org.springframework.data.domain.Sort.Direction.SORT DIRECTION
        //       at java.base/java.lang.Enum.valueOf(Enum.java:273)
        //       at com.toyota.saleservice.service.impl.SoldProductServiceImpl.getSoldProducts(SoldProductServiceImpl.java:62)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange and Act
        soldProductServiceImpl.getSoldProducts(1, 3, "Name", 10.0d, 10.0d, true, "Sort By", "Sort Direction");
    }

    /**
     * Method under test:
     * {@link SoldProductServiceImpl#addSoldProduct(Long, Long, SoldProductDto)}
     */
    @Test
    void testAddSoldProduct() {
        // Arrange
        Sale sale = new Sale();
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalPrice(10.0d);

        SoldProduct soldProduct = new SoldProduct();
        soldProduct.setDeleted(true);
        soldProduct.setDiscount(3L);
        soldProduct.setId(1L);
        soldProduct.setName("Name");
        soldProduct.setPrice(10.0d);
        soldProduct.setProductId(1L);
        soldProduct.setQuantity(1);
        soldProduct.setSale(sale);
        soldProduct.setTotal(10.0d);

        Sale sale2 = new Sale();
        sale2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
        sale2.setTotalPrice(10.0d);

        SoldProduct soldProduct2 = new SoldProduct();
        soldProduct2.setDeleted(true);
        soldProduct2.setDiscount(3L);
        soldProduct2.setId(1L);
        soldProduct2.setName("Name");
        soldProduct2.setPrice(10.0d);
        soldProduct2.setProductId(1L);
        soldProduct2.setQuantity(1);
        soldProduct2.setSale(sale2);
        soldProduct2.setTotal(10.0d);
        Optional<SoldProduct> ofResult = Optional.of(soldProduct2);
        when(soldProductRepository.save(Mockito.<SoldProduct>any())).thenReturn(soldProduct);
        when(soldProductRepository.findBySaleIdAndProductId(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(ofResult);
        SoldProductDto soldProductDto = new SoldProductDto();
        when(mapUtil.convertSoldProductToSoldProductDto(Mockito.<SoldProduct>any())).thenReturn(soldProductDto);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setInventory(1);
        productDTO.setName("Name");
        productDTO.setPrice(10.0d);
        Optional<ProductDTO> ofResult2 = Optional.of(productDTO);
        doNothing().when(productServiceClient).updateProductInventory(Mockito.<ProductDTO>any());
        when(productServiceClient.getProductById(Mockito.<Long>any())).thenReturn(ofResult2);
        Optional<Long> ofResult3 = Optional.<Long>of(3L);
        when(campaignProductServiceImpl.getDiscountForProduct(Mockito.<Long>any())).thenReturn(ofResult3);

        Sale sale3 = new Sale();
        sale3.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale3.setDeleted(true);
        sale3.setId(1L);
        sale3.setPaymentType(PaymentType.CASH);
        sale3.setSoldProducts(new ArrayList<>());
        sale3.setTotalPrice(10.0d);
        Optional<Sale> ofResult4 = Optional.of(sale3);

        Sale sale4 = new Sale();
        sale4.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale4.setDeleted(true);
        sale4.setId(1L);
        sale4.setPaymentType(PaymentType.CASH);
        sale4.setSoldProducts(new ArrayList<>());
        sale4.setTotalPrice(10.0d);
        when(saleRepository.save(Mockito.<Sale>any())).thenReturn(sale4);
        when(saleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult4);

        // Act
        SoldProductDto actualAddSoldProductResult = soldProductServiceImpl.addSoldProduct(1L, 1L, new SoldProductDto());

        // Assert
        verify(productServiceClient).getProductById(eq(1L));
        verify(productServiceClient).updateProductInventory(isA(ProductDTO.class));
        verify(soldProductRepository, atLeast(1)).findBySaleIdAndProductId(eq(1L), eq(1L));
        verify(mapUtil).convertSoldProductToSoldProductDto(isA(SoldProduct.class));
        verify(campaignProductServiceImpl).getDiscountForProduct(eq(1L));
        verify(saleRepository).findById(eq(1L));
        verify(saleRepository).save(isA(Sale.class));
        verify(soldProductRepository).save(isA(SoldProduct.class));
        assertSame(soldProductDto, actualAddSoldProductResult);
    }

    /**
     * Method under test:
     * {@link SoldProductServiceImpl#addSoldProduct(Long, Long, SoldProductDto)}
     */
    @Test
    void testAddSoldProduct2() {
        // Arrange
        Sale sale = new Sale();
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalPrice(10.0d);

        SoldProduct soldProduct = new SoldProduct();
        soldProduct.setDeleted(true);
        soldProduct.setDiscount(3L);
        soldProduct.setId(1L);
        soldProduct.setName("Name");
        soldProduct.setPrice(10.0d);
        soldProduct.setProductId(1L);
        soldProduct.setQuantity(1);
        soldProduct.setSale(sale);
        soldProduct.setTotal(10.0d);

        Sale sale2 = new Sale();
        sale2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
        sale2.setTotalPrice(10.0d);

        SoldProduct soldProduct2 = new SoldProduct();
        soldProduct2.setDeleted(true);
        soldProduct2.setDiscount(3L);
        soldProduct2.setId(1L);
        soldProduct2.setName("Name");
        soldProduct2.setPrice(10.0d);
        soldProduct2.setProductId(1L);
        soldProduct2.setQuantity(1);
        soldProduct2.setSale(sale2);
        soldProduct2.setTotal(10.0d);
        Optional<SoldProduct> ofResult = Optional.of(soldProduct2);
        when(soldProductRepository.save(Mockito.<SoldProduct>any())).thenReturn(soldProduct);
        when(soldProductRepository.findBySaleIdAndProductId(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(ofResult);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setInventory(1);
        productDTO.setName("Name");
        productDTO.setPrice(10.0d);
        Optional<ProductDTO> ofResult2 = Optional.of(productDTO);
        doNothing().when(productServiceClient).updateProductInventory(Mockito.<ProductDTO>any());
        when(productServiceClient.getProductById(Mockito.<Long>any())).thenReturn(ofResult2);
        Optional<Long> ofResult3 = Optional.<Long>of(3L);
        when(campaignProductServiceImpl.getDiscountForProduct(Mockito.<Long>any())).thenReturn(ofResult3);

        Sale sale3 = new Sale();
        sale3.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale3.setDeleted(true);
        sale3.setId(1L);
        sale3.setPaymentType(PaymentType.CASH);
        sale3.setSoldProducts(new ArrayList<>());
        sale3.setTotalPrice(10.0d);
        Optional<Sale> ofResult4 = Optional.of(sale3);
        when(saleRepository.save(Mockito.<Sale>any())).thenThrow(new ProductNotFoundException("An error occurred"));
        when(saleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult4);

        // Act and Assert
        assertThrows(ProductNotFoundException.class,
                () -> soldProductServiceImpl.addSoldProduct(1L, 1L, new SoldProductDto()));
        verify(productServiceClient).getProductById(eq(1L));
        verify(productServiceClient).updateProductInventory(isA(ProductDTO.class));
        verify(soldProductRepository).findBySaleIdAndProductId(eq(1L), eq(1L));
        verify(campaignProductServiceImpl).getDiscountForProduct(eq(1L));
        verify(saleRepository).findById(eq(1L));
        verify(saleRepository).save(isA(Sale.class));
        verify(soldProductRepository).save(isA(SoldProduct.class));
    }

    /**
     * Method under test:
     * {@link SoldProductServiceImpl#addSoldProduct(Long, Long, SoldProductDto)}
     */
    @Test
    void testAddSoldProduct3() {
        // Arrange
        Sale sale = new Sale();
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalPrice(10.0d);

        SoldProduct soldProduct = new SoldProduct();
        soldProduct.setDeleted(true);
        soldProduct.setDiscount(3L);
        soldProduct.setId(1L);
        soldProduct.setName("Name");
        soldProduct.setPrice(10.0d);
        soldProduct.setProductId(1L);
        soldProduct.setQuantity(1);
        soldProduct.setSale(sale);
        soldProduct.setTotal(10.0d);

        Sale sale2 = new Sale();
        sale2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
        sale2.setTotalPrice(10.0d);
        SoldProduct soldProduct2 = mock(SoldProduct.class);
        when(soldProduct2.getPrice()).thenReturn(10.0d);
        when(soldProduct2.getQuantity()).thenReturn(1);
        doNothing().when(soldProduct2).setDeleted(anyBoolean());
        doNothing().when(soldProduct2).setDiscount(anyLong());
        doNothing().when(soldProduct2).setId(Mockito.<Long>any());
        doNothing().when(soldProduct2).setName(Mockito.<String>any());
        doNothing().when(soldProduct2).setPrice(anyDouble());
        doNothing().when(soldProduct2).setProductId(Mockito.<Long>any());
        doNothing().when(soldProduct2).setQuantity(anyInt());
        doNothing().when(soldProduct2).setSale(Mockito.<Sale>any());
        doNothing().when(soldProduct2).setTotal(anyDouble());
        soldProduct2.setDeleted(true);
        soldProduct2.setDiscount(3L);
        soldProduct2.setId(1L);
        soldProduct2.setName("Name");
        soldProduct2.setPrice(10.0d);
        soldProduct2.setProductId(1L);
        soldProduct2.setQuantity(1);
        soldProduct2.setSale(sale2);
        soldProduct2.setTotal(10.0d);
        Optional<SoldProduct> ofResult = Optional.of(soldProduct2);
        when(soldProductRepository.save(Mockito.<SoldProduct>any())).thenReturn(soldProduct);
        when(soldProductRepository.findBySaleIdAndProductId(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(ofResult);
        SoldProductDto soldProductDto = new SoldProductDto();
        when(mapUtil.convertSoldProductToSoldProductDto(Mockito.<SoldProduct>any())).thenReturn(soldProductDto);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setInventory(1);
        productDTO.setName("Name");
        productDTO.setPrice(10.0d);
        Optional<ProductDTO> ofResult2 = Optional.of(productDTO);
        doNothing().when(productServiceClient).updateProductInventory(Mockito.<ProductDTO>any());
        when(productServiceClient.getProductById(Mockito.<Long>any())).thenReturn(ofResult2);
        Optional<Long> ofResult3 = Optional.<Long>of(3L);
        when(campaignProductServiceImpl.getDiscountForProduct(Mockito.<Long>any())).thenReturn(ofResult3);

        Sale sale3 = new Sale();
        sale3.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale3.setDeleted(true);
        sale3.setId(1L);
        sale3.setPaymentType(PaymentType.CASH);
        sale3.setSoldProducts(new ArrayList<>());
        sale3.setTotalPrice(10.0d);
        Optional<Sale> ofResult4 = Optional.of(sale3);

        Sale sale4 = new Sale();
        sale4.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale4.setDeleted(true);
        sale4.setId(1L);
        sale4.setPaymentType(PaymentType.CASH);
        sale4.setSoldProducts(new ArrayList<>());
        sale4.setTotalPrice(10.0d);
        when(saleRepository.save(Mockito.<Sale>any())).thenReturn(sale4);
        when(saleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult4);

        // Act
        SoldProductDto actualAddSoldProductResult = soldProductServiceImpl.addSoldProduct(1L, 1L, new SoldProductDto());

        // Assert
        verify(productServiceClient).getProductById(eq(1L));
        verify(productServiceClient).updateProductInventory(isA(ProductDTO.class));
        verify(soldProductRepository, atLeast(1)).findBySaleIdAndProductId(eq(1L), eq(1L));
        verify(soldProduct2).getPrice();
        verify(soldProduct2, atLeast(1)).getQuantity();
        verify(soldProduct2).setDeleted(eq(true));
        verify(soldProduct2, atLeast(1)).setDiscount(eq(3L));
        verify(soldProduct2).setId(eq(1L));
        verify(soldProduct2, atLeast(1)).setName(eq("Name"));
        verify(soldProduct2, atLeast(1)).setPrice(eq(10.0d));
        verify(soldProduct2, atLeast(1)).setProductId(eq(1L));
        verify(soldProduct2, atLeast(1)).setQuantity(eq(1));
        verify(soldProduct2).setSale(isA(Sale.class));
        verify(soldProduct2, atLeast(1)).setTotal(anyDouble());
        verify(mapUtil).convertSoldProductToSoldProductDto(isA(SoldProduct.class));
        verify(campaignProductServiceImpl).getDiscountForProduct(eq(1L));
        verify(saleRepository).findById(eq(1L));
        verify(saleRepository).save(isA(Sale.class));
        verify(soldProductRepository).save(isA(SoldProduct.class));
        assertSame(soldProductDto, actualAddSoldProductResult);
    }

    /**
     * Method under test: {@link SoldProductServiceImpl#deleteSoldProduct(Long)}
     */
    @Test
    void testDeleteSoldProduct() {
        // Arrange
        Sale sale = new Sale();
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalPrice(10.0d);

        SoldProduct soldProduct = new SoldProduct();
        soldProduct.setDeleted(true);
        soldProduct.setDiscount(3L);
        soldProduct.setId(1L);
        soldProduct.setName("Name");
        soldProduct.setPrice(10.0d);
        soldProduct.setProductId(1L);
        soldProduct.setQuantity(1);
        soldProduct.setSale(sale);
        soldProduct.setTotal(10.0d);
        Optional<SoldProduct> ofResult = Optional.of(soldProduct);

        Sale sale2 = new Sale();
        sale2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
        sale2.setTotalPrice(10.0d);

        SoldProduct soldProduct2 = new SoldProduct();
        soldProduct2.setDeleted(true);
        soldProduct2.setDiscount(3L);
        soldProduct2.setId(1L);
        soldProduct2.setName("Name");
        soldProduct2.setPrice(10.0d);
        soldProduct2.setProductId(1L);
        soldProduct2.setQuantity(1);
        soldProduct2.setSale(sale2);
        soldProduct2.setTotal(10.0d);
        when(soldProductRepository.save(Mockito.<SoldProduct>any())).thenReturn(soldProduct2);
        when(soldProductRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        SoldProductDto soldProductDto = new SoldProductDto();
        when(mapUtil.convertSoldProductToSoldProductDto(Mockito.<SoldProduct>any())).thenReturn(soldProductDto);

        // Act
        SoldProductDto actualDeleteSoldProductResult = soldProductServiceImpl.deleteSoldProduct(1L);

        // Assert
        verify(mapUtil).convertSoldProductToSoldProductDto(isA(SoldProduct.class));
        verify(soldProductRepository).findById(eq(1L));
        verify(soldProductRepository).save(isA(SoldProduct.class));
        assertSame(soldProductDto, actualDeleteSoldProductResult);
    }

    /**
     * Method under test: {@link SoldProductServiceImpl#deleteSoldProduct(Long)}
     */
    @Test
    void testDeleteSoldProduct2() {
        // Arrange
        Sale sale = new Sale();
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalPrice(10.0d);

        SoldProduct soldProduct = new SoldProduct();
        soldProduct.setDeleted(true);
        soldProduct.setDiscount(3L);
        soldProduct.setId(1L);
        soldProduct.setName("Name");
        soldProduct.setPrice(10.0d);
        soldProduct.setProductId(1L);
        soldProduct.setQuantity(1);
        soldProduct.setSale(sale);
        soldProduct.setTotal(10.0d);
        Optional<SoldProduct> ofResult = Optional.of(soldProduct);

        Sale sale2 = new Sale();
        sale2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
        sale2.setTotalPrice(10.0d);

        SoldProduct soldProduct2 = new SoldProduct();
        soldProduct2.setDeleted(true);
        soldProduct2.setDiscount(3L);
        soldProduct2.setId(1L);
        soldProduct2.setName("Name");
        soldProduct2.setPrice(10.0d);
        soldProduct2.setProductId(1L);
        soldProduct2.setQuantity(1);
        soldProduct2.setSale(sale2);
        soldProduct2.setTotal(10.0d);
        when(soldProductRepository.save(Mockito.<SoldProduct>any())).thenReturn(soldProduct2);
        when(soldProductRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(mapUtil.convertSoldProductToSoldProductDto(Mockito.<SoldProduct>any()))
                .thenThrow(new ProductNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ProductNotFoundException.class, () -> soldProductServiceImpl.deleteSoldProduct(1L));
        verify(mapUtil).convertSoldProductToSoldProductDto(isA(SoldProduct.class));
        verify(soldProductRepository).findById(eq(1L));
        verify(soldProductRepository).save(isA(SoldProduct.class));
    }

    /**
     * Method under test: {@link SoldProductServiceImpl#deleteSoldProduct(Long)}
     */
    @Test
    void testDeleteSoldProduct3() {
        // Arrange
        Sale sale = new Sale();
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalPrice(10.0d);

        SoldProduct soldProduct = new SoldProduct();
        soldProduct.setDeleted(true);
        soldProduct.setDiscount(3L);
        soldProduct.setId(1L);
        soldProduct.setName("Name");
        soldProduct.setPrice(10.0d);
        soldProduct.setProductId(1L);
        soldProduct.setQuantity(1);
        soldProduct.setSale(sale);
        soldProduct.setTotal(10.0d);
        Optional<SoldProduct> ofResult = Optional.of(soldProduct);

        Sale sale2 = new Sale();
        sale2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
        sale2.setTotalPrice(10.0d);
        SoldProduct soldProduct2 = mock(SoldProduct.class);
        when(soldProduct2.getId()).thenReturn(1L);
        doNothing().when(soldProduct2).setDeleted(anyBoolean());
        doNothing().when(soldProduct2).setDiscount(anyLong());
        doNothing().when(soldProduct2).setId(Mockito.<Long>any());
        doNothing().when(soldProduct2).setName(Mockito.<String>any());
        doNothing().when(soldProduct2).setPrice(anyDouble());
        doNothing().when(soldProduct2).setProductId(Mockito.<Long>any());
        doNothing().when(soldProduct2).setQuantity(anyInt());
        doNothing().when(soldProduct2).setSale(Mockito.<Sale>any());
        doNothing().when(soldProduct2).setTotal(anyDouble());
        soldProduct2.setDeleted(true);
        soldProduct2.setDiscount(3L);
        soldProduct2.setId(1L);
        soldProduct2.setName("Name");
        soldProduct2.setPrice(10.0d);
        soldProduct2.setProductId(1L);
        soldProduct2.setQuantity(1);
        soldProduct2.setSale(sale2);
        soldProduct2.setTotal(10.0d);
        when(soldProductRepository.save(Mockito.<SoldProduct>any())).thenReturn(soldProduct2);
        when(soldProductRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        SoldProductDto soldProductDto = new SoldProductDto();
        when(mapUtil.convertSoldProductToSoldProductDto(Mockito.<SoldProduct>any())).thenReturn(soldProductDto);

        // Act
        SoldProductDto actualDeleteSoldProductResult = soldProductServiceImpl.deleteSoldProduct(1L);

        // Assert
        verify(soldProduct2).getId();
        verify(soldProduct2).setDeleted(eq(true));
        verify(soldProduct2).setDiscount(eq(3L));
        verify(soldProduct2).setId(eq(1L));
        verify(soldProduct2).setName(eq("Name"));
        verify(soldProduct2).setPrice(eq(10.0d));
        verify(soldProduct2).setProductId(eq(1L));
        verify(soldProduct2).setQuantity(eq(1));
        verify(soldProduct2).setSale(isA(Sale.class));
        verify(soldProduct2).setTotal(eq(10.0d));
        verify(mapUtil).convertSoldProductToSoldProductDto(isA(SoldProduct.class));
        verify(soldProductRepository).findById(eq(1L));
        verify(soldProductRepository).save(isA(SoldProduct.class));
        assertSame(soldProductDto, actualDeleteSoldProductResult);
    }

    /**
     * Method under test: {@link SoldProductServiceImpl#deleteSoldProduct(Long)}
     */
    @Test
    void testDeleteSoldProduct4() {
        // Arrange
        Sale sale = new Sale();
        sale.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale.setDeleted(true);
        sale.setId(1L);
        sale.setPaymentType(PaymentType.CASH);
        sale.setSoldProducts(new ArrayList<>());
        sale.setTotalPrice(10.0d);
        SoldProduct soldProduct = mock(SoldProduct.class);
        doNothing().when(soldProduct).setDeleted(anyBoolean());
        doNothing().when(soldProduct).setDiscount(anyLong());
        doNothing().when(soldProduct).setId(Mockito.<Long>any());
        doNothing().when(soldProduct).setName(Mockito.<String>any());
        doNothing().when(soldProduct).setPrice(anyDouble());
        doNothing().when(soldProduct).setProductId(Mockito.<Long>any());
        doNothing().when(soldProduct).setQuantity(anyInt());
        doNothing().when(soldProduct).setSale(Mockito.<Sale>any());
        doNothing().when(soldProduct).setTotal(anyDouble());
        soldProduct.setDeleted(true);
        soldProduct.setDiscount(3L);
        soldProduct.setId(1L);
        soldProduct.setName("Name");
        soldProduct.setPrice(10.0d);
        soldProduct.setProductId(1L);
        soldProduct.setQuantity(1);
        soldProduct.setSale(sale);
        soldProduct.setTotal(10.0d);
        Optional<SoldProduct> ofResult = Optional.of(soldProduct);

        Sale sale2 = new Sale();
        sale2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sale2.setDeleted(true);
        sale2.setId(1L);
        sale2.setPaymentType(PaymentType.CASH);
        sale2.setSoldProducts(new ArrayList<>());
        sale2.setTotalPrice(10.0d);
        SoldProduct soldProduct2 = mock(SoldProduct.class);
        when(soldProduct2.getId()).thenReturn(1L);
        doNothing().when(soldProduct2).setDeleted(anyBoolean());
        doNothing().when(soldProduct2).setDiscount(anyLong());
        doNothing().when(soldProduct2).setId(Mockito.<Long>any());
        doNothing().when(soldProduct2).setName(Mockito.<String>any());
        doNothing().when(soldProduct2).setPrice(anyDouble());
        doNothing().when(soldProduct2).setProductId(Mockito.<Long>any());
        doNothing().when(soldProduct2).setQuantity(anyInt());
        doNothing().when(soldProduct2).setSale(Mockito.<Sale>any());
        doNothing().when(soldProduct2).setTotal(anyDouble());
        soldProduct2.setDeleted(true);
        soldProduct2.setDiscount(3L);
        soldProduct2.setId(1L);
        soldProduct2.setName("Name");
        soldProduct2.setPrice(10.0d);
        soldProduct2.setProductId(1L);
        soldProduct2.setQuantity(1);
        soldProduct2.setSale(sale2);
        soldProduct2.setTotal(10.0d);
        when(soldProductRepository.save(Mockito.<SoldProduct>any())).thenReturn(soldProduct2);
        when(soldProductRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        SoldProductDto soldProductDto = new SoldProductDto();
        when(mapUtil.convertSoldProductToSoldProductDto(Mockito.<SoldProduct>any())).thenReturn(soldProductDto);

        // Act
        SoldProductDto actualDeleteSoldProductResult = soldProductServiceImpl.deleteSoldProduct(1L);

        // Assert
        verify(soldProduct2).getId();
        verify(soldProduct2).setDeleted(eq(true));
        verify(soldProduct, atLeast(1)).setDeleted(eq(true));
        verify(soldProduct2).setDiscount(eq(3L));
        verify(soldProduct).setDiscount(eq(3L));
        verify(soldProduct2).setId(eq(1L));
        verify(soldProduct).setId(eq(1L));
        verify(soldProduct2).setName(eq("Name"));
        verify(soldProduct).setName(eq("Name"));
        verify(soldProduct2).setPrice(eq(10.0d));
        verify(soldProduct).setPrice(eq(10.0d));
        verify(soldProduct2).setProductId(eq(1L));
        verify(soldProduct).setProductId(eq(1L));
        verify(soldProduct2).setQuantity(eq(1));
        verify(soldProduct).setQuantity(eq(1));
        verify(soldProduct2).setSale(isA(Sale.class));
        verify(soldProduct).setSale(isA(Sale.class));
        verify(soldProduct2).setTotal(eq(10.0d));
        verify(soldProduct).setTotal(eq(10.0d));
        verify(mapUtil).convertSoldProductToSoldProductDto(isA(SoldProduct.class));
        verify(soldProductRepository).findById(eq(1L));
        verify(soldProductRepository).save(isA(SoldProduct.class));
        assertSame(soldProductDto, actualDeleteSoldProductResult);
    }

    @Test
    public void testAddSoldProduct_ProductDoesNotExistInSale() {
        // Arrange
        Long productId = 1L;
        Long saleId = 1L;
        SoldProductDto soldProductDto = new SoldProductDto();
        soldProductDto.setProductId(productId);
        soldProductDto.setQuantity(2);
        soldProductDto.setInventory(10);  // Set inventory to a valid amount

        ProductDTO product = new ProductDTO();
        product.setId(productId);
        product.setPrice(100.0);
        product.setInventory(10);  // Set inventory to a valid amount

        Sale sale = new Sale();
        sale.setId(saleId);
        sale.setSoldProducts(new ArrayList<>());  // Sold products list initialized to avoid NullPointerException

        SoldProduct savedSoldProduct = new SoldProduct();
        savedSoldProduct.setId(1L);
        savedSoldProduct.setProductId(productId);
        savedSoldProduct.setSale(sale);
        savedSoldProduct.setQuantity(2);
        savedSoldProduct.setTotal(200.0);

        Mockito.when(productServiceClient.getProductById(productId)).thenReturn(Optional.of(product));
        Mockito.when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
        Mockito.when(soldProductRepository.findBySaleIdAndProductId(saleId, productId)).thenReturn(Optional.empty());
        Mockito.when(soldProductRepository.save(Mockito.any(SoldProduct.class))).thenReturn(savedSoldProduct);
        Mockito.when(soldProductRepository.findBySaleIdAndProductId(saleId, productId)).thenReturn(Optional.of(savedSoldProduct));
        Mockito.when(mapUtil.convertSoldProductToSoldProductDto(savedSoldProduct)).thenReturn(soldProductDto);

        // Act
        SoldProductDto result = soldProductServiceImpl.addSoldProduct(productId, saleId, soldProductDto);

        // Assert
        assertNotNull(result);
        Assertions.assertEquals(productId, result.getProductId());
        Mockito.verify(soldProductRepository).save(Mockito.any(SoldProduct.class));
    }

    @Test
    public void testAddSoldProduct_ProductExistsInSale() {
        // Arrange
        Long productId = 1L;
        Long saleId = 1L;
        SoldProductDto soldProductDto = new SoldProductDto();
        soldProductDto.setProductId(productId);
        soldProductDto.setQuantity(2);
        soldProductDto.setInventory(10); // Inventory set to 10

        ProductDTO product = new ProductDTO();
        product.setId(productId);
        product.setPrice(100.0);
        product.setName("Test Product"); // Ensure product name is set
        product.setInventory(10); // Ensure inventory is set on product DTO

        Sale sale = new Sale();
        sale.setId(saleId);
        sale.setSoldProducts(new ArrayList<>()); // Sold products list initialized

        SoldProduct existingSoldProduct = new SoldProduct();
        existingSoldProduct.setId(1L);
        existingSoldProduct.setProductId(productId);
        existingSoldProduct.setQuantity(1); // Initially 1 unit
        existingSoldProduct.setSale(sale);
        existingSoldProduct.setPrice(product.getPrice());
        existingSoldProduct.setTotal(product.getPrice() * existingSoldProduct.getQuantity());

        // Mocking
        Mockito.when(productServiceClient.getProductById(productId)).thenReturn(Optional.of(product));
        Mockito.when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
        Mockito.when(soldProductRepository.findBySaleIdAndProductId(saleId, productId)).thenReturn(Optional.of(existingSoldProduct));
        Mockito.when(soldProductRepository.save(existingSoldProduct)).thenReturn(existingSoldProduct);
        Mockito.when(mapUtil.convertSoldProductToSoldProductDto(existingSoldProduct)).thenReturn(soldProductDto);

        // Act
        SoldProductDto result = soldProductServiceImpl.addSoldProduct(productId, saleId, soldProductDto);

        // Assert
        assertNotNull(result);
        Assertions.assertEquals(productId, result.getProductId());
        Assertions.assertEquals(3, existingSoldProduct.getQuantity()); // Updated quantity (1 + 2)
        Assertions.assertEquals(300.0, existingSoldProduct.getTotal()); // Updated total (3 * 100.0)
        Mockito.verify(soldProductRepository).save(existingSoldProduct);
    }



    @Test
    public void testUpdateSoldProduct_Success() {
        // Arrange
        Long soldProductId = 1L;
        SoldProductDto soldProductDto = new SoldProductDto();
        soldProductDto.setQuantity(3);

        SoldProduct existingSoldProduct = new SoldProduct();
        existingSoldProduct.setId(soldProductId);
        existingSoldProduct.setQuantity(2);

        Mockito.when(soldProductRepository.findById(soldProductId)).thenReturn(Optional.of(existingSoldProduct));
        Mockito.when(soldProductRepository.save(existingSoldProduct)).thenReturn(existingSoldProduct);
        Mockito.when(mapUtil.convertSoldProductToSoldProductDto(existingSoldProduct)).thenReturn(soldProductDto);

        // Act
        SoldProductDto result = soldProductServiceImpl.updateSoldProduct(soldProductId, soldProductDto);

        // Assert
        assertNotNull(result);
        assertEquals(soldProductDto.getQuantity(), result.getQuantity());
        Mockito.verify(soldProductRepository).save(existingSoldProduct);
    }
    @Test
    public void testUpdateSoldProduct_NotFound() {
        // Arrange
        Long soldProductId = 1L;
        SoldProductDto soldProductDto = new SoldProductDto();

        Mockito.when(soldProductRepository.findById(soldProductId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> {
            soldProductServiceImpl.updateSoldProduct(soldProductId, soldProductDto);
        });
    }

    @Test
    public void testDeleteSoldProduct_NotFound() {
        // Arrange
        Long soldProductId = 1L;

        Mockito.when(soldProductRepository.findById(soldProductId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> {
            soldProductServiceImpl.deleteSoldProduct(soldProductId);
        });
    }

    @Test
    public void testAddSoldProduct_ProductNotFound() {
        // Arrange
        Long productId = 1L;
        Long saleId = 1L;
        SoldProductDto soldProductDto = new SoldProductDto();

        Mockito.when(productServiceClient.getProductById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> {
            soldProductServiceImpl.addSoldProduct(productId, saleId, soldProductDto);
        });
    }

    @Test
    public void testAddSoldProduct_SaleNotFound() {
        // Arrange
        Long productId = 1L;
        Long saleId = 1L;
        SoldProductDto soldProductDto = new SoldProductDto();
        ProductDTO productDTO = new ProductDTO();

        Mockito.when(productServiceClient.getProductById(productId)).thenReturn(Optional.of(productDTO));
        Mockito.when(saleRepository.findById(saleId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SaleNotFoundException.class, () -> {
            soldProductServiceImpl.addSoldProduct(productId, saleId, soldProductDto);
        });
    }

    @Test
    public void testCheckAndUpdateInventory_QuantityShortage() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setInventory(5);
        int quantity = 10;

        // Act & Assert
        assertThrows(ProductQuantityShortageException.class, () -> {
            soldProductServiceImpl.checkAndUpdateInventory(productDTO, quantity);
        });
    }
    @Test
    public void testCreateNewSoldProduct() {
        // Arrange
        Long productId = 1L;
        Long saleId = 1L;
        SoldProductDto soldProductDto = new SoldProductDto();
        soldProductDto.setProductId(productId);
        soldProductDto.setQuantity(2);
        soldProductDto.setInventory(10);

        ProductDTO product = new ProductDTO();
        product.setId(productId);
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setInventory(10);

        Sale sale = new Sale();
        sale.setId(saleId);
        sale.setSoldProducts(new ArrayList<>());

        SoldProduct soldProduct = new SoldProduct();

        // Mocking
        Mockito.when(productServiceClient.getProductById(productId)).thenReturn(Optional.of(product));
        Mockito.when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
        Mockito.doNothing().when(productServiceClient).updateProductInventory(product);

        // Act
        soldProductServiceImpl.createNewSoldProduct(soldProductDto, product, sale, soldProduct);

        // Assert
        Assertions.assertEquals(productId, soldProduct.getProductId());
        Assertions.assertEquals("Test Product", soldProduct.getName());
        Assertions.assertEquals(2, soldProduct.getQuantity());
        Assertions.assertEquals(200.0, soldProduct.getTotal());
        Mockito.verify(soldProductRepository).save(soldProduct);
    }

    @Test
    public void testGetSoldProducts2() {
        // Arrange
        int page = 0;
        int size = 10;
        String name = "Product1";
        Double minPrice = 50.0;
        Double maxPrice = 200.0;
        boolean deleted = false;
        String sortBy = "name";
        String sortDirection = "ASC";

        SoldProduct soldProduct1 = new SoldProduct();
        SoldProduct soldProduct2 = new SoldProduct();
        List<SoldProduct> soldProducts = Arrays.asList(soldProduct1, soldProduct2);

        SoldProductDto soldProductDto1 = new SoldProductDto();
        SoldProductDto soldProductDto2 = new SoldProductDto();
        List<SoldProductDto> soldProductDtos = Arrays.asList(soldProductDto1, soldProductDto2);

        Page<SoldProduct> pageResponse = new PageImpl<>(soldProducts, PageRequest.of(page, size), soldProducts.size());

        when(soldProductRepository.getSoldProductsFiltered(name, minPrice, maxPrice, deleted, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy))))
                .thenReturn(pageResponse);
        when(mapUtil.convertSoldProductToSoldProductDto(soldProduct1)).thenReturn(soldProductDto1);
        when(mapUtil.convertSoldProductToSoldProductDto(soldProduct2)).thenReturn(soldProductDto2);

        // Act
        PaginationResponse<SoldProductDto> response = soldProductServiceImpl.getSoldProducts(page, size, name, minPrice, maxPrice, deleted, sortBy, sortDirection);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getContent().size());
        assertEquals(1, response.getPageable().getTotalPages());
        assertEquals(2, response.getPageable().getTotalElements());
        verify(soldProductRepository, times(1)).getSoldProductsFiltered(name, minPrice, maxPrice, deleted, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy)));
        verify(mapUtil, times(1)).convertSoldProductToSoldProductDto(soldProduct1);
        verify(mapUtil, times(1)).convertSoldProductToSoldProductDto(soldProduct2);
    }
}