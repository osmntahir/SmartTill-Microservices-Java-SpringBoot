package com.toyota.usermanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object (DTO) for user data.
 * This class is used to transfer user-related information between layers in the application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    /**
     * The unique identifier of the user.
     */
    private String id;

    private String username;


    private String firstName;


    private String lastName;


    private String email;


    private String password;

    /**
     * A list of roles assigned to the user.
     * Each role defines a set of permissions that the user is granted.
     */
    private List<String> roles;

}
