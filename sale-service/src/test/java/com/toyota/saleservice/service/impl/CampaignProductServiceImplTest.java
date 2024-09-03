package com.toyota.saleservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.toyota.saleservice.config.ProductServiceClient;
import com.toyota.saleservice.dao.CampaignProductRepository;
import com.toyota.saleservice.dao.CampaignRepository;
import com.toyota.saleservice.domain.Campaign;
import com.toyota.saleservice.domain.CampaignProduct;
import com.toyota.saleservice.dto.CampaignProductDto;
import com.toyota.saleservice.dto.ProductDTO;
import com.toyota.saleservice.exception.CampaignNotFoundException;
import com.toyota.saleservice.exception.CampaignProductNotFoundException;
import com.toyota.saleservice.exception.ProductNotFoundException;
import com.toyota.saleservice.service.common.MapUtil;
import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CampaignProductServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CampaignProductServiceImplTest {
    @MockBean
    private CampaignProductRepository campaignProductRepository;

    @Autowired
    private CampaignProductServiceImpl campaignProductServiceImpl;

    @MockBean
    private CampaignRepository campaignRepository;

    @MockBean
    private MapUtil mapUtil;

    @MockBean
    private ProductServiceClient productServiceClient;

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#getAllCampaignProducts()}
     */
    @Test
    void testGetAllCampaignProducts() {
        // Arrange
        when(campaignProductRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<CampaignProductDto> actualAllCampaignProducts = campaignProductServiceImpl.getAllCampaignProducts();

        // Assert
        verify(campaignProductRepository).findAll();
        assertTrue(actualAllCampaignProducts.isEmpty());
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#getAllCampaignProducts()}
     */
    @Test
    void testGetAllCampaignProducts2() {
        // Arrange
        when(mapUtil.convertCampaignProductToCampaignProductDto(Mockito.<CampaignProduct>any()))
                .thenThrow(new CampaignNotFoundException("An error occurred"));

        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Getting all campaign products");

        CampaignProduct campaignProduct = new CampaignProduct();
        campaignProduct.setCampaign(campaign);
        campaignProduct.setDeleted(true);
        campaignProduct.setId(1L);
        campaignProduct.setProductId(1L);

        ArrayList<CampaignProduct> campaignProductList = new ArrayList<>();
        campaignProductList.add(campaignProduct);
        when(campaignProductRepository.findAll()).thenReturn(campaignProductList);

        // Act and Assert
        assertThrows(CampaignNotFoundException.class, () -> campaignProductServiceImpl.getAllCampaignProducts());
        verify(mapUtil).convertCampaignProductToCampaignProductDto(isA(CampaignProduct.class));
        verify(campaignProductRepository).findAll();
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#addCampaignProduct(CampaignProductDto)}
     */
    @Test
    void testAddCampaignProduct() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Name");

        CampaignProduct campaignProduct = new CampaignProduct();
        campaignProduct.setCampaign(campaign);
        campaignProduct.setDeleted(true);
        campaignProduct.setId(1L);
        campaignProduct.setProductId(1L);
        CampaignProductDto campaignProductDto = new CampaignProductDto();
        when(mapUtil.convertCampaignProductToCampaignProductDto(Mockito.<CampaignProduct>any()))
                .thenReturn(campaignProductDto);
        when(mapUtil.convertCampaignProductDtoToCampaignProduct(Mockito.<CampaignProductDto>any()))
                .thenReturn(campaignProduct);

        Campaign campaign2 = new Campaign();
        campaign2.setCampaignProducts(new ArrayList<>());
        campaign2.setDeleted(true);
        campaign2.setDescription("The characteristics of someone or something");
        campaign2.setDiscount(3L);
        campaign2.setId(1L);
        campaign2.setName("Name");

        CampaignProduct campaignProduct2 = new CampaignProduct();
        campaignProduct2.setCampaign(campaign2);
        campaignProduct2.setDeleted(true);
        campaignProduct2.setId(1L);
        campaignProduct2.setProductId(1L);
        when(campaignProductRepository.save(Mockito.<CampaignProduct>any())).thenReturn(campaignProduct2);
        when(campaignRepository.existsById(Mockito.<Long>any())).thenReturn(true);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setInventory(1);
        productDTO.setName("Name");
        productDTO.setPrice(10.0d);
        Optional<ProductDTO> ofResult = Optional.of(productDTO);
        when(productServiceClient.getProductById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        CampaignProductDto actualAddCampaignProductResult = campaignProductServiceImpl
                .addCampaignProduct(new CampaignProductDto());

        // Assert
        verify(productServiceClient).getProductById(isNull());
        verify(mapUtil).convertCampaignProductDtoToCampaignProduct(isA(CampaignProductDto.class));
        verify(mapUtil).convertCampaignProductToCampaignProductDto(isA(CampaignProduct.class));
        verify(campaignRepository).existsById(isNull());
        verify(campaignProductRepository).save(isA(CampaignProduct.class));
        assertSame(campaignProductDto, actualAddCampaignProductResult);
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#addCampaignProduct(CampaignProductDto)}
     */
    @Test
    void testAddCampaignProduct2() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Name");

        CampaignProduct campaignProduct = new CampaignProduct();
        campaignProduct.setCampaign(campaign);
        campaignProduct.setDeleted(true);
        campaignProduct.setId(1L);
        campaignProduct.setProductId(1L);
        when(mapUtil.convertCampaignProductToCampaignProductDto(Mockito.<CampaignProduct>any()))
                .thenThrow(new CampaignNotFoundException("An error occurred"));
        when(mapUtil.convertCampaignProductDtoToCampaignProduct(Mockito.<CampaignProductDto>any()))
                .thenReturn(campaignProduct);

        Campaign campaign2 = new Campaign();
        campaign2.setCampaignProducts(new ArrayList<>());
        campaign2.setDeleted(true);
        campaign2.setDescription("The characteristics of someone or something");
        campaign2.setDiscount(3L);
        campaign2.setId(1L);
        campaign2.setName("Name");

        CampaignProduct campaignProduct2 = new CampaignProduct();
        campaignProduct2.setCampaign(campaign2);
        campaignProduct2.setDeleted(true);
        campaignProduct2.setId(1L);
        campaignProduct2.setProductId(1L);
        when(campaignProductRepository.save(Mockito.<CampaignProduct>any())).thenReturn(campaignProduct2);
        when(campaignRepository.existsById(Mockito.<Long>any())).thenReturn(true);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setInventory(1);
        productDTO.setName("Name");
        productDTO.setPrice(10.0d);
        Optional<ProductDTO> ofResult = Optional.of(productDTO);
        when(productServiceClient.getProductById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(CampaignNotFoundException.class,
                () -> campaignProductServiceImpl.addCampaignProduct(new CampaignProductDto()));
        verify(productServiceClient).getProductById(isNull());
        verify(mapUtil).convertCampaignProductDtoToCampaignProduct(isA(CampaignProductDto.class));
        verify(mapUtil).convertCampaignProductToCampaignProductDto(isA(CampaignProduct.class));
        verify(campaignRepository).existsById(isNull());
        verify(campaignProductRepository).save(isA(CampaignProduct.class));
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#addCampaignProduct(CampaignProductDto)}
     */
    @Test
    void testAddCampaignProduct3() {
        // Arrange
        when(campaignRepository.existsById(Mockito.<Long>any())).thenReturn(false);

        // Act and Assert
        assertThrows(CampaignNotFoundException.class,
                () -> campaignProductServiceImpl.addCampaignProduct(new CampaignProductDto()));
        verify(campaignRepository).existsById(isNull());
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#addCampaignProduct(CampaignProductDto)}
     */
    @Test
    void testAddCampaignProduct4() {
        // Arrange
        when(campaignRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        Optional<ProductDTO> emptyResult = Optional.empty();
        when(productServiceClient.getProductById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ProductNotFoundException.class,
                () -> campaignProductServiceImpl.addCampaignProduct(new CampaignProductDto()));
        verify(productServiceClient).getProductById(isNull());
        verify(campaignRepository).existsById(isNull());
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#updateCampaignProduct(Long, CampaignProductDto)}
     */
    @Test
    void testUpdateCampaignProduct() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Name");

        CampaignProduct campaignProduct = new CampaignProduct();
        campaignProduct.setCampaign(campaign);
        campaignProduct.setDeleted(true);
        campaignProduct.setId(1L);
        campaignProduct.setProductId(1L);
        CampaignProductDto campaignProductDto = new CampaignProductDto();
        when(mapUtil.convertCampaignProductToCampaignProductDto(Mockito.<CampaignProduct>any()))
                .thenReturn(campaignProductDto);
        when(mapUtil.convertCampaignProductDtoToCampaignProduct(Mockito.<CampaignProductDto>any()))
                .thenReturn(campaignProduct);

        Campaign campaign2 = new Campaign();
        campaign2.setCampaignProducts(new ArrayList<>());
        campaign2.setDeleted(true);
        campaign2.setDescription("The characteristics of someone or something");
        campaign2.setDiscount(3L);
        campaign2.setId(1L);
        campaign2.setName("Name");

        CampaignProduct campaignProduct2 = new CampaignProduct();
        campaignProduct2.setCampaign(campaign2);
        campaignProduct2.setDeleted(true);
        campaignProduct2.setId(1L);
        campaignProduct2.setProductId(1L);
        Optional<CampaignProduct> ofResult = Optional.of(campaignProduct2);

        Campaign campaign3 = new Campaign();
        campaign3.setCampaignProducts(new ArrayList<>());
        campaign3.setDeleted(true);
        campaign3.setDescription("The characteristics of someone or something");
        campaign3.setDiscount(3L);
        campaign3.setId(1L);
        campaign3.setName("Name");

        CampaignProduct campaignProduct3 = new CampaignProduct();
        campaignProduct3.setCampaign(campaign3);
        campaignProduct3.setDeleted(true);
        campaignProduct3.setId(1L);
        campaignProduct3.setProductId(1L);
        when(campaignProductRepository.save(Mockito.<CampaignProduct>any())).thenReturn(campaignProduct3);
        when(campaignProductRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(campaignRepository.existsById(Mockito.<Long>any())).thenReturn(true);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setInventory(1);
        productDTO.setName("Name");
        productDTO.setPrice(10.0d);
        Optional<ProductDTO> ofResult2 = Optional.of(productDTO);
        when(productServiceClient.getProductById(Mockito.<Long>any())).thenReturn(ofResult2);

        // Act
        CampaignProductDto actualUpdateCampaignProductResult = campaignProductServiceImpl.updateCampaignProduct(1L,
                new CampaignProductDto());

        // Assert
        verify(productServiceClient).getProductById(isNull());
        verify(mapUtil).convertCampaignProductDtoToCampaignProduct(isA(CampaignProductDto.class));
        verify(mapUtil).convertCampaignProductToCampaignProductDto(isA(CampaignProduct.class));
        verify(campaignRepository).existsById(isNull());
        verify(campaignProductRepository).findById(eq(1L));
        verify(campaignProductRepository).save(isA(CampaignProduct.class));
        assertSame(campaignProductDto, actualUpdateCampaignProductResult);
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#updateCampaignProduct(Long, CampaignProductDto)}
     */
    @Test
    void testUpdateCampaignProduct2() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Name");

        CampaignProduct campaignProduct = new CampaignProduct();
        campaignProduct.setCampaign(campaign);
        campaignProduct.setDeleted(true);
        campaignProduct.setId(1L);
        campaignProduct.setProductId(1L);
        when(mapUtil.convertCampaignProductDtoToCampaignProduct(Mockito.<CampaignProductDto>any()))
                .thenReturn(campaignProduct);

        Campaign campaign2 = new Campaign();
        campaign2.setCampaignProducts(new ArrayList<>());
        campaign2.setDeleted(true);
        campaign2.setDescription("The characteristics of someone or something");
        campaign2.setDiscount(3L);
        campaign2.setId(1L);
        campaign2.setName("Name");

        CampaignProduct campaignProduct2 = new CampaignProduct();
        campaignProduct2.setCampaign(campaign2);
        campaignProduct2.setDeleted(true);
        campaignProduct2.setId(1L);
        campaignProduct2.setProductId(1L);
        Optional<CampaignProduct> ofResult = Optional.of(campaignProduct2);
        when(campaignProductRepository.save(Mockito.<CampaignProduct>any()))
                .thenThrow(new CampaignNotFoundException("An error occurred"));
        when(campaignProductRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(campaignRepository.existsById(Mockito.<Long>any())).thenReturn(true);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setInventory(1);
        productDTO.setName("Name");
        productDTO.setPrice(10.0d);
        Optional<ProductDTO> ofResult2 = Optional.of(productDTO);
        when(productServiceClient.getProductById(Mockito.<Long>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(CampaignNotFoundException.class,
                () -> campaignProductServiceImpl.updateCampaignProduct(1L, new CampaignProductDto()));
        verify(productServiceClient).getProductById(isNull());
        verify(mapUtil).convertCampaignProductDtoToCampaignProduct(isA(CampaignProductDto.class));
        verify(campaignRepository).existsById(isNull());
        verify(campaignProductRepository).findById(eq(1L));
        verify(campaignProductRepository).save(isA(CampaignProduct.class));
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#updateCampaignProduct(Long, CampaignProductDto)}
     */
    @Test
    void testUpdateCampaignProduct3() {
        // Arrange
        Optional<CampaignProduct> emptyResult = Optional.empty();
        when(campaignProductRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(EntityNotFoundException.class,
                () -> campaignProductServiceImpl.updateCampaignProduct(1L, new CampaignProductDto()));
        verify(campaignProductRepository).findById(eq(1L));
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#updateCampaignProduct(Long, CampaignProductDto)}
     */
    @Test
    void testUpdateCampaignProduct4() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Name");

        CampaignProduct campaignProduct = new CampaignProduct();
        campaignProduct.setCampaign(campaign);
        campaignProduct.setDeleted(true);
        campaignProduct.setId(1L);
        campaignProduct.setProductId(1L);
        Optional<CampaignProduct> ofResult = Optional.of(campaignProduct);
        when(campaignProductRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(campaignRepository.existsById(Mockito.<Long>any())).thenReturn(false);

        // Act and Assert
        assertThrows(EntityNotFoundException.class,
                () -> campaignProductServiceImpl.updateCampaignProduct(1L, new CampaignProductDto()));
        verify(campaignRepository).existsById(isNull());
        verify(campaignProductRepository).findById(eq(1L));
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#deleteCampaignProduct(Long)}
     */
    @Test
    void testDeleteCampaignProduct() {
        // Arrange
        CampaignProductDto campaignProductDto = new CampaignProductDto();
        when(mapUtil.convertCampaignProductToCampaignProductDto(Mockito.<CampaignProduct>any()))
                .thenReturn(campaignProductDto);

        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Name");

        CampaignProduct campaignProduct = new CampaignProduct();
        campaignProduct.setCampaign(campaign);
        campaignProduct.setDeleted(true);
        campaignProduct.setId(1L);
        campaignProduct.setProductId(1L);
        Optional<CampaignProduct> ofResult = Optional.of(campaignProduct);

        Campaign campaign2 = new Campaign();
        campaign2.setCampaignProducts(new ArrayList<>());
        campaign2.setDeleted(true);
        campaign2.setDescription("The characteristics of someone or something");
        campaign2.setDiscount(3L);
        campaign2.setId(1L);
        campaign2.setName("Name");

        CampaignProduct campaignProduct2 = new CampaignProduct();
        campaignProduct2.setCampaign(campaign2);
        campaignProduct2.setDeleted(true);
        campaignProduct2.setId(1L);
        campaignProduct2.setProductId(1L);
        when(campaignProductRepository.save(Mockito.<CampaignProduct>any())).thenReturn(campaignProduct2);
        when(campaignProductRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        CampaignProductDto actualDeleteCampaignProductResult = campaignProductServiceImpl.deleteCampaignProduct(1L);

        // Assert
        verify(mapUtil).convertCampaignProductToCampaignProductDto(isA(CampaignProduct.class));
        verify(campaignProductRepository).findById(eq(1L));
        verify(campaignProductRepository).save(isA(CampaignProduct.class));
        assertSame(campaignProductDto, actualDeleteCampaignProductResult);
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#deleteCampaignProduct(Long)}
     */
    @Test
    void testDeleteCampaignProduct2() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Name");

        CampaignProduct campaignProduct = new CampaignProduct();
        campaignProduct.setCampaign(campaign);
        campaignProduct.setDeleted(true);
        campaignProduct.setId(1L);
        campaignProduct.setProductId(1L);
        Optional<CampaignProduct> ofResult = Optional.of(campaignProduct);
        when(campaignProductRepository.save(Mockito.<CampaignProduct>any()))
                .thenThrow(new CampaignNotFoundException("An error occurred"));
        when(campaignProductRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(CampaignNotFoundException.class, () -> campaignProductServiceImpl.deleteCampaignProduct(1L));
        verify(campaignProductRepository).findById(eq(1L));
        verify(campaignProductRepository).save(isA(CampaignProduct.class));
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#deleteCampaignProduct(Long)}
     */
    @Test
    void testDeleteCampaignProduct3() {
        // Arrange
        Optional<CampaignProduct> emptyResult = Optional.empty();
        when(campaignProductRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(CampaignProductNotFoundException.class, () -> campaignProductServiceImpl.deleteCampaignProduct(1L));
        verify(campaignProductRepository).findById(eq(1L));
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#getDiscountForProduct(Long)}
     */
    @Test
    void testGetDiscountForProduct() {
        // Arrange
        when(campaignProductRepository.findByProductId(Mockito.<Long>any())).thenReturn(new ArrayList<>());

        // Act
        Optional<Long> actualDiscountForProduct = campaignProductServiceImpl.getDiscountForProduct(1L);

        // Assert
        verify(campaignProductRepository).findByProductId(eq(1L));
        assertEquals(0L, actualDiscountForProduct.get().longValue());
        assertTrue(actualDiscountForProduct.isPresent());
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#getDiscountForProduct(Long)}
     */
    @Test
    void testGetDiscountForProduct2() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Name");

        CampaignProduct campaignProduct = new CampaignProduct();
        campaignProduct.setCampaign(campaign);
        campaignProduct.setDeleted(true);
        campaignProduct.setId(1L);
        campaignProduct.setProductId(1L);

        ArrayList<CampaignProduct> campaignProductList = new ArrayList<>();
        campaignProductList.add(campaignProduct);
        when(campaignProductRepository.findByProductId(Mockito.<Long>any())).thenReturn(campaignProductList);

        // Act
        Optional<Long> actualDiscountForProduct = campaignProductServiceImpl.getDiscountForProduct(1L);

        // Assert
        verify(campaignProductRepository).findByProductId(eq(1L));
        assertEquals(3L, actualDiscountForProduct.get().longValue());
        assertTrue(actualDiscountForProduct.isPresent());
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#getDiscountForProduct(Long)}
     */
    @Test
    void testGetDiscountForProduct3() {
        // Arrange
        when(campaignProductRepository.findByProductId(Mockito.<Long>any()))
                .thenThrow(new CampaignNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(CampaignNotFoundException.class, () -> campaignProductServiceImpl.getDiscountForProduct(1L));
        verify(campaignProductRepository).findByProductId(eq(1L));
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#getCampaignProductsByProductId(Long)}
     */
    @Test
    void testGetCampaignProductsByProductId() {
        // Arrange
        when(campaignProductRepository.findByProductIdAndDeletedFalse(Mockito.<Long>any())).thenReturn(new ArrayList<>());

        // Act
        List<CampaignProductDto> actualCampaignProductsByProductId = campaignProductServiceImpl
                .getCampaignProductsByProductId(1L);

        // Assert
        verify(campaignProductRepository).findByProductIdAndDeletedFalse(eq(1L));
        assertTrue(actualCampaignProductsByProductId.isEmpty());
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#getCampaignProductsByProductId(Long)}
     */
    @Test
    void testGetCampaignProductsByProductId2() {
        // Arrange
        when(mapUtil.convertCampaignProductToCampaignProductDto(Mockito.<CampaignProduct>any()))
                .thenThrow(new CampaignNotFoundException("An error occurred"));

        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Name");

        CampaignProduct campaignProduct = new CampaignProduct();
        campaignProduct.setCampaign(campaign);
        campaignProduct.setDeleted(true);
        campaignProduct.setId(1L);
        campaignProduct.setProductId(1L);

        ArrayList<CampaignProduct> campaignProductList = new ArrayList<>();
        campaignProductList.add(campaignProduct);
        when(campaignProductRepository.findByProductIdAndDeletedFalse(Mockito.<Long>any())).thenReturn(campaignProductList);

        // Act and Assert
        assertThrows(CampaignNotFoundException.class, () -> campaignProductServiceImpl.getCampaignProductsByProductId(1L));
        verify(campaignProductRepository).findByProductIdAndDeletedFalse(eq(1L));
        verify(mapUtil).convertCampaignProductToCampaignProductDto(isA(CampaignProduct.class));
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#getCampaignProductsByCampaignId(Long)}
     */
    @Test
    void testGetCampaignProductsByCampaignId() {
        // Arrange
        when(campaignProductRepository.findByCampaignIdAndDeletedFalse(Mockito.<Long>any())).thenReturn(new ArrayList<>());

        // Act
        List<CampaignProductDto> actualCampaignProductsByCampaignId = campaignProductServiceImpl
                .getCampaignProductsByCampaignId(1L);

        // Assert
        verify(campaignProductRepository).findByCampaignIdAndDeletedFalse(eq(1L));
        assertTrue(actualCampaignProductsByCampaignId.isEmpty());
    }

    /**
     * Method under test:
     * {@link CampaignProductServiceImpl#getCampaignProductsByCampaignId(Long)}
     */
    @Test
    void testGetCampaignProductsByCampaignId2() {
        // Arrange
        when(mapUtil.convertCampaignProductToCampaignProductDto(Mockito.<CampaignProduct>any()))
                .thenThrow(new CampaignNotFoundException("An error occurred"));

        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Name");

        CampaignProduct campaignProduct = new CampaignProduct();
        campaignProduct.setCampaign(campaign);
        campaignProduct.setDeleted(true);
        campaignProduct.setId(1L);
        campaignProduct.setProductId(1L);

        ArrayList<CampaignProduct> campaignProductList = new ArrayList<>();
        campaignProductList.add(campaignProduct);
        when(campaignProductRepository.findByCampaignIdAndDeletedFalse(Mockito.<Long>any()))
                .thenReturn(campaignProductList);

        // Act and Assert
        assertThrows(CampaignNotFoundException.class, () -> campaignProductServiceImpl.getCampaignProductsByCampaignId(1L));
        verify(campaignProductRepository).findByCampaignIdAndDeletedFalse(eq(1L));
        verify(mapUtil).convertCampaignProductToCampaignProductDto(isA(CampaignProduct.class));
    }
}
