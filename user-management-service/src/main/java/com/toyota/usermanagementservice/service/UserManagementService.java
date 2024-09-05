package com.toyota.usermanagementservice.service;


import com.toyota.usermanagementservice.dto.UserDto;
import com.toyota.usermanagementservice.dto.UserRole;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserManagementService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public UserManagementService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public ResponseEntity<String> createUser(UserDto userDto) {
        UsersResource usersResource = keycloak.realm(realm).users();

        // Ensure the user has at least one role
        if (userDto.getRoles() == null || userDto.getRoles().isEmpty()) {
            return ResponseEntity.badRequest().body("User must have at least one role.");
        }

        // Ensure the user has valid roles
        List<String> validRoles = Arrays.asList("ADMIN", "CASHIER", "MANAGER");
        for (String role : userDto.getRoles()) {
            if (!validRoles.contains(role)) {
                return ResponseEntity.badRequest().body("Invalid role: " + role);
            }
        }

        // Create the UserRepresentation
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setEnabled(true);
        user.setCredentials(Collections.singletonList(createPasswordCredential(userDto.getPassword())));

        // Create the user in Keycloak
        Response response = usersResource.create(user);

        if (response.getStatus() == 201) {
            // Fetch the newly created user by username
            String userId = usersResource.search(userDto.getUsername()).get(0).getId();

            // Assign roles to the user
            RealmResource realmResource = keycloak.realm(realm);
            RoleScopeResource roleScopeResource = realmResource.users().get(userId).roles().realmLevel();

            // Add roles from userDto
            for (String roleName : userDto.getRoles()) {
                RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
                roleScopeResource.add(Collections.singletonList(role));
            }

            return ResponseEntity.ok("User created and roles assigned successfully");
        } else {
            return ResponseEntity.status(response.getStatus()).body("Failed to create user");
        }
    }
    public ResponseEntity<String> updateUser(String userId, UserDto userDto) {
        UsersResource usersResource = keycloak.realm(realm).users();
        UserRepresentation user = usersResource.get(userId).toRepresentation();

        // Ensure the user has valid roles
        List<String> validRoles = Arrays.asList("ADMIN", "CASHIER", "MANAGER");
        if (userDto.getRoles() != null) {
            for (String role : userDto.getRoles()) {
                if (!validRoles.contains(role)) {
                    return ResponseEntity.badRequest().body("Invalid role: " + role);
                }
            }
        }

        // Update user details if provided
        if (userDto.getFirstName() != null) user.setFirstName(userDto.getFirstName());
        if (userDto.getLastName() != null) user.setLastName(userDto.getLastName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        if (userDto.getUsername() != null) user.setUsername(userDto.getUsername());
        if (userDto.getPassword() != null) user.setCredentials(Collections.singletonList(createPasswordCredential(userDto.getPassword())));
        usersResource.get(userId).update(user);

        // Handle role assignments
        RoleScopeResource rolesResource = usersResource.get(userId).roles().realmLevel();

        // Fetch current roles
        List<RoleRepresentation> currentRoles = rolesResource.listAll();
        Set<String> currentRoleNames = currentRoles.stream().map(RoleRepresentation::getName).collect(Collectors.toSet());

        // Determine roles to add and remove, filtering by the manageable roles
        Set<String> newRoles = new HashSet<>(userDto.getRoles());
        List<RoleRepresentation> rolesToAdd = newRoles.stream()
                .filter(validRoles::contains)
                .filter(roleName -> !currentRoleNames.contains(roleName))
                .map(roleName -> keycloak.realm(realm).roles().get(roleName).toRepresentation())
                .collect(Collectors.toList());
        List<RoleRepresentation> rolesToRemove = currentRoles.stream()
                .filter(role -> !newRoles.contains(role.getName()) && validRoles.contains(role.getName()))
                .collect(Collectors.toList());

        // Update roles
        if (!rolesToAdd.isEmpty()) rolesResource.add(rolesToAdd);
        if (!rolesToRemove.isEmpty()) rolesResource.remove(rolesToRemove);

        return ResponseEntity.ok("User updated successfully with role adjustments.");
    }
    
    public ResponseEntity<String> deleteUser(String userId) {
        UsersResource usersResource = keycloak.realm(realm).users();
        UserRepresentation user = usersResource.get(userId).toRepresentation();

        user.setEnabled(false);  // Soft delete by disabling user
        usersResource.get(userId).update(user);

        return ResponseEntity.ok("User disabled successfully");
    }

    public ResponseEntity<String> assignRole(String userId, String roleName) {
        RoleRepresentation role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
        keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(role));

        return ResponseEntity.ok("Role assigned successfully");
    }

    private CredentialRepresentation createPasswordCredential(String password) {
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        return passwordCred;
    }

    public List<UserDto> getUsers() {

        List<UserRepresentation> allUsers = keycloak.realm(realm).users().list();


        return allUsers.stream()
                .filter(UserRepresentation::isEnabled)
                .map(user -> {

                    UserDto userDto = new UserDto();
                    userDto.setUsername(user.getUsername());
                    userDto.setFirstName(user.getFirstName());
                    userDto.setLastName(user.getLastName());
                    userDto.setEmail(user.getEmail());
                    userDto.setId(user.getId());

                    // Fetch roles for each user
                    List<RoleRepresentation> roles = keycloak.realm(realm)
                            .users()
                            .get(user.getId())
                            .roles()
                            .getAll()
                            .getRealmMappings();

                    if (roles != null) {
                        userDto.setRoles(roles.stream()
                                .map(RoleRepresentation::getName)
                                .collect(Collectors.toList()));
                    }

                    return userDto;
                })
                .collect(Collectors.toList());
    }

}
