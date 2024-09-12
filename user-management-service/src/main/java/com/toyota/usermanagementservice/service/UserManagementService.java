package com.toyota.usermanagementservice.service;

import com.toyota.usermanagementservice.dto.UserDto;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users in Keycloak.
 * This service provides functionalities to create, update, delete users, as well as assign and unassign roles.
 * It communicates with Keycloak's Admin REST API to perform operations.
 */
@Service
public class UserManagementService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    /**
     * Constructs a new {@link UserManagementService} with the provided Keycloak instance.
     *
     * @param keycloak the Keycloak client used to interact with the Keycloak server
     */
    public UserManagementService(@Lazy Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    /**
     * Creates a new user in Keycloak.
     * The user must have at least one valid role (ADMIN, CASHIER, or MANAGER).
     *
     * @param userDto the user data to create
     * @return a {@link ResponseEntity} containing the result of the creation process
     */
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

    /**
     * Updates an existing user in Keycloak.
     * The user data such as name, email, and roles can be updated.
     *
     * @param userId the ID of the user to update
     * @param userDto the new user data
     * @return a {@link ResponseEntity} containing the result of the update process
     */
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

        if (userDto.getRoles() == null ) {
            return ResponseEntity.ok("User updated successfully.");
        }
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

    /**
     * Soft deletes a user by disabling them in Keycloak.
     *
     * @param userId the ID of the user to disable
     * @return a {@link ResponseEntity} containing the result of the deletion process
     */
    public ResponseEntity<String> deleteUser(String userId) {
        UsersResource usersResource = keycloak.realm(realm).users();
        UserRepresentation user = usersResource.get(userId).toRepresentation();

        user.setEnabled(false);  // Soft delete by disabling user
        usersResource.get(userId).update(user);

        return ResponseEntity.ok("User disabled successfully");
    }

    /**
     * Assigns a role to a user in Keycloak.
     *
     * @param userId the ID of the user
     * @param roleName the name of the role to assign
     * @return a {@link ResponseEntity} containing the result of the role assignment
     */
    public ResponseEntity<String> assignRole(String userId, String roleName) {
        UserResource userResource = keycloak.realm(realm).users().get(userId);
        if (userResource == null) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        RoleRepresentation role;
        try {
            role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body("Role not found.");
        }

        try {
            userResource.roles().realmLevel().add(Collections.singletonList(role));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to assign role.");
        }

        return ResponseEntity.ok("Role assigned successfully.");
    }

    /**
     * Unassigns a role from a user in Keycloak.
     *
     * @param userId the ID of the user
     * @param roleName the name of the role to unassign
     * @return a {@link ResponseEntity} containing the result of the role unassignment
     */
    public ResponseEntity<String> unassignRole(String userId, String roleName) {
        try {
            RoleRepresentation role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
            keycloak.realm(realm).users().get(userId).roles().realmLevel().remove(Collections.singletonList(role));
            return ResponseEntity.ok("Role unassigned successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to unassign role: " + e.getMessage());
        }
    }

    /**
     * Retrieves all users in the system and filters out disabled users.
     *
     * @return a list of {@link UserDto} representing all enabled users
     */
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

    /**
     * Creates a password credential for Keycloak.
     *
     * @param password the user's password
     * @return a {@link CredentialRepresentation} for the user's password
     */
    private CredentialRepresentation createPasswordCredential(String password) {
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        return passwordCred;
    }
}
