package com.toyota.saleservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.toyota.saleservice.dao.CampaignRepository;
import com.toyota.saleservice.domain.Campaign;
import com.toyota.saleservice.dto.CampaignDto;
import com.toyota.saleservice.exception.CampaignAlreadyExistsException;
import com.toyota.saleservice.exception.CampaignNotFoundException;
import com.toyota.saleservice.service.common.MapUtil;

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

@ContextConfiguration(classes = {CampaignServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CampaignServiceImplTest {
    @MockBean
    private CampaignRepository campaignRepository;

    @Autowired
    private CampaignServiceImpl campaignServiceImpl;

    @MockBean
    private MapUtil mapUtil;

    /**
     * Method under test:
     * {@link CampaignServiceImpl#getCampaignsFiltered(int, int, String, Double, Double, boolean, List, String)}
     */
    @Test
    void testGetCampaignsFiltered() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Getting campaigns with filters");

        ArrayList<Campaign> content = new ArrayList<>();
        content.add(campaign);
        PageImpl<Campaign> pageImpl = new PageImpl<>(content);
        when(campaignRepository.getCampaignsFiltered(Mockito.<String>any(), Mockito.<Double>any(), Mockito.<Double>any(),
                anyBoolean(), Mockito.<Pageable>any())).thenReturn(pageImpl);
        when(mapUtil.convertCampaignToCampaignDto(Mockito.<Campaign>any()))
                .thenThrow(new CampaignAlreadyExistsException("An error occurred"));

        // Act and Assert
        assertThrows(CampaignAlreadyExistsException.class,
                () -> campaignServiceImpl.getCampaignsFiltered(1, 3, "Name", 10.0d, 10.0d, true, new ArrayList<>(), "asc"));
        verify(campaignRepository).getCampaignsFiltered(eq("Name"), eq(10.0d), eq(10.0d), eq(true), isA(Pageable.class));
        verify(mapUtil).convertCampaignToCampaignDto(isA(Campaign.class));
    }

    /**
     * Method under test:
     * {@link CampaignServiceImpl#getCampaignsFiltered(int, int, String, Double, Double, boolean, List, String)}
     */
    @Test
    void testGetCampaignsFiltered2() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Getting campaigns with filters");

        ArrayList<Campaign> content = new ArrayList<>();
        content.add(campaign);
        PageImpl<Campaign> pageImpl = new PageImpl<>(content);
        when(campaignRepository.getCampaignsFiltered(Mockito.<String>any(), Mockito.<Double>any(), Mockito.<Double>any(),
                anyBoolean(), Mockito.<Pageable>any())).thenReturn(pageImpl);
        when(mapUtil.convertCampaignToCampaignDto(Mockito.<Campaign>any()))
                .thenThrow(new CampaignAlreadyExistsException("An error occurred"));

        ArrayList<String> sortBy = new ArrayList<>();
        sortBy.add("Getting campaigns with filters");

        // Act and Assert
        assertThrows(CampaignAlreadyExistsException.class,
                () -> campaignServiceImpl.getCampaignsFiltered(1, 3, "Name", 10.0d, 10.0d, true, sortBy, "asc"));
        verify(campaignRepository).getCampaignsFiltered(eq("Name"), eq(10.0d), eq(10.0d), eq(true), isA(Pageable.class));
        verify(mapUtil).convertCampaignToCampaignDto(isA(Campaign.class));
    }

    /**
     * Method under test:
     * {@link CampaignServiceImpl#getCampaignsFiltered(int, int, String, Double, Double, boolean, List, String)}
     */
    @Test
    void testGetCampaignsFiltered3() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Getting campaigns with filters");

        ArrayList<Campaign> content = new ArrayList<>();
        content.add(campaign);
        PageImpl<Campaign> pageImpl = new PageImpl<>(content);
        when(campaignRepository.getCampaignsFiltered(Mockito.<String>any(), Mockito.<Double>any(), Mockito.<Double>any(),
                anyBoolean(), Mockito.<Pageable>any())).thenReturn(pageImpl);
        when(mapUtil.convertCampaignToCampaignDto(Mockito.<Campaign>any()))
                .thenThrow(new CampaignAlreadyExistsException("An error occurred"));

        ArrayList<String> sortBy = new ArrayList<>();
        sortBy.add("Asc");
        sortBy.add("Getting campaigns with filters");

        // Act and Assert
        assertThrows(CampaignAlreadyExistsException.class,
                () -> campaignServiceImpl.getCampaignsFiltered(1, 3, "Name", 10.0d, 10.0d, true, sortBy, "asc"));
        verify(campaignRepository).getCampaignsFiltered(eq("Name"), eq(10.0d), eq(10.0d), eq(true), isA(Pageable.class));
        verify(mapUtil).convertCampaignToCampaignDto(isA(Campaign.class));
    }

    /**
     * Method under test:
     * {@link CampaignServiceImpl#getCampaignsFiltered(int, int, String, Double, Double, boolean, List, String)}
     */
    @Test
    void testGetCampaignsFiltered4() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Getting campaigns with filters");

        ArrayList<Campaign> content = new ArrayList<>();
        content.add(campaign);
        PageImpl<Campaign> pageImpl = new PageImpl<>(content);
        when(campaignRepository.getCampaignsFiltered(Mockito.<String>any(), Mockito.<Double>any(), Mockito.<Double>any(),
                anyBoolean(), Mockito.<Pageable>any())).thenReturn(pageImpl);
        when(mapUtil.convertCampaignToCampaignDto(Mockito.<Campaign>any()))
                .thenThrow(new CampaignAlreadyExistsException("An error occurred"));

        ArrayList<String> sortBy = new ArrayList<>();
        sortBy.add("Getting campaigns with filters");

        // Act and Assert
        assertThrows(CampaignAlreadyExistsException.class,
                () -> campaignServiceImpl.getCampaignsFiltered(1, 3, "Name", 10.0d, 10.0d, true, sortBy, "desc"));
        verify(campaignRepository).getCampaignsFiltered(eq("Name"), eq(10.0d), eq(10.0d), eq(true), isA(Pageable.class));
        verify(mapUtil).convertCampaignToCampaignDto(isA(Campaign.class));
    }

    /**
     * Method under test: {@link CampaignServiceImpl#addCampaign(CampaignDto)}
     */
    @Test
    void testAddCampaign() {
        // Arrange
        when(campaignRepository.existsByName(Mockito.<String>any())).thenReturn(true);

        // Act and Assert
        assertThrows(CampaignAlreadyExistsException.class, () -> campaignServiceImpl.addCampaign(new CampaignDto()));
        verify(campaignRepository).existsByName(isNull());
    }

    /**
     * Method under test: {@link CampaignServiceImpl#addCampaign(CampaignDto)}
     */
    @Test
    void testAddCampaign2() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Name");
        when(campaignRepository.existsByName(Mockito.<String>any())).thenReturn(false);
        when(campaignRepository.save(Mockito.<Campaign>any())).thenReturn(campaign);

        Campaign campaign2 = new Campaign();
        campaign2.setCampaignProducts(new ArrayList<>());
        campaign2.setDeleted(true);
        campaign2.setDescription("The characteristics of someone or something");
        campaign2.setDiscount(3L);
        campaign2.setId(1L);
        campaign2.setName("Name");
        when(mapUtil.convertCampaignDtoToCampaign(Mockito.<CampaignDto>any())).thenReturn(campaign2);
        CampaignDto campaignDto = new CampaignDto();
        when(mapUtil.convertCampaignToCampaignDto(Mockito.<Campaign>any())).thenReturn(campaignDto);

        // Act
        CampaignDto actualAddCampaignResult = campaignServiceImpl.addCampaign(new CampaignDto());

        // Assert
        verify(campaignRepository).existsByName(isNull());
        verify(mapUtil).convertCampaignDtoToCampaign(isA(CampaignDto.class));
        verify(mapUtil).convertCampaignToCampaignDto(isA(Campaign.class));
        verify(campaignRepository).save(isA(Campaign.class));
        assertSame(campaignDto, actualAddCampaignResult);
    }

    /**
     * Method under test: {@link CampaignServiceImpl#addCampaign(CampaignDto)}
     */
    @Test
    void testAddCampaign3() {
        // Arrange
        when(campaignRepository.existsByName(Mockito.<String>any())).thenReturn(false);
        when(mapUtil.convertCampaignDtoToCampaign(Mockito.<CampaignDto>any()))
                .thenThrow(new CampaignNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(CampaignNotFoundException.class, () -> campaignServiceImpl.addCampaign(new CampaignDto()));
        verify(campaignRepository).existsByName(isNull());
        verify(mapUtil).convertCampaignDtoToCampaign(isA(CampaignDto.class));
    }

    /**
     * Method under test:
     * {@link CampaignServiceImpl#updateCampaign(Long, CampaignDto)}
     */
    @Test
    void testUpdateCampaign() {
        // Arrange
        when(campaignRepository.existsByName(Mockito.<String>any())).thenReturn(true);

        // Act and Assert
        assertThrows(CampaignAlreadyExistsException.class, () -> campaignServiceImpl.updateCampaign(1L, new CampaignDto()));
        verify(campaignRepository).existsByName(isNull());
    }

    /**
     * Method under test:
     * {@link CampaignServiceImpl#updateCampaign(Long, CampaignDto)}
     */
    @Test
    void testUpdateCampaign2() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Name");
        Optional<Campaign> ofResult = Optional.of(campaign);

        Campaign campaign2 = new Campaign();
        campaign2.setCampaignProducts(new ArrayList<>());
        campaign2.setDeleted(true);
        campaign2.setDescription("The characteristics of someone or something");
        campaign2.setDiscount(3L);
        campaign2.setId(1L);
        campaign2.setName("Name");
        when(campaignRepository.save(Mockito.<Campaign>any())).thenReturn(campaign2);
        when(campaignRepository.existsByName(Mockito.<String>any())).thenReturn(false);
        when(campaignRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        CampaignDto campaignDto = new CampaignDto();
        when(mapUtil.convertCampaignToCampaignDto(Mockito.<Campaign>any())).thenReturn(campaignDto);

        // Act
        CampaignDto actualUpdateCampaignResult = campaignServiceImpl.updateCampaign(1L, new CampaignDto());

        // Assert
        verify(campaignRepository).existsByName(isNull());
        verify(mapUtil).convertCampaignToCampaignDto(isA(Campaign.class));
        verify(campaignRepository).findById(eq(1L));
        verify(campaignRepository).save(isA(Campaign.class));
        assertSame(campaignDto, actualUpdateCampaignResult);
    }

    /**
     * Method under test:
     * {@link CampaignServiceImpl#updateCampaign(Long, CampaignDto)}
     */
    @Test
    void testUpdateCampaign3() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Name");
        Optional<Campaign> ofResult = Optional.of(campaign);

        Campaign campaign2 = new Campaign();
        campaign2.setCampaignProducts(new ArrayList<>());
        campaign2.setDeleted(true);
        campaign2.setDescription("The characteristics of someone or something");
        campaign2.setDiscount(3L);
        campaign2.setId(1L);
        campaign2.setName("Name");
        when(campaignRepository.save(Mockito.<Campaign>any())).thenReturn(campaign2);
        when(campaignRepository.existsByName(Mockito.<String>any())).thenReturn(false);
        when(campaignRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(mapUtil.convertCampaignToCampaignDto(Mockito.<Campaign>any()))
                .thenThrow(new CampaignAlreadyExistsException("An error occurred"));

        // Act and Assert
        assertThrows(CampaignAlreadyExistsException.class, () -> campaignServiceImpl.updateCampaign(1L, new CampaignDto()));
        verify(campaignRepository).existsByName(isNull());
        verify(mapUtil).convertCampaignToCampaignDto(isA(Campaign.class));
        verify(campaignRepository).findById(eq(1L));
        verify(campaignRepository).save(isA(Campaign.class));
    }

    /**
     * Method under test:
     * {@link CampaignServiceImpl#updateCampaign(Long, CampaignDto)}
     */
    @Test
    void testUpdateCampaign4() {
        // Arrange
        when(campaignRepository.existsByName(Mockito.<String>any())).thenReturn(false);
        Optional<Campaign> emptyResult = Optional.empty();
        when(campaignRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(CampaignNotFoundException.class, () -> campaignServiceImpl.updateCampaign(1L, new CampaignDto()));
        verify(campaignRepository).existsByName(isNull());
        verify(campaignRepository).findById(eq(1L));
    }

    /**
     * Method under test:
     * {@link CampaignServiceImpl#updateCampaign(Long, CampaignDto)}
     */
    @Test
    void testUpdateCampaign5() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Name");
        Optional<Campaign> ofResult = Optional.of(campaign);
        when(campaignRepository.existsByName(Mockito.<String>any())).thenReturn(false);
        when(campaignRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> campaignServiceImpl.updateCampaign(1L, new CampaignDto(1L, "updating campaign with name : {} ",
                        "The characteristics of someone or something", 3L, new ArrayList<>(), true)));
        verify(campaignRepository).existsByName(eq("updating campaign with name : {} "));
        verify(campaignRepository).findById(eq(1L));
    }

    /**
     * Method under test: {@link CampaignServiceImpl#deleteCampaign(Long)}
     */
    @Test
    void testDeleteCampaign() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Name");
        Optional<Campaign> ofResult = Optional.of(campaign);

        Campaign campaign2 = new Campaign();
        campaign2.setCampaignProducts(new ArrayList<>());
        campaign2.setDeleted(true);
        campaign2.setDescription("The characteristics of someone or something");
        campaign2.setDiscount(3L);
        campaign2.setId(1L);
        campaign2.setName("Name");
        when(campaignRepository.save(Mockito.<Campaign>any())).thenReturn(campaign2);
        when(campaignRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        CampaignDto campaignDto = new CampaignDto();
        when(mapUtil.convertCampaignToCampaignDto(Mockito.<Campaign>any())).thenReturn(campaignDto);

        // Act
        CampaignDto actualDeleteCampaignResult = campaignServiceImpl.deleteCampaign(1L);

        // Assert
        verify(mapUtil).convertCampaignToCampaignDto(isA(Campaign.class));
        verify(campaignRepository).findById(eq(1L));
        verify(campaignRepository).save(isA(Campaign.class));
        assertSame(campaignDto, actualDeleteCampaignResult);
    }

    /**
     * Method under test: {@link CampaignServiceImpl#deleteCampaign(Long)}
     */
    @Test
    void testDeleteCampaign2() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setCampaignProducts(new ArrayList<>());
        campaign.setDeleted(true);
        campaign.setDescription("The characteristics of someone or something");
        campaign.setDiscount(3L);
        campaign.setId(1L);
        campaign.setName("Name");
        Optional<Campaign> ofResult = Optional.of(campaign);

        Campaign campaign2 = new Campaign();
        campaign2.setCampaignProducts(new ArrayList<>());
        campaign2.setDeleted(true);
        campaign2.setDescription("The characteristics of someone or something");
        campaign2.setDiscount(3L);
        campaign2.setId(1L);
        campaign2.setName("Name");
        when(campaignRepository.save(Mockito.<Campaign>any())).thenReturn(campaign2);
        when(campaignRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(mapUtil.convertCampaignToCampaignDto(Mockito.<Campaign>any()))
                .thenThrow(new CampaignAlreadyExistsException("An error occurred"));

        // Act and Assert
        assertThrows(CampaignAlreadyExistsException.class, () -> campaignServiceImpl.deleteCampaign(1L));
        verify(mapUtil).convertCampaignToCampaignDto(isA(Campaign.class));
        verify(campaignRepository).findById(eq(1L));
        verify(campaignRepository).save(isA(Campaign.class));
    }

    /**
     * Method under test: {@link CampaignServiceImpl#deleteCampaign(Long)}
     */
    @Test
    void testDeleteCampaign3() {
        // Arrange
        Optional<Campaign> emptyResult = Optional.empty();
        when(campaignRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(CampaignNotFoundException.class, () -> campaignServiceImpl.deleteCampaign(1L));
        verify(campaignRepository).findById(eq(1L));
    }
}
