package com.toyota.usermanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {
    private String role;

    // Getter ve Setter
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
