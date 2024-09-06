package com.toyota.reportservice.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SoldProductDtoTest {
    /**
     * Methods under test:
     * <ul>
     *   <li>{@link SoldProductDto#equals(Object)}
     *   <li>{@link SoldProductDto#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual() {
        // Arrange
        SoldProductDto soldProductDto = new SoldProductDto();
        SoldProductDto soldProductDto2 = new SoldProductDto();

        // Act and Assert
        assertEquals(soldProductDto, soldProductDto2);
        int expectedHashCodeResult = soldProductDto.hashCode();
        assertEquals(expectedHashCodeResult, soldProductDto2.hashCode());
    }

    /**
     * Methods under test:
     * <ul>
     *   <li>{@link SoldProductDto#equals(Object)}
     *   <li>{@link SoldProductDto#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual2() {
        // Arrange
        SoldProductDto soldProductDto = new SoldProductDto(1L, 1L, "Product Name", 10.0d, 1, 10.0d, 10.0d, 10.0d, 10.0d, 1,
                true);
        SoldProductDto soldProductDto2 = new SoldProductDto(1L, 1L, "Product Name", 10.0d, 1, 10.0d, 10.0d, 10.0d, 10.0d, 1,
                true);

        // Act and Assert
        assertEquals(soldProductDto, soldProductDto2);
        int expectedHashCodeResult = soldProductDto.hashCode();
        assertEquals(expectedHashCodeResult, soldProductDto2.hashCode());
    }

    /**
     * Methods under test:
     * <ul>
     *   <li>{@link SoldProductDto#equals(Object)}
     *   <li>{@link SoldProductDto#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode_whenOtherIsSame_thenReturnEqual() {
        // Arrange
        SoldProductDto soldProductDto = new SoldProductDto();

        // Act and Assert
        assertEquals(soldProductDto, soldProductDto);
        int expectedHashCodeResult = soldProductDto.hashCode();
        assertEquals(expectedHashCodeResult, soldProductDto.hashCode());
    }

    /**
     * Method under test: {@link SoldProductDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual() {
        // Arrange
        SoldProductDto soldProductDto = new SoldProductDto(1L, 1L, "Product Name", 10.0d, 1, 10.0d, 10.0d, 10.0d, 10.0d, 1,
                true);

        // Act and Assert
        assertNotEquals(soldProductDto, new SoldProductDto());
    }

    /**
     * Method under test: {@link SoldProductDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual2() {
        // Arrange
        SoldProductDto soldProductDto = new SoldProductDto();
        soldProductDto.setId(1L);

        // Act and Assert
        assertNotEquals(soldProductDto, new SoldProductDto());
    }

    /**
     * Method under test: {@link SoldProductDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual3() {
        // Arrange
        SoldProductDto soldProductDto = new SoldProductDto();
        soldProductDto.setProductId(1L);

        // Act and Assert
        assertNotEquals(soldProductDto, new SoldProductDto());
    }

    /**
     * Method under test: {@link SoldProductDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual4() {
        // Arrange
        SoldProductDto soldProductDto = new SoldProductDto();
        soldProductDto.setProductName("Product Name");

        // Act and Assert
        assertNotEquals(soldProductDto, new SoldProductDto());
    }

    /**
     * Method under test: {@link SoldProductDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual5() {
        // Arrange
        SoldProductDto soldProductDto = new SoldProductDto();
        soldProductDto.setInventory(1);

        // Act and Assert
        assertNotEquals(soldProductDto, new SoldProductDto());
    }

    /**
     * Method under test: {@link SoldProductDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual6() {
        // Arrange
        SoldProductDto soldProductDto = new SoldProductDto();
        soldProductDto.setDiscount(10.0d);

        // Act and Assert
        assertNotEquals(soldProductDto, new SoldProductDto());
    }

    /**
     * Method under test: {@link SoldProductDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual7() {
        // Arrange
        SoldProductDto soldProductDto = new SoldProductDto();
        soldProductDto.setDiscountAmount(10.0d);

        // Act and Assert
        assertNotEquals(soldProductDto, new SoldProductDto());
    }

    /**
     * Method under test: {@link SoldProductDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual8() {
        // Arrange
        SoldProductDto soldProductDto = new SoldProductDto();
        soldProductDto.setFinalPriceAfterDiscount(10.0d);

        // Act and Assert
        assertNotEquals(soldProductDto, new SoldProductDto());
    }

    /**
     * Method under test: {@link SoldProductDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual9() {
        // Arrange
        SoldProductDto soldProductDto = new SoldProductDto();
        soldProductDto.setTotal(10.0d);

        // Act and Assert
        assertNotEquals(soldProductDto, new SoldProductDto());
    }

    /**
     * Method under test: {@link SoldProductDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual10() {
        // Arrange
        SoldProductDto soldProductDto = new SoldProductDto();
        soldProductDto.setQuantity(1);

        // Act and Assert
        assertNotEquals(soldProductDto, new SoldProductDto());
    }

    /**
     * Method under test: {@link SoldProductDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual11() {
        // Arrange
        SoldProductDto soldProductDto = new SoldProductDto();
        soldProductDto.setDeleted(true);

        // Act and Assert
        assertNotEquals(soldProductDto, new SoldProductDto());
    }

    /**
     * Method under test: {@link SoldProductDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual12() {
        // Arrange
        SoldProductDto soldProductDto = new SoldProductDto();

        SoldProductDto soldProductDto2 = new SoldProductDto();
        soldProductDto2.setId(1L);

        // Act and Assert
        assertNotEquals(soldProductDto, soldProductDto2);
    }

    /**
     * Method under test: {@link SoldProductDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual13() {
        // Arrange
        SoldProductDto soldProductDto = new SoldProductDto();

        SoldProductDto soldProductDto2 = new SoldProductDto();
        soldProductDto2.setProductId(1L);

        // Act and Assert
        assertNotEquals(soldProductDto, soldProductDto2);
    }

    /**
     * Method under test: {@link SoldProductDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual14() {
        // Arrange
        SoldProductDto soldProductDto = new SoldProductDto();

        SoldProductDto soldProductDto2 = new SoldProductDto();
        soldProductDto2.setProductName("Product Name");

        // Act and Assert
        assertNotEquals(soldProductDto, soldProductDto2);
    }

    /**
     * Method under test: {@link SoldProductDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsNull_thenReturnNotEqual() {
        // Arrange, Act and Assert
        assertNotEquals(new SoldProductDto(), null);
    }

    /**
     * Method under test: {@link SoldProductDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsWrongType_thenReturnNotEqual() {
        // Arrange, Act and Assert
        assertNotEquals(new SoldProductDto(), "Different type to SoldProductDto");
    }

    /**
     * Methods under test:
     * <ul>
     *   <li>{@link SoldProductDto#SoldProductDto()}
     *   <li>{@link SoldProductDto#setDeleted(boolean)}
     *   <li>{@link SoldProductDto#setDiscount(double)}
     *   <li>{@link SoldProductDto#setDiscountAmount(double)}
     *   <li>{@link SoldProductDto#setFinalPriceAfterDiscount(double)}
     *   <li>{@link SoldProductDto#setId(Long)}
     *   <li>{@link SoldProductDto#setInventory(int)}
     *   <li>{@link SoldProductDto#setPrice(double)}
     *   <li>{@link SoldProductDto#setProductId(Long)}
     *   <li>{@link SoldProductDto#setProductName(String)}
     *   <li>{@link SoldProductDto#setQuantity(int)}
     *   <li>{@link SoldProductDto#setTotal(double)}
     *   <li>{@link SoldProductDto#toString()}
     *   <li>{@link SoldProductDto#getDiscount()}
     *   <li>{@link SoldProductDto#getDiscountAmount()}
     *   <li>{@link SoldProductDto#getFinalPriceAfterDiscount()}
     *   <li>{@link SoldProductDto#getId()}
     *   <li>{@link SoldProductDto#getInventory()}
     *   <li>{@link SoldProductDto#getPrice()}
     *   <li>{@link SoldProductDto#getProductId()}
     *   <li>{@link SoldProductDto#getProductName()}
     *   <li>{@link SoldProductDto#getQuantity()}
     *   <li>{@link SoldProductDto#getTotal()}
     *   <li>{@link SoldProductDto#isDeleted()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange and Act
        SoldProductDto actualSoldProductDto = new SoldProductDto();
        actualSoldProductDto.setDeleted(true);
        actualSoldProductDto.setDiscount(10.0d);
        actualSoldProductDto.setDiscountAmount(10.0d);
        actualSoldProductDto.setFinalPriceAfterDiscount(10.0d);
        actualSoldProductDto.setId(1L);
        actualSoldProductDto.setInventory(1);
        actualSoldProductDto.setPrice(10.0d);
        actualSoldProductDto.setProductId(1L);
        actualSoldProductDto.setProductName("Product Name");
        actualSoldProductDto.setQuantity(1);
        actualSoldProductDto.setTotal(10.0d);
        String actualToStringResult = actualSoldProductDto.toString();
        double actualDiscount = actualSoldProductDto.getDiscount();
        double actualDiscountAmount = actualSoldProductDto.getDiscountAmount();
        double actualFinalPriceAfterDiscount = actualSoldProductDto.getFinalPriceAfterDiscount();
        Long actualId = actualSoldProductDto.getId();
        int actualInventory = actualSoldProductDto.getInventory();
        double actualPrice = actualSoldProductDto.getPrice();
        Long actualProductId = actualSoldProductDto.getProductId();
        String actualProductName = actualSoldProductDto.getProductName();
        int actualQuantity = actualSoldProductDto.getQuantity();
        double actualTotal = actualSoldProductDto.getTotal();
        boolean actualIsDeletedResult = actualSoldProductDto.isDeleted();

        // Assert that nothing has changed
        assertEquals("Product Name", actualProductName);
        assertEquals(
                "SoldProductDto(id=1, productId=1, productName=Product Name, price=10.0, inventory=1, discount=10.0,"
                        + " discountAmount=10.0, finalPriceAfterDiscount=10.0, total=10.0, quantity=1, deleted=true)",
                actualToStringResult);
        assertEquals(1, actualInventory);
        assertEquals(1, actualQuantity);
        assertEquals(10.0d, actualDiscount);
        assertEquals(10.0d, actualDiscountAmount);
        assertEquals(10.0d, actualFinalPriceAfterDiscount);
        assertEquals(10.0d, actualPrice);
        assertEquals(10.0d, actualTotal);
        assertEquals(1L, actualId.longValue());
        assertEquals(1L, actualProductId.longValue());
        assertTrue(actualIsDeletedResult);
    }

    /**
     * Method under test:
     * {@link SoldProductDto#SoldProductDto(Long, Long, String, double, int, double, double, double, double, int, boolean)}
     */
    @Test
    void testNewSoldProductDto() {
        // Arrange and Act
        SoldProductDto actualSoldProductDto = new SoldProductDto(1L, 1L, "Product Name", 10.0d, 1, 10.0d, 10.0d, 10.0d,
                10.0d, 1, true);

        // Assert
        assertEquals("Product Name", actualSoldProductDto.getProductName());
        assertEquals(1, actualSoldProductDto.getInventory());
        assertEquals(1, actualSoldProductDto.getQuantity());
        assertEquals(10.0d, actualSoldProductDto.getDiscount());
        assertEquals(10.0d, actualSoldProductDto.getDiscountAmount());
        assertEquals(10.0d, actualSoldProductDto.getFinalPriceAfterDiscount());
        assertEquals(10.0d, actualSoldProductDto.getPrice());
        assertEquals(10.0d, actualSoldProductDto.getTotal());
        assertEquals(1L, actualSoldProductDto.getId().longValue());
        assertEquals(1L, actualSoldProductDto.getProductId().longValue());
        assertTrue(actualSoldProductDto.isDeleted());
    }
}
