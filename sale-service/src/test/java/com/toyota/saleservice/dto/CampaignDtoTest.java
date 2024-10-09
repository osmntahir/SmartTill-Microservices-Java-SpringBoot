//package com.toyota.saleservice.dto;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//
//class CampaignDtoTest {
//
//    @Test
//    void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual() {
//        // Arrange
//        CampaignDto campaignDto = new CampaignDto();
//        CampaignDto campaignDto2 = new CampaignDto();
//
//        // Act and Assert
//        assertEquals(campaignDto, campaignDto2);
//        int expectedHashCodeResult = campaignDto.hashCode();
//        assertEquals(expectedHashCodeResult, campaignDto2.hashCode());
//    }
//
//
//    @Test
//    void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual2() {
//        // Arrange
//        CampaignDto campaignDto = new CampaignDto(1L, "Name", "The characteristics of someone or something", 3L,
//                new ArrayList<>(), true);
//        CampaignDto campaignDto2 = new CampaignDto(1L, "Name", "The characteristics of someone or something", 3L,
//                new ArrayList<>(), true);
//
//        // Act and Assert
//        assertEquals(campaignDto, campaignDto2);
//        int expectedHashCodeResult = campaignDto.hashCode();
//        assertEquals(expectedHashCodeResult, campaignDto2.hashCode());
//    }
//
//
//    @Test
//    void testEqualsAndHashCode_whenOtherIsSame_thenReturnEqual() {
//        // Arrange
//        CampaignDto campaignDto = new CampaignDto();
//
//        // Act and Assert
//        assertEquals(campaignDto, campaignDto);
//        int expectedHashCodeResult = campaignDto.hashCode();
//        assertEquals(expectedHashCodeResult, campaignDto.hashCode());
//    }
//
//
//    @Test
//    void testEquals_whenOtherIsDifferent_thenReturnNotEqual() {
//        // Arrange
//        CampaignDto campaignDto = new CampaignDto(1L, "Name", "The characteristics of someone or something", 3L,
//                new ArrayList<>(), true);
//
//        // Act and Assert
//        assertNotEquals(campaignDto, new CampaignDto());
//    }
//
//
//    @Test
//    void testEquals_whenOtherIsDifferent_thenReturnNotEqual2() {
//        // Arrange
//        CampaignDto campaignDto = new CampaignDto();
//        campaignDto.setId(1L);
//
//        // Act and Assert
//        assertNotEquals(campaignDto, new CampaignDto());
//    }
//
//
//    @Test
//    void testEquals_whenOtherIsDifferent_thenReturnNotEqual3() {
//        // Arrange
//        CampaignDto campaignDto = new CampaignDto();
//        campaignDto.setName("Name");
//
//        // Act and Assert
//        assertNotEquals(campaignDto, new CampaignDto());
//    }
//
//
//    @Test
//    void testEquals_whenOtherIsDifferent_thenReturnNotEqual4() {
//        // Arrange
//        CampaignDto campaignDto = new CampaignDto();
//        campaignDto.setDescription("The characteristics of someone or something");
//
//        // Act and Assert
//        assertNotEquals(campaignDto, new CampaignDto());
//    }
//
//
//    @Test
//    void testEquals_whenOtherIsDifferent_thenReturnNotEqual5() {
//        // Arrange
//        CampaignDto campaignDto = new CampaignDto();
//        campaignDto.setCampaignProducts(new ArrayList<>());
//
//        // Act and Assert
//        assertNotEquals(campaignDto, new CampaignDto());
//    }
//
//
//    @Test
//    void testEquals_whenOtherIsDifferent_thenReturnNotEqual6() {
//        // Arrange
//        CampaignDto campaignDto = new CampaignDto();
//        campaignDto.setDeleted(true);
//
//        // Act and Assert
//        assertNotEquals(campaignDto, new CampaignDto());
//    }
//
//
//    @Test
//    void testEquals_whenOtherIsDifferent_thenReturnNotEqual7() {
//        // Arrange
//        CampaignDto campaignDto = new CampaignDto();
//
//        CampaignDto campaignDto2 = new CampaignDto();
//        campaignDto2.setId(1L);
//
//        // Act and Assert
//        assertNotEquals(campaignDto, campaignDto2);
//    }
//
//    @Test
//    void testEquals_whenOtherIsDifferent_thenReturnNotEqual8() {
//        // Arrange
//        CampaignDto campaignDto = new CampaignDto();
//
//        CampaignDto campaignDto2 = new CampaignDto();
//        campaignDto2.setName("Name");
//
//        // Act and Assert
//        assertNotEquals(campaignDto, campaignDto2);
//    }
//
//
//    @Test
//    void testEquals_whenOtherIsDifferent_thenReturnNotEqual9() {
//        // Arrange
//        CampaignDto campaignDto = new CampaignDto();
//
//        CampaignDto campaignDto2 = new CampaignDto();
//        campaignDto2.setDescription("The characteristics of someone or something");
//
//        // Act and Assert
//        assertNotEquals(campaignDto, campaignDto2);
//    }
//
//    @Test
//    void testEquals_whenOtherIsDifferent_thenReturnNotEqual10() {
//        // Arrange
//        CampaignDto campaignDto = new CampaignDto();
//
//        CampaignDto campaignDto2 = new CampaignDto();
//        campaignDto2.setCampaignProducts(new ArrayList<>());
//
//        // Act and Assert
//        assertNotEquals(campaignDto, campaignDto2);
//    }
//
//
//    @Test
//    void testEquals_whenOtherIsNull_thenReturnNotEqual() {
//        // Arrange, Act and Assert
//        assertNotEquals(new CampaignDto(), null);
//    }
//
//
//    @Test
//    void testEquals_whenOtherIsWrongType_thenReturnNotEqual() {
//        // Arrange, Act and Assert
//        assertNotEquals(new CampaignDto(), "Different type to CampaignDto");
//    }
//
//    @Test
//    void testGettersAndSetters() {
//        // Arrange
//        CampaignDto campaignDto = new CampaignDto();
//        ArrayList<CampaignProduct> campaignProducts = new ArrayList<>();
//
//        // Act
//        campaignDto.setCampaignProducts(campaignProducts);
//        campaignDto.setDeleted(true);
//        campaignDto.setDescription("The characteristics of someone or something");
//        campaignDto.setDiscount(3L);
//        campaignDto.setId(1L);
//        campaignDto.setName("Name");
//        String actualToStringResult = campaignDto.toString();
//        List<CampaignProduct> actualCampaignProducts = campaignDto.getCampaignProducts();
//        String actualDescription = campaignDto.getDescription();
//        long actualDiscount = campaignDto.getDiscount();
//        Long actualId = campaignDto.getId();
//        String actualName = campaignDto.getName();
//        boolean actualIsDeletedResult = campaignDto.isDeleted();
//
//        // Assert that nothing has changed
//        assertEquals("CampaignDto(id=1, name=Name, description=The characteristics of someone or something, discount=3,"
//                + " campaignProducts=[], deleted=true)", actualToStringResult);
//        assertEquals("Name", actualName);
//        assertEquals("The characteristics of someone or something", actualDescription);
//        assertEquals(1L, actualId.longValue());
//        assertEquals(3L, actualDiscount);
//        assertTrue(actualIsDeletedResult);
//        assertTrue(actualCampaignProducts.isEmpty());
//        assertSame(campaignProducts, actualCampaignProducts);
//    }
//}
