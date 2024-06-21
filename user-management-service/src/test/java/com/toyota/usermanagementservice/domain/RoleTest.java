package com.toyota.usermanagementservice.domain;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.toyota.usermanagementservice.domain.Role;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleTest {

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Test
    public void testSerialization() throws IOException {
        // Serialize enum to JSON
        String json = objectMapper.writeValueAsString(Role.ADMIN);
        assertEquals("\"ADMIN\"", json); // Ensure JSON string is correct
    }

    @Test
    public void testDeserialization() throws IOException {
        // Deserialize JSON to enum
        String json = "\"CASHIER\"";
        Role role = objectMapper.readValue(json, Role.class);
        assertEquals(Role.CASHIER, role); // Ensure enum value is correct
    }
}
