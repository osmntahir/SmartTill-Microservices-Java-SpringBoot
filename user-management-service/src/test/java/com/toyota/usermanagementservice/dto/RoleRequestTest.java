package com.toyota.usermanagementservice.dto;

import com.toyota.usermanagementservice.domain.Role;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoleRequestTest {

    @Test
    public void testGetterAndSetter() {

        String roleValue = "ADMIN";


        RoleRequest request = new RoleRequest();
        request.setRole(roleValue);


        assertEquals(roleValue, request.getRole());
    }

    @Test
    public void testEnumConversion() {

        String roleValue = "CASHIER";


        RoleRequest request = new RoleRequest();
        request.setRole(roleValue);


        assertEquals(Role.CASHIER, Role.forValue(roleValue));
    }
}
