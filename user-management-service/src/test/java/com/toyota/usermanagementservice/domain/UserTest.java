package com.toyota.usermanagementservice.domain;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the User class.
 */
public class UserTest {

    /**
     * Test for the constructor and getters of the User class.
     */
    @Test
    void testUserConstructorAndGetters() {
        // Test data
        Long id = 1L;
        String username = "TestUser";
        String firstName = "Test";
        String lastName = "User";
        String email = "john@example.com";
        boolean deleted = false;
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        roles.add(Role.CASHIER);

        // Create a user instance
        User user = new User(id, username, firstName, lastName, email, deleted, roles);

        // Assertions
        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(email, user.getEmail());
        assertEquals(deleted, user.isDeleted());
        assertEquals(roles, user.getRoles());
    }

    /**
     * Test for the setters of the User class.
     */
    @Test
    void testUserSetters() {
        // Test data
        Long id = 1L;
        String username = "TestUser";
        String firstName = "Test";
        String lastName = "User";
        String email = "john@example.com";
        boolean deleted = false;
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        roles.add(Role.CASHIER);

        // Create a user instance
        User user = new User();

        // Set values using setters
        user.setId(id);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setDeleted(deleted);
        user.setRoles(roles);

        // Assertions
        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(email, user.getEmail());
        assertEquals(deleted, user.isDeleted());
        assertEquals(roles, user.getRoles());
    }


}
