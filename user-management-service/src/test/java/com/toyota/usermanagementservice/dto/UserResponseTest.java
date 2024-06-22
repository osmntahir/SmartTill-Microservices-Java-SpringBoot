package com.toyota.usermanagementservice.dto;

import com.toyota.usermanagementservice.domain.Role;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserResponseTest {

    @Test
    void testGettersAndSetters() {
        // Test data
        Long id = 1L;
        String firstname = "John";
        String lastname = "Doe";
        String username = "johndoe";
        String email = "john.doe@example.com";
        Set<Role> roles = new HashSet<>(Set.of(Role.CASHIER));

        // Create UserResponse instance
        UserResponse userResponse = new UserResponse();
        userResponse.setId(id);
        userResponse.setFirstname(firstname);
        userResponse.setLastname(lastname);
        userResponse.setUsername(username);
        userResponse.setEmail(email);
        userResponse.setRole(roles);

        // Assert getters
        assertEquals(id, userResponse.getId());
        assertEquals(firstname, userResponse.getFirstname());
        assertEquals(lastname, userResponse.getLastname());
        assertEquals(username, userResponse.getUsername());
        assertEquals(email, userResponse.getEmail());
        assertEquals(roles, userResponse.getRole());
    }

    @Test
    void testNoArgsConstructor() {
        UserResponse userResponse = new UserResponse();

        // Assert that all fields initialized to null or default values
        assertNull(userResponse.getId());
        assertNull(userResponse.getFirstname());
        assertNull(userResponse.getLastname());
        assertNull(userResponse.getUsername());
        assertNull(userResponse.getEmail());
        assertNull(userResponse.getRole());
    }

    @Test
    void testAllArgsConstructor() {
        // Test data
        Long id = 1L;
        String firstname = "John";
        String lastname = "Doe";
        String username = "johndoe";
        String email = "john.doe@example.com";
        Set<Role> roles = new HashSet<>(Set.of(Role.CASHIER));

        // Create UserResponse instance using constructor with arguments
        UserResponse userResponse = new UserResponse( firstname, lastname, username, email, roles);
        userResponse.setId(id);
        // Assert all fields are correctly initialized
        assertEquals(id, userResponse.getId());
        assertEquals(firstname, userResponse.getFirstname());
        assertEquals(lastname, userResponse.getLastname());
        assertEquals(username, userResponse.getUsername());
        assertEquals(email, userResponse.getEmail());
        assertEquals(roles, userResponse.getRole());
    }
}
