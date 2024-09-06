package com.toyota.reportservice.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class ProductDTOTest {
    /**
     * Methods under test:
     * <ul>
     *   <li>{@link ProductDTO#equals(Object)}
     *   <li>{@link ProductDTO#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setInventory(1);
        productDTO.setName("Name");
        productDTO.setPrice(10.0d);

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setId(1L);
        productDTO2.setInventory(1);
        productDTO2.setName("Name");
        productDTO2.setPrice(10.0d);

        // Act and Assert
        assertEquals(productDTO, productDTO2);
        int expectedHashCodeResult = productDTO.hashCode();
        assertEquals(expectedHashCodeResult, productDTO2.hashCode());
    }

    /**
     * Methods under test:
     * <ul>
     *   <li>{@link ProductDTO#equals(Object)}
     *   <li>{@link ProductDTO#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual2() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(null);
        productDTO.setInventory(1);
        productDTO.setName("Name");
        productDTO.setPrice(10.0d);

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setId(null);
        productDTO2.setInventory(1);
        productDTO2.setName("Name");
        productDTO2.setPrice(10.0d);

        // Act and Assert
        assertEquals(productDTO, productDTO2);
        int expectedHashCodeResult = productDTO.hashCode();
        assertEquals(expectedHashCodeResult, productDTO2.hashCode());
    }

    /**
     * Methods under test:
     * <ul>
     *   <li>{@link ProductDTO#equals(Object)}
     *   <li>{@link ProductDTO#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual3() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setInventory(1);
        productDTO.setName(null);
        productDTO.setPrice(10.0d);

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setId(1L);
        productDTO2.setInventory(1);
        productDTO2.setName(null);
        productDTO2.setPrice(10.0d);

        // Act and Assert
        assertEquals(productDTO, productDTO2);
        int expectedHashCodeResult = productDTO.hashCode();
        assertEquals(expectedHashCodeResult, productDTO2.hashCode());
    }

    /**
     * Methods under test:
     * <ul>
     *   <li>{@link ProductDTO#equals(Object)}
     *   <li>{@link ProductDTO#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode_whenOtherIsSame_thenReturnEqual() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setInventory(1);
        productDTO.setName("Name");
        productDTO.setPrice(10.0d);

        // Act and Assert
        assertEquals(productDTO, productDTO);
        int expectedHashCodeResult = productDTO.hashCode();
        assertEquals(expectedHashCodeResult, productDTO.hashCode());
    }

    /**
     * Method under test: {@link ProductDTO#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(2L);
        productDTO.setInventory(1);
        productDTO.setName("Name");
        productDTO.setPrice(10.0d);

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setId(1L);
        productDTO2.setInventory(1);
        productDTO2.setName("Name");
        productDTO2.setPrice(10.0d);

        // Act and Assert
        assertNotEquals(productDTO, productDTO2);
    }

    /**
     * Method under test: {@link ProductDTO#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual2() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(null);
        productDTO.setInventory(1);
        productDTO.setName("Name");
        productDTO.setPrice(10.0d);

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setId(1L);
        productDTO2.setInventory(1);
        productDTO2.setName("Name");
        productDTO2.setPrice(10.0d);

        // Act and Assert
        assertNotEquals(productDTO, productDTO2);
    }

    /**
     * Method under test: {@link ProductDTO#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual3() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setInventory(3);
        productDTO.setName("Name");
        productDTO.setPrice(10.0d);

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setId(1L);
        productDTO2.setInventory(1);
        productDTO2.setName("Name");
        productDTO2.setPrice(10.0d);

        // Act and Assert
        assertNotEquals(productDTO, productDTO2);
    }

    /**
     * Method under test: {@link ProductDTO#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual4() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setInventory(1);
        productDTO.setName(null);
        productDTO.setPrice(10.0d);

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setId(1L);
        productDTO2.setInventory(1);
        productDTO2.setName("Name");
        productDTO2.setPrice(10.0d);

        // Act and Assert
        assertNotEquals(productDTO, productDTO2);
    }

    /**
     * Method under test: {@link ProductDTO#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual5() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setInventory(1);
        productDTO.setName("com.toyota.reportservice.dto.ProductDTO");
        productDTO.setPrice(10.0d);

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setId(1L);
        productDTO2.setInventory(1);
        productDTO2.setName("Name");
        productDTO2.setPrice(10.0d);

        // Act and Assert
        assertNotEquals(productDTO, productDTO2);
    }

    /**
     * Method under test: {@link ProductDTO#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual6() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setInventory(1);
        productDTO.setName("Name");
        productDTO.setPrice(0.5d);

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setId(1L);
        productDTO2.setInventory(1);
        productDTO2.setName("Name");
        productDTO2.setPrice(10.0d);

        // Act and Assert
        assertNotEquals(productDTO, productDTO2);
    }

    /**
     * Method under test: {@link ProductDTO#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsNull_thenReturnNotEqual() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setInventory(1);
        productDTO.setName("Name");
        productDTO.setPrice(10.0d);

        // Act and Assert
        assertNotEquals(productDTO, null);
    }

    /**
     * Method under test: {@link ProductDTO#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsWrongType_thenReturnNotEqual() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setInventory(1);
        productDTO.setName("Name");
        productDTO.setPrice(10.0d);

        // Act and Assert
        assertNotEquals(productDTO, "Different type to ProductDTO");
    }

    /**
     * Methods under test:
     * <ul>
     *   <li>default or parameterless constructor of {@link ProductDTO}
     *   <li>{@link ProductDTO#setId(Long)}
     *   <li>{@link ProductDTO#setInventory(int)}
     *   <li>{@link ProductDTO#setName(String)}
     *   <li>{@link ProductDTO#setPrice(double)}
     *   <li>{@link ProductDTO#toString()}
     *   <li>{@link ProductDTO#getId()}
     *   <li>{@link ProductDTO#getInventory()}
     *   <li>{@link ProductDTO#getName()}
     *   <li>{@link ProductDTO#getPrice()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange and Act
        ProductDTO actualProductDTO = new ProductDTO();
        actualProductDTO.setId(1L);
        actualProductDTO.setInventory(1);
        actualProductDTO.setName("Name");
        actualProductDTO.setPrice(10.0d);
        String actualToStringResult = actualProductDTO.toString();
        Long actualId = actualProductDTO.getId();
        int actualInventory = actualProductDTO.getInventory();
        String actualName = actualProductDTO.getName();

        // Assert that nothing has changed
        assertEquals("Name", actualName);
        assertEquals("ProductDTO(id=1, name=Name, price=10.0, inventory=1)", actualToStringResult);
        assertEquals(1, actualInventory);
        assertEquals(10.0d, actualProductDTO.getPrice());
        assertEquals(1L, actualId.longValue());
    }
}
