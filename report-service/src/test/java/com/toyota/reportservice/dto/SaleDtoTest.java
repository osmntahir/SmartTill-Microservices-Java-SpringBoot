package com.toyota.reportservice.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SaleDtoTest {
    /**
     * Methods under test:
     * <ul>
     *   <li>{@link SaleDto#equals(Object)}
     *   <li>{@link SaleDto#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual() {
        // Arrange
        SaleDto saleDto = new SaleDto();
        SaleDto saleDto2 = new SaleDto();

        // Act and Assert
        assertEquals(saleDto, saleDto2);
        int expectedHashCodeResult = saleDto.hashCode();
        assertEquals(expectedHashCodeResult, saleDto2.hashCode());
    }

    /**
     * Methods under test:
     * <ul>
     *   <li>{@link SaleDto#equals(Object)}
     *   <li>{@link SaleDto#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual2() {
        // Arrange
        LocalDateTime date = LocalDate.of(1970, 1, 1).atStartOfDay();
        SaleDto saleDto = new SaleDto(1L, date, PaymentType.CASH, 10.0d, 10.0d, 10.0d, "Cashier Name", new ArrayList<>());
        LocalDateTime date2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        SaleDto saleDto2 = new SaleDto(1L, date2, PaymentType.CASH, 10.0d, 10.0d, 10.0d, "Cashier Name", new ArrayList<>());

        // Act and Assert
        assertEquals(saleDto, saleDto2);
        int expectedHashCodeResult = saleDto.hashCode();
        assertEquals(expectedHashCodeResult, saleDto2.hashCode());
    }

    /**
     * Methods under test:
     * <ul>
     *   <li>{@link SaleDto#equals(Object)}
     *   <li>{@link SaleDto#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode_whenOtherIsSame_thenReturnEqual() {
        // Arrange
        SaleDto saleDto = new SaleDto();

        // Act and Assert
        assertEquals(saleDto, saleDto);
        int expectedHashCodeResult = saleDto.hashCode();
        assertEquals(expectedHashCodeResult, saleDto.hashCode());
    }

    /**
     * Method under test: {@link SaleDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual() {
        // Arrange
        LocalDateTime date = LocalDate.of(1970, 1, 1).atStartOfDay();
        SaleDto saleDto = new SaleDto(1L, date, PaymentType.CASH, 10.0d, 10.0d, 10.0d, "Cashier Name", new ArrayList<>());

        // Act and Assert
        assertNotEquals(saleDto, new SaleDto());
    }

    /**
     * Method under test: {@link SaleDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual2() {
        // Arrange
        SaleDto saleDto = new SaleDto();
        saleDto.setId(1L);

        // Act and Assert
        assertNotEquals(saleDto, new SaleDto());
    }

    /**
     * Method under test: {@link SaleDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual3() {
        // Arrange
        SaleDto saleDto = new SaleDto();
        saleDto.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());

        // Act and Assert
        assertNotEquals(saleDto, new SaleDto());
    }

    /**
     * Method under test: {@link SaleDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual4() {
        // Arrange
        SaleDto saleDto = new SaleDto();
        saleDto.setPaymentType(PaymentType.CASH);

        // Act and Assert
        assertNotEquals(saleDto, new SaleDto());
    }

    /**
     * Method under test: {@link SaleDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual5() {
        // Arrange
        SaleDto saleDto = new SaleDto();
        saleDto.setTotalDiscountAmount(10.0d);

        // Act and Assert
        assertNotEquals(saleDto, new SaleDto());
    }

    /**
     * Method under test: {@link SaleDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual6() {
        // Arrange
        SaleDto saleDto = new SaleDto();
        saleDto.setTotalDiscountedPrice(10.0d);

        // Act and Assert
        assertNotEquals(saleDto, new SaleDto());
    }

    /**
     * Method under test: {@link SaleDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual7() {
        // Arrange
        SaleDto saleDto = new SaleDto();
        saleDto.setCashierName("Cashier Name");

        // Act and Assert
        assertNotEquals(saleDto, new SaleDto());
    }

    /**
     * Method under test: {@link SaleDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual8() {
        // Arrange
        SaleDto saleDto = new SaleDto();
        saleDto.setSoldProducts(new ArrayList<>());

        // Act and Assert
        assertNotEquals(saleDto, new SaleDto());
    }

    /**
     * Method under test: {@link SaleDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual9() {
        // Arrange
        SaleDto saleDto = new SaleDto();

        SaleDto saleDto2 = new SaleDto();
        saleDto2.setId(1L);

        // Act and Assert
        assertNotEquals(saleDto, saleDto2);
    }

    /**
     * Method under test: {@link SaleDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual10() {
        // Arrange
        SaleDto saleDto = new SaleDto();

        SaleDto saleDto2 = new SaleDto();
        saleDto2.setDate(LocalDate.of(1970, 1, 1).atStartOfDay());

        // Act and Assert
        assertNotEquals(saleDto, saleDto2);
    }

    /**
     * Method under test: {@link SaleDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual11() {
        // Arrange
        SaleDto saleDto = new SaleDto();

        SaleDto saleDto2 = new SaleDto();
        saleDto2.setPaymentType(PaymentType.CASH);

        // Act and Assert
        assertNotEquals(saleDto, saleDto2);
    }

    /**
     * Method under test: {@link SaleDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual12() {
        // Arrange
        SaleDto saleDto = new SaleDto();

        SaleDto saleDto2 = new SaleDto();
        saleDto2.setCashierName("Cashier Name");

        // Act and Assert
        assertNotEquals(saleDto, saleDto2);
    }

    /**
     * Method under test: {@link SaleDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual13() {
        // Arrange
        SaleDto saleDto = new SaleDto();

        SaleDto saleDto2 = new SaleDto();
        saleDto2.setSoldProducts(new ArrayList<>());

        // Act and Assert
        assertNotEquals(saleDto, saleDto2);
    }

    /**
     * Method under test: {@link SaleDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsNull_thenReturnNotEqual() {
        // Arrange, Act and Assert
        assertNotEquals(new SaleDto(), null);
    }

    /**
     * Method under test: {@link SaleDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsWrongType_thenReturnNotEqual() {
        // Arrange, Act and Assert
        assertNotEquals(new SaleDto(), "Different type to SaleDto");
    }

    /**
     * Methods under test:
     * <ul>
     *   <li>{@link SaleDto#SaleDto()}
     *   <li>{@link SaleDto#setCashierName(String)}
     *   <li>{@link SaleDto#setDate(LocalDateTime)}
     *   <li>{@link SaleDto#setId(Long)}
     *   <li>{@link SaleDto#setPaymentType(PaymentType)}
     *   <li>{@link SaleDto#setSoldProducts(List)}
     *   <li>{@link SaleDto#setTotalDiscountAmount(double)}
     *   <li>{@link SaleDto#setTotalDiscountedPrice(double)}
     *   <li>{@link SaleDto#setTotalPrice(double)}
     *   <li>{@link SaleDto#toString()}
     *   <li>{@link SaleDto#getCashierName()}
     *   <li>{@link SaleDto#getDate()}
     *   <li>{@link SaleDto#getId()}
     *   <li>{@link SaleDto#getPaymentType()}
     *   <li>{@link SaleDto#getSoldProducts()}
     *   <li>{@link SaleDto#getTotalDiscountAmount()}
     *   <li>{@link SaleDto#getTotalDiscountedPrice()}
     *   <li>{@link SaleDto#getTotalPrice()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange and Act
        SaleDto actualSaleDto = new SaleDto();
        actualSaleDto.setCashierName("Cashier Name");
        LocalDateTime date = LocalDate.of(1970, 1, 1).atStartOfDay();
        actualSaleDto.setDate(date);
        actualSaleDto.setId(1L);
        actualSaleDto.setPaymentType(PaymentType.CASH);
        ArrayList<SoldProductDto> soldProducts = new ArrayList<>();
        actualSaleDto.setSoldProducts(soldProducts);
        actualSaleDto.setTotalDiscountAmount(10.0d);
        actualSaleDto.setTotalDiscountedPrice(10.0d);
        actualSaleDto.setTotalPrice(10.0d);
        String actualToStringResult = actualSaleDto.toString();
        String actualCashierName = actualSaleDto.getCashierName();
        LocalDateTime actualDate = actualSaleDto.getDate();
        Long actualId = actualSaleDto.getId();
        PaymentType actualPaymentType = actualSaleDto.getPaymentType();
        List<SoldProductDto> actualSoldProducts = actualSaleDto.getSoldProducts();
        double actualTotalDiscountAmount = actualSaleDto.getTotalDiscountAmount();
        double actualTotalDiscountedPrice = actualSaleDto.getTotalDiscountedPrice();

        // Assert that nothing has changed
        assertEquals("Cashier Name", actualCashierName);
        assertEquals("SaleDto(id=1, date=1970-01-01T00:00, paymentType=CASH, totalPrice=10.0, totalDiscountAmount=10.0,"
                + " totalDiscountedPrice=10.0, cashierName=Cashier Name, soldProducts=[])", actualToStringResult);
        assertEquals(10.0d, actualTotalDiscountAmount);
        assertEquals(10.0d, actualTotalDiscountedPrice);
        assertEquals(10.0d, actualSaleDto.getTotalPrice());
        assertEquals(1L, actualId.longValue());
        assertEquals(PaymentType.CASH, actualPaymentType);
        assertTrue(actualSoldProducts.isEmpty());
        assertSame(soldProducts, actualSoldProducts);
        assertSame(date, actualDate);
    }

    /**
     * Method under test:
     * {@link SaleDto#SaleDto(Long, LocalDateTime, PaymentType, double, double, double, String, List)}
     */
    @Test
    void testNewSaleDto() {
        // Arrange
        LocalDateTime date = LocalDate.of(1970, 1, 1).atStartOfDay();
        ArrayList<SoldProductDto> soldProducts = new ArrayList<>();

        // Act
        SaleDto actualSaleDto = new SaleDto(1L, date, PaymentType.CASH, 10.0d, 10.0d, 10.0d, "Cashier Name", soldProducts);

        // Assert
        assertEquals("Cashier Name", actualSaleDto.getCashierName());
        assertEquals(10.0d, actualSaleDto.getTotalDiscountAmount());
        assertEquals(10.0d, actualSaleDto.getTotalDiscountedPrice());
        assertEquals(10.0d, actualSaleDto.getTotalPrice());
        assertEquals(1L, actualSaleDto.getId().longValue());
        assertEquals(PaymentType.CASH, actualSaleDto.getPaymentType());
        List<SoldProductDto> soldProducts2 = actualSaleDto.getSoldProducts();
        assertTrue(soldProducts2.isEmpty());
        assertSame(soldProducts, soldProducts2);
        assertSame(date, actualSaleDto.getDate());
    }

    /**
     * Method under test:
     * {@link SaleDto#SaleDto(Long, LocalDateTime, PaymentType, double, double, double, String, List)}
     */
    @Test
    void testNewSaleDto2() {
        // Arrange
        LocalDateTime date = LocalDate.of(1970, 1, 1).atStartOfDay();

        ArrayList<SoldProductDto> soldProducts = new ArrayList<>();
        soldProducts.add(new SoldProductDto());

        // Act
        SaleDto actualSaleDto = new SaleDto(1L, date, PaymentType.CASH, 10.0d, 10.0d, 10.0d, "Cashier Name", soldProducts);

        // Assert
        assertEquals("Cashier Name", actualSaleDto.getCashierName());
        assertEquals(10.0d, actualSaleDto.getTotalDiscountAmount());
        assertEquals(10.0d, actualSaleDto.getTotalDiscountedPrice());
        assertEquals(10.0d, actualSaleDto.getTotalPrice());
        assertEquals(1L, actualSaleDto.getId().longValue());
        assertEquals(PaymentType.CASH, actualSaleDto.getPaymentType());
        assertSame(soldProducts, actualSaleDto.getSoldProducts());
        assertSame(date, actualSaleDto.getDate());
    }

    /**
     * Method under test:
     * {@link SaleDto#SaleDto(Long, LocalDateTime, PaymentType, double, double, double, String, List)}
     */
    @Test
    void testNewSaleDto3() {
        // Arrange
        LocalDateTime date = LocalDate.of(1970, 1, 1).atStartOfDay();

        ArrayList<SoldProductDto> soldProducts = new ArrayList<>();
        soldProducts.add(new SoldProductDto());
        soldProducts.add(new SoldProductDto());

        // Act
        SaleDto actualSaleDto = new SaleDto(1L, date, PaymentType.CASH, 10.0d, 10.0d, 10.0d, "Cashier Name", soldProducts);

        // Assert
        assertEquals("Cashier Name", actualSaleDto.getCashierName());
        assertEquals(10.0d, actualSaleDto.getTotalDiscountAmount());
        assertEquals(10.0d, actualSaleDto.getTotalDiscountedPrice());
        assertEquals(10.0d, actualSaleDto.getTotalPrice());
        assertEquals(1L, actualSaleDto.getId().longValue());
        assertEquals(PaymentType.CASH, actualSaleDto.getPaymentType());
        assertSame(soldProducts, actualSaleDto.getSoldProducts());
        assertSame(date, actualSaleDto.getDate());
    }
}
