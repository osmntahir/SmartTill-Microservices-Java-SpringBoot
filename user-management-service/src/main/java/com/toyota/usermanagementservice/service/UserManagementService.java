package com.toyota.usermanagementservice.service;


import com.toyota.usermanagementservice.dto.UserDto;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
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

        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setEnabled(true);

        user.setCredentials(Collections.singletonList(createPasswordCredential(userDto.getPassword())));

        Response response = usersResource.create(user);

        if (response.getStatus() == 201) {
            return ResponseEntity.ok("User created successfully");
        } else {
            return ResponseEntity.status(response.getStatus()).body("Failed to create user");
        }
    }

    public ResponseEntity<String> updateUser(String userId, UserDto userDto) {
        UsersResource usersResource = keycloak.realm(realm).users();
        UserRepresentation user = usersResource.get(userId).toRepresentation();


        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        }
        if (userDto.getPassword() != null) {
            user.setCredentials(Collections.singletonList(createPasswordCredential(userDto.getPassword())));
        }
        usersResource.get(userId).update(user);

        return ResponseEntity.ok("User updated successfully");
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
                    return userDto;
                })
                .collect(Collectors.toList());
    }

}
