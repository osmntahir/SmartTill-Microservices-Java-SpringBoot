package com.toyota.usermanagementservice.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class UserDtoTest {
    /**
     * Methods under test:
     * <ul>
     *   <li>{@link UserDto#equals(Object)}
     *   <li>{@link UserDto#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual() {
        // Arrange
        UserDto userDto = new UserDto();
        UserDto userDto2 = new UserDto();

        // Act and Assert
        assertEquals(userDto, userDto2);
        int expectedHashCodeResult = userDto.hashCode();
        assertEquals(expectedHashCodeResult, userDto2.hashCode());
    }

    /**
     * Methods under test:
     * <ul>
     *   <li>{@link UserDto#equals(Object)}
     *   <li>{@link UserDto#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual2() {
        // Arrange
        UserDto userDto = new UserDto("42", "janedoe", "Jane", "Doe", "jane.doe@example.org", "iloveyou",
                new ArrayList<>());
        UserDto userDto2 = new UserDto("42", "janedoe", "Jane", "Doe", "jane.doe@example.org", "iloveyou",
                new ArrayList<>());

        // Act and Assert
        assertEquals(userDto, userDto2);
        int expectedHashCodeResult = userDto.hashCode();
        assertEquals(expectedHashCodeResult, userDto2.hashCode());
    }

    /**
     * Methods under test:
     * <ul>
     *   <li>{@link UserDto#equals(Object)}
     *   <li>{@link UserDto#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode_whenOtherIsSame_thenReturnEqual() {
        // Arrange
        UserDto userDto = new UserDto();

        // Act and Assert
        assertEquals(userDto, userDto);
        int expectedHashCodeResult = userDto.hashCode();
        assertEquals(expectedHashCodeResult, userDto.hashCode());
    }

    /**
     * Method under test: {@link UserDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual() {
        // Arrange
        UserDto userDto = new UserDto("42", "janedoe", "Jane", "Doe", "jane.doe@example.org", "iloveyou",
                new ArrayList<>());

        // Act and Assert
        assertNotEquals(userDto, new UserDto());
    }

    /**
     * Method under test: {@link UserDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual2() {
        // Arrange
        UserDto userDto = new UserDto();

        // Act and Assert
        assertNotEquals(userDto,
                new UserDto("42", "janedoe", "Jane", "Doe", "jane.doe@example.org", "iloveyou", new ArrayList<>()));
    }

    /**
     * Method under test: {@link UserDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual3() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setUsername("janedoe");

        // Act and Assert
        assertNotEquals(userDto, new UserDto());
    }

    /**
     * Method under test: {@link UserDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual4() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setFirstName("Jane");

        // Act and Assert
        assertNotEquals(userDto, new UserDto());
    }

    /**
     * Method under test: {@link UserDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual5() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setLastName("Doe");

        // Act and Assert
        assertNotEquals(userDto, new UserDto());
    }

    /**
     * Method under test: {@link UserDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual6() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("jane.doe@example.org");

        // Act and Assert
        assertNotEquals(userDto, new UserDto());
    }

    /**
     * Method under test: {@link UserDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual7() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setPassword("iloveyou");

        // Act and Assert
        assertNotEquals(userDto, new UserDto());
    }

    /**
     * Method under test: {@link UserDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual8() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setRoles(new ArrayList<>());

        // Act and Assert
        assertNotEquals(userDto, new UserDto());
    }

    /**
     * Method under test: {@link UserDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual9() {
        // Arrange
        UserDto userDto = new UserDto();

        UserDto userDto2 = new UserDto();
        userDto2.setUsername("janedoe");

        // Act and Assert
        assertNotEquals(userDto, userDto2);
    }

    /**
     * Method under test: {@link UserDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual10() {
        // Arrange
        UserDto userDto = new UserDto();

        UserDto userDto2 = new UserDto();
        userDto2.setFirstName("Jane");

        // Act and Assert
        assertNotEquals(userDto, userDto2);
    }

    /**
     * Method under test: {@link UserDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual11() {
        // Arrange
        UserDto userDto = new UserDto();

        UserDto userDto2 = new UserDto();
        userDto2.setLastName("Doe");

        // Act and Assert
        assertNotEquals(userDto, userDto2);
    }

    /**
     * Method under test: {@link UserDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual12() {
        // Arrange
        UserDto userDto = new UserDto();

        UserDto userDto2 = new UserDto();
        userDto2.setEmail("jane.doe@example.org");

        // Act and Assert
        assertNotEquals(userDto, userDto2);
    }

    /**
     * Method under test: {@link UserDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual13() {
        // Arrange
        UserDto userDto = new UserDto();

        UserDto userDto2 = new UserDto();
        userDto2.setPassword("iloveyou");

        // Act and Assert
        assertNotEquals(userDto, userDto2);
    }

    /**
     * Method under test: {@link UserDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual14() {
        // Arrange
        UserDto userDto = new UserDto();

        UserDto userDto2 = new UserDto();
        userDto2.setRoles(new ArrayList<>());

        // Act and Assert
        assertNotEquals(userDto, userDto2);
    }

    /**
     * Method under test: {@link UserDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsNull_thenReturnNotEqual() {
        // Arrange, Act and Assert
        assertNotEquals(new UserDto(), null);
    }

    /**
     * Method under test: {@link UserDto#equals(Object)}
     */
    @Test
    void testEquals_whenOtherIsWrongType_thenReturnNotEqual() {
        // Arrange, Act and Assert
        assertNotEquals(new UserDto(), "Different type to UserDto");
    }

    /**
     * Methods under test:
     * <ul>
     *   <li>{@link UserDto#UserDto()}
     *   <li>{@link UserDto#setEmail(String)}
     *   <li>{@link UserDto#setFirstName(String)}
     *   <li>{@link UserDto#setId(String)}
     *   <li>{@link UserDto#setLastName(String)}
     *   <li>{@link UserDto#setPassword(String)}
     *   <li>{@link UserDto#setRoles(List)}
     *   <li>{@link UserDto#setUsername(String)}
     *   <li>{@link UserDto#toString()}
     *   <li>{@link UserDto#getEmail()}
     *   <li>{@link UserDto#getFirstName()}
     *   <li>{@link UserDto#getId()}
     *   <li>{@link UserDto#getLastName()}
     *   <li>{@link UserDto#getPassword()}
     *   <li>{@link UserDto#getRoles()}
     *   <li>{@link UserDto#getUsername()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange and Act
        UserDto actualUserDto = new UserDto();
        actualUserDto.setEmail("jane.doe@example.org");
        actualUserDto.setFirstName("Jane");
        actualUserDto.setId("42");
        actualUserDto.setLastName("Doe");
        actualUserDto.setPassword("iloveyou");
        ArrayList<String> roles = new ArrayList<>();
        actualUserDto.setRoles(roles);
        actualUserDto.setUsername("janedoe");
        String actualToStringResult = actualUserDto.toString();
        String actualEmail = actualUserDto.getEmail();
        String actualFirstName = actualUserDto.getFirstName();
        String actualId = actualUserDto.getId();
        String actualLastName = actualUserDto.getLastName();
        String actualPassword = actualUserDto.getPassword();
        List<String> actualRoles = actualUserDto.getRoles();

        // Assert that nothing has changed
        assertEquals("42", actualId);
        assertEquals("Doe", actualLastName);
        assertEquals("Jane", actualFirstName);
        assertEquals(
                "UserDto(id=42, username=janedoe, firstName=Jane, lastName=Doe, email=jane.doe@example.org, password=iloveyou,"
                        + " roles=[])",
                actualToStringResult);
        assertEquals("iloveyou", actualPassword);
        assertEquals("jane.doe@example.org", actualEmail);
        assertEquals("janedoe", actualUserDto.getUsername());
        assertTrue(actualRoles.isEmpty());
        assertSame(roles, actualRoles);
    }

    /**
     * Methods under test:
     * <ul>
     *   <li>
     * {@link UserDto#UserDto(String, String, String, String, String, String, List)}
     *   <li>{@link UserDto#setEmail(String)}
     *   <li>{@link UserDto#setFirstName(String)}
     *   <li>{@link UserDto#setId(String)}
     *   <li>{@link UserDto#setLastName(String)}
     *   <li>{@link UserDto#setPassword(String)}
     *   <li>{@link UserDto#setRoles(List)}
     *   <li>{@link UserDto#setUsername(String)}
     *   <li>{@link UserDto#toString()}
     *   <li>{@link UserDto#getEmail()}
     *   <li>{@link UserDto#getFirstName()}
     *   <li>{@link UserDto#getId()}
     *   <li>{@link UserDto#getLastName()}
     *   <li>{@link UserDto#getPassword()}
     *   <li>{@link UserDto#getRoles()}
     *   <li>{@link UserDto#getUsername()}
     * </ul>
     */
    @Test
    void testGettersAndSetters2() {
        // Arrange and Act
        UserDto actualUserDto = new UserDto("42", "janedoe", "Jane", "Doe", "jane.doe@example.org", "iloveyou",
                new ArrayList<>());
        actualUserDto.setEmail("jane.doe@example.org");
        actualUserDto.setFirstName("Jane");
        actualUserDto.setId("42");
        actualUserDto.setLastName("Doe");
        actualUserDto.setPassword("iloveyou");
        ArrayList<String> roles = new ArrayList<>();
        actualUserDto.setRoles(roles);
        actualUserDto.setUsername("janedoe");
        String actualToStringResult = actualUserDto.toString();
        String actualEmail = actualUserDto.getEmail();
        String actualFirstName = actualUserDto.getFirstName();
        String actualId = actualUserDto.getId();
        String actualLastName = actualUserDto.getLastName();
        String actualPassword = actualUserDto.getPassword();
        List<String> actualRoles = actualUserDto.getRoles();

        // Assert that nothing has changed
        assertEquals("42", actualId);
        assertEquals("Doe", actualLastName);
        assertEquals("Jane", actualFirstName);
        assertEquals(
                "UserDto(id=42, username=janedoe, firstName=Jane, lastName=Doe, email=jane.doe@example.org, password=iloveyou,"
                        + " roles=[])",
                actualToStringResult);
        assertEquals("iloveyou", actualPassword);
        assertEquals("jane.doe@example.org", actualEmail);
        assertEquals("janedoe", actualUserDto.getUsername());
        assertTrue(actualRoles.isEmpty());
        assertSame(roles, actualRoles);
    }
}
