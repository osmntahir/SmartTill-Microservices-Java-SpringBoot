package com.toyota.usermanagementservice.dto;

import com.toyota.usermanagementservice.domain.Role;
import jakarta.validation.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;



import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class UserDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUserDto() {
        UserDto userDto = new UserDto(1L, "username", "John", "Doe", "john.doe@example.com", Set.of(Role.CASHIER));

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testBlankUsername() {
        UserDto userDto = new UserDto(1L, "", "John", "Doe", "john.doe@example.com", Set.of(Role.CASHIER));

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
        assertEquals("Username must not be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankFirstName() {
        UserDto userDto = new UserDto(1L, "username", "", "Doe", "john.doe@example.com", Set.of(Role.CASHIER));

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
        assertEquals("First name must not be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankLastName() {
        UserDto userDto = new UserDto(1L, "username", "John", "", "john.doe@example.com", Set.of(Role.CASHIER));

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
        assertEquals("Last name must not be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidEmail() {
        UserDto userDto = new UserDto(1L, "username", "John", "Doe", "invalid-email", Set.of(Role.CASHIER));

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
        assertEquals("Email must be valid", violations.iterator().next().getMessage());
    }

    @Test
    void testNoRoles() {
        UserDto userDto = new UserDto(1L, "username", "John", "Doe", "john.doe@example.com", Set.of());

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
        assertEquals("User must have at least one role", violations.iterator().next().getMessage());
    }


    @Test
    void testGetterAndSetters() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("username");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setRoles(new HashSet<>(Set.of(Role.CASHIER)));

        assertEquals(1L, userDto.getId());
        assertEquals("username", userDto.getUsername());
        assertEquals("John", userDto.getFirstName());
        assertEquals("Doe", userDto.getLastName());
        assertEquals("john.doe@example.com", userDto.getEmail());
        assertEquals(Set.of(Role.CASHIER), userDto.getRoles());
    }

    @Test
    void testNoArgsConstructor() {
        UserDto userDto = new UserDto();

        assertNotNull(userDto);
        assertNull(userDto.getId());
        assertNull(userDto.getUsername());
        assertNull(userDto.getFirstName());
        assertNull(userDto.getLastName());
        assertNull(userDto.getEmail());
        assertNull(userDto.getRoles());
    }

    @Test
    void testNotBlankValidations() {
        UserDto userDto = new UserDto();

        Set<String> violations = validateNotBlankFields(userDto);
        assertTrue(violations.contains("Username must not be blank"));
        assertTrue(violations.contains("First name must not be blank"));
        assertTrue(violations.contains("Last name must not be blank"));
        assertTrue(violations.contains("Email must not be blank"));
    }

    private Set<String> validateNotBlankFields(UserDto userDto) {
        Set<String> messages = new HashSet<>();
        validator.validate(userDto).forEach(violation -> {
            messages.add(violation.getMessage());
        });
        return messages;
    }

}
