package com.toyota.saleservice.service;

import com.toyota.productservice.domain.Product;
import com.toyota.saleservice.dao.CampaignProductRepository;
import com.toyota.saleservice.dao.CampaignRepository;
import com.toyota.productservice.dao.ProductRepository;
import com.toyota.saleservice.domain.Campaign;
import com.toyota.saleservice.domain.CampaignProduct;
import com.toyota.saleservice.dto.CampaignProductDto;
import com.toyota.saleservice.exception.CampaignNotFoundException;
import com.toyota.saleservice.exception.CampaignProductNotFoundException;
import com.toyota.saleservice.exception.ProductNotFoundException;
import com.toyota.saleservice.service.common.MapUtil;
import com.toyota.saleservice.service.impl.CampaignProductServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CampaignProductServiceImplTest {

    @InjectMocks
    private CampaignProductServiceImpl campaignProductService;

    @Mock
    private CampaignProductRepository campaignProductRepository;

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MapUtil mapUtil;

    private CampaignProductDto campaignProductDto;
    private CampaignProduct campaignProduct;
    private Campaign campaign;

    @BeforeEach
    void setUp() {
        campaignProductDto = new CampaignProductDto();
        campaignProductDto.setId(1L);
        campaignProductDto.setCampaignId(1L);
        campaignProductDto.setProductId(1L);

        campaignProduct = new CampaignProduct();
        campaignProduct.setId(1L);
        campaignProduct.setCampaign(new Campaign());
        campaignProduct.setProduct(new Product());

        campaign = new Campaign();
        campaign.setId(1L);
        campaign.setDiscount(20);
    }

    @Test
    void testGetAllCampaignProducts() {
        when(campaignProductRepository.findAll()).thenReturn(List.of(campaignProduct));
        when(mapUtil.convertCampaignProductToCampaignProductDto(any(CampaignProduct.class))).thenReturn(campaignProductDto);

        List<CampaignProductDto> result = campaignProductService.getAllCampaignProducts();

        assertEquals(1, result.size());
        verify(campaignProductRepository, times(1)).findAll();
        verify(mapUtil, times(1)).convertCampaignProductToCampaignProductDto(any(CampaignProduct.class));
    }

    @Test
    void testAddCampaignProduct() {
        when(campaignRepository.existsById(anyLong())).thenReturn(true);
        when(productRepository.existsById(anyLong())).thenReturn(true);
        when(mapUtil.convertCampaignProductDtoToCampaignProduct(any(CampaignProductDto.class))).thenReturn(campaignProduct);
        when(campaignProductRepository.save(any(CampaignProduct.class))).thenReturn(campaignProduct);
        when(mapUtil.convertCampaignProductToCampaignProductDto(any(CampaignProduct.class))).thenReturn(campaignProductDto);

        CampaignProductDto result = campaignProductService.addCampaignProduct(campaignProductDto);

        assertNotNull(result);
        assertEquals(campaignProductDto.getId(), result.getId());
        verify(campaignRepository, times(1)).existsById(anyLong());
        verify(productRepository, times(1)).existsById(anyLong());
        verify(campaignProductRepository, times(1)).save(any(CampaignProduct.class));
        verify(mapUtil, times(1)).convertCampaignProductToCampaignProductDto(any(CampaignProduct.class));
    }

    @Test
    void testAddCampaignProductCampaignNotFound() {
        when(campaignRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(CampaignNotFoundException.class, () -> campaignProductService.addCampaignProduct(campaignProductDto));
        verify(campaignRepository, times(1)).existsById(anyLong());
    }

    @Test
    void testAddCampaignProductProductNotFound() {
        when(campaignRepository.existsById(anyLong())).thenReturn(true);
        when(productRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> campaignProductService.addCampaignProduct(campaignProductDto));
        verify(campaignRepository, times(1)).existsById(anyLong());
        verify(productRepository, times(1)).existsById(anyLong());
    }

    @Test
    void testUpdateCampaignProduct() {
        when(campaignProductRepository.findById(anyLong())).thenReturn(Optional.of(campaignProduct));
        when(campaignRepository.existsById(anyLong())).thenReturn(true);
        when(productRepository.existsById(anyLong())).thenReturn(true);
        when(mapUtil.convertCampaignProductDtoToCampaignProduct(any(CampaignProductDto.class))).thenReturn(campaignProduct);
        when(campaignProductRepository.save(any(CampaignProduct.class))).thenReturn(campaignProduct);
        when(mapUtil.convertCampaignProductToCampaignProductDto(any(CampaignProduct.class))).thenReturn(campaignProductDto);

        CampaignProductDto result = campaignProductService.updateCampaignProduct(1L, campaignProductDto);

        assertNotNull(result);
        assertEquals(campaignProductDto.getId(), result.getId());
        verify(campaignProductRepository, times(1)).findById(anyLong());
        verify(campaignRepository, times(1)).existsById(anyLong());
        verify(productRepository, times(1)).existsById(anyLong());
        verify(campaignProductRepository, times(1)).save(any(CampaignProduct.class));
    }

    @Test
    void testUpdateCampaignProductNotFound() {
        when(campaignProductRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> campaignProductService.updateCampaignProduct(1L, campaignProductDto));
        verify(campaignProductRepository, times(1)).findById(anyLong());
    }

    @Test
    void testUpdateCampaignProductWithInactiveStatus() {
        campaignProductDto.setDeleted(true);
        when(campaignProductRepository.findById(anyLong())).thenReturn(Optional.of(campaignProduct));

        assertThrows(IllegalArgumentException.class, () -> campaignProductService.updateCampaignProduct(1L, campaignProductDto));
        verify(campaignProductRepository, times(1)).findById(anyLong());
    }

    @Test
    void testDeleteCampaignProduct() {
        // Initialize the campaignProduct object
        campaignProduct.setDeleted(false);

        // Mock the repository to return the campaignProduct
        when(campaignProductRepository.findById(anyLong())).thenReturn(Optional.of(campaignProduct));
        // Mock the save method to return the modified campaignProduct
        when(campaignProductRepository.save(any(CampaignProduct.class))).thenAnswer(invocation -> invocation.getArgument(0));
        // Mock the mapUtil to return the campaignProductDto with deleted = true
        campaignProductDto.setDeleted(true);
        when(mapUtil.convertCampaignProductToCampaignProductDto(any(CampaignProduct.class))).thenReturn(campaignProductDto);

        // Call the service method
        CampaignProductDto result = campaignProductService.deleteCampaignProduct(1L);

        // Verify that the result is not null
        assertNotNull(result);
        // Verify that the result's deleted field is true
        assertTrue(result.isDeleted());

        // Verify that campaignProductRepository.findById() was called once
        verify(campaignProductRepository, times(1)).findById(anyLong());
        // Verify that campaignProductRepository.save() was called once
        verify(campaignProductRepository, times(1)).save(any(CampaignProduct.class));

        // Check if the deleted field of campaignProduct is actually true
        assertTrue(campaignProduct.isDeleted());
    }


    @Test
    void testDeleteCampaignProductNotFound() {
        when(campaignProductRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CampaignProductNotFoundException.class, () -> campaignProductService.deleteCampaignProduct(1L));
        verify(campaignProductRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetDiscountForProduct() {
        campaignProduct.setCampaign(campaign);
        when(campaignProductRepository.findByProductId(anyLong())).thenReturn(List.of(campaignProduct));

        Optional<Long> discount = campaignProductService.getDiscountForProduct(1L);

        assertTrue(discount.isPresent());
        assertEquals(20L, discount.get());
        verify(campaignProductRepository, times(1)).findByProductId(anyLong());
    }

    @Test
    void testGetDiscountForProductNoCampaignProducts() {
        when(campaignProductRepository.findByProductId(anyLong())).thenReturn(List.of());

        Optional<Long> discount = campaignProductService.getDiscountForProduct(1L);

        assertTrue(discount.isPresent());
        assertEquals(0L, discount.get());
        verify(campaignProductRepository, times(1)).findByProductId(anyLong());
    }

    @Test
    void testGetCampaignProductsByProductId() {
        when(campaignProductRepository.findByProductIdAndDeletedFalse(anyLong())).thenReturn(List.of(campaignProduct));
        when(mapUtil.convertCampaignProductToCampaignProductDto(any(CampaignProduct.class))).thenReturn(campaignProductDto);

        List<CampaignProductDto> result = campaignProductService.getCampaignProductsByProductId(1L);

        assertEquals(1, result.size());
        verify(campaignProductRepository, times(1)).findByProductIdAndDeletedFalse(anyLong());
        verify(mapUtil, times(1)).convertCampaignProductToCampaignProductDto(any(CampaignProduct.class));
    }

    @Test
    void testGetCampaignProductsByCampaignId() {
        when(campaignProductRepository.findByCampaignIdAndDeletedFalse(anyLong())).thenReturn(List.of(campaignProduct));
        when(mapUtil.convertCampaignProductToCampaignProductDto(any(CampaignProduct.class))).thenReturn(campaignProductDto);

        List<CampaignProductDto> result = campaignProductService.getCampaignProductsByCampaignId(1L);

        assertEquals(1, result.size());
        verify(campaignProductRepository, times(1)).findByCampaignIdAndDeletedFalse(anyLong());
        verify(mapUtil, times(1)).convertCampaignProductToCampaignProductDto(any(CampaignProduct.class));
    }

    @Test
    void testUpdateCampaignProduct_CampaignNotFound() {
        when(campaignProductRepository.findById(anyLong())).thenReturn(Optional.of(campaignProduct));
        when(campaignRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            campaignProductService.updateCampaignProduct(1L, campaignProductDto);
        });

        assertEquals("Campaign not found with id: " + campaignProductDto.getCampaignId(), exception.getMessage());
        verify(campaignProductRepository, times(1)).findById(anyLong());
        verify(campaignProductRepository, never()).save(any(CampaignProduct.class));
    }

    @Test
    void testUpdateCampaignProduct_ProductNotFound() {
        when(campaignProductRepository.findById(anyLong())).thenReturn(Optional.of(campaignProduct));
        when(campaignRepository.existsById(anyLong())).thenReturn(true);
        when(productRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            campaignProductService.updateCampaignProduct(1L, campaignProductDto);
        });

        assertEquals("Product not found with id: " + campaignProductDto.getProductId(), exception.getMessage());
        verify(campaignProductRepository, times(1)).findById(anyLong());
        verify(campaignProductRepository, never()).save(any(CampaignProduct.class));
    }

    @Test
    void testUpdateCampaignProduct_InactiveStatus() {
        campaignProductDto.setDeleted(true);

        when(campaignProductRepository.findById(anyLong())).thenReturn(Optional.of(campaignProduct));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            campaignProductService.updateCampaignProduct(1L, campaignProductDto);
        });

        assertEquals("Cannot update Campaign Product with inactive status", exception.getMessage());
        verify(campaignProductRepository, times(1)).findById(anyLong());
        verify(campaignProductRepository, never()).save(any(CampaignProduct.class));
    }

    @Test
    void testUpdateCampaignProduct_NotFound() {
        when(campaignProductRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            campaignProductService.updateCampaignProduct(1L, campaignProductDto);
        });

        assertEquals("Campaign Product not found with id: 1", exception.getMessage());
        verify(campaignProductRepository, times(1)).findById(anyLong());
        verify(campaignProductRepository, never()).save(any(CampaignProduct.class));
    }
}

