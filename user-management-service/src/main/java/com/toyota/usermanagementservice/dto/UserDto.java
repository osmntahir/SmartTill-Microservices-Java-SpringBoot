package com.toyota.usermanagementservice.dto;


import com.toyota.usermanagementservice.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * DTO class representing a user in the system.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {
    private Long id;
    @NotBlank(message = "Username must not be blank")
    private String username;
    @NotBlank(message = "First name must not be blank")
    private String firstName;
    @NotBlank(message = "Last name must not be blank")
    private String lastName;
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email must not be blank")
    private String email;
    @Size(min=1, message = "User must have at least one role")
    private Set<Role> roles;
}
