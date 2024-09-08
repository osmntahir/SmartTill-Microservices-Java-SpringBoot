package com.toyota.usermanagementservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.toyota.usermanagementservice.dto.UserDto;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import org.jboss.resteasy.core.ServerResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.MappingsRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserManagementService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserManagementServiceTest {
    @MockBean
    private Keycloak keycloak;

    @Autowired
    private UserManagementService userManagementService;

    /**
     * Method under test: {@link UserManagementService#createUser(UserDto)}
     */
    @Test
    void testCreateUser() {
        // Arrange
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(mock(UsersResource.class));
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act
        ResponseEntity<String> actualCreateUserResult = userManagementService.createUser(new UserDto());

        // Assert
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).users();
        HttpStatusCode statusCode = actualCreateUserResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("User must have at least one role.", actualCreateUserResult.getBody());
        assertEquals(400, actualCreateUserResult.getStatusCodeValue());
        assertEquals(HttpStatus.BAD_REQUEST, statusCode);
        assertTrue(actualCreateUserResult.hasBody());
        assertTrue(actualCreateUserResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserManagementService#createUser(UserDto)}
     */
    @Test
    void testCreateUser2() {
        // Arrange
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenThrow(new NotFoundException());
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> userManagementService.createUser(new UserDto()));
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).users();
    }

    /**
     * Method under test: {@link UserManagementService#createUser(UserDto)}
     */
    @Test
    void testCreateUser3() {
        // Arrange
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(mock(UsersResource.class));
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act
        ResponseEntity<String> actualCreateUserResult = userManagementService
                .createUser(new UserDto("42", "janedoe", "Jane", "Doe", "jane.doe@example.org", "iloveyou", new ArrayList<>()));

        // Assert
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).users();
        HttpStatusCode statusCode = actualCreateUserResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("User must have at least one role.", actualCreateUserResult.getBody());
        assertEquals(400, actualCreateUserResult.getStatusCodeValue());
        assertEquals(HttpStatus.BAD_REQUEST, statusCode);
        assertTrue(actualCreateUserResult.hasBody());
        assertTrue(actualCreateUserResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserManagementService#createUser(UserDto)}
     */
    @Test
    void testCreateUser4() {
        // Arrange
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(mock(UsersResource.class));
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        ArrayList<String> roles = new ArrayList<>();
        roles.add("User must have at least one role.");

        // Act
        ResponseEntity<String> actualCreateUserResult = userManagementService
                .createUser(new UserDto("42", "janedoe", "Jane", "Doe", "jane.doe@example.org", "iloveyou", roles));

        // Assert
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).users();
        HttpStatusCode statusCode = actualCreateUserResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("Invalid role: User must have at least one role.", actualCreateUserResult.getBody());
        assertEquals(400, actualCreateUserResult.getStatusCodeValue());
        assertEquals(HttpStatus.BAD_REQUEST, statusCode);
        assertTrue(actualCreateUserResult.hasBody());
        assertTrue(actualCreateUserResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserManagementService#createUser(UserDto)}
     */
    @Test
    void testCreateUser5() {
        // Arrange
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(mock(UsersResource.class));
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        ArrayList<String> roles = new ArrayList<>();
        roles.add("CASHIER");
        roles.add("User must have at least one role.");

        // Act
        ResponseEntity<String> actualCreateUserResult = userManagementService
                .createUser(new UserDto("42", "janedoe", "Jane", "Doe", "jane.doe@example.org", "iloveyou", roles));

        // Assert
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).users();
        HttpStatusCode statusCode = actualCreateUserResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("Invalid role: User must have at least one role.", actualCreateUserResult.getBody());
        assertEquals(400, actualCreateUserResult.getStatusCodeValue());
        assertEquals(HttpStatus.BAD_REQUEST, statusCode);
        assertTrue(actualCreateUserResult.hasBody());
        assertTrue(actualCreateUserResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserManagementService#createUser(UserDto)}
     */
    @Test
    void testCreateUser6() {
        // Arrange
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.create(Mockito.<UserRepresentation>any())).thenReturn(new ServerResponse());
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        ArrayList<String> roles = new ArrayList<>();
        roles.add("CASHIER");
        roles.add("ADMIN");

        // Act
        ResponseEntity<String> actualCreateUserResult = userManagementService
                .createUser(new UserDto("42", "janedoe", "Jane", "Doe", "jane.doe@example.org", "iloveyou", roles));

        // Assert
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).users();
        verify(usersResource).create(isA(UserRepresentation.class));
        HttpStatusCode statusCode = actualCreateUserResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("Failed to create user", actualCreateUserResult.getBody());
        assertEquals(200, actualCreateUserResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualCreateUserResult.hasBody());
        assertTrue(actualCreateUserResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserManagementService#updateUser(String, UserDto)}
     */
    @Test
    void testUpdateUser() {
        // Arrange
        UserResource userResource = mock(UserResource.class);
        when(userResource.toRepresentation()).thenReturn(new UserRepresentation());
        doNothing().when(userResource).update(Mockito.<UserRepresentation>any());
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(Mockito.<String>any())).thenReturn(userResource);
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act
        ResponseEntity<String> actualUpdateUserResult = userManagementService.updateUser("42", new UserDto());

        // Assert
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).users();
        verify(userResource).toRepresentation();
        verify(userResource).update(isA(UserRepresentation.class));
        verify(usersResource, atLeast(1)).get(eq("42"));
        HttpStatusCode statusCode = actualUpdateUserResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("User updated successfully.", actualUpdateUserResult.getBody());
        assertEquals(200, actualUpdateUserResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualUpdateUserResult.hasBody());
        assertTrue(actualUpdateUserResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserManagementService#updateUser(String, UserDto)}
     */
    @Test
    void testUpdateUser2() {
        // Arrange
        UserResource userResource = mock(UserResource.class);
        when(userResource.toRepresentation()).thenThrow(new NotFoundException());
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(Mockito.<String>any())).thenReturn(userResource);
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> userManagementService.updateUser("42", new UserDto()));
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).users();
        verify(userResource).toRepresentation();
        verify(usersResource).get(eq("42"));
    }

    /**
     * Method under test: {@link UserManagementService#updateUser(String, UserDto)}
     */
    @Test
    void testUpdateUser3() {
        // Arrange
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        when(roleScopeResource.listAll()).thenReturn(new ArrayList<>());
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        UserResource userResource = mock(UserResource.class);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(userResource.toRepresentation()).thenReturn(new UserRepresentation());
        doNothing().when(userResource).update(Mockito.<UserRepresentation>any());
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(Mockito.<String>any())).thenReturn(userResource);
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act
        ResponseEntity<String> actualUpdateUserResult = userManagementService.updateUser("42",
                new UserDto("42", "janedoe", "Jane", "Doe", "jane.doe@example.org", "iloveyou", new ArrayList<>()));

        // Assert
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).users();
        verify(roleMappingResource).realmLevel();
        verify(roleScopeResource).listAll();
        verify(userResource).roles();
        verify(userResource).toRepresentation();
        verify(userResource).update(isA(UserRepresentation.class));
        verify(usersResource, atLeast(1)).get(eq("42"));
        HttpStatusCode statusCode = actualUpdateUserResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("User updated successfully with role adjustments.", actualUpdateUserResult.getBody());
        assertEquals(200, actualUpdateUserResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualUpdateUserResult.hasBody());
        assertTrue(actualUpdateUserResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserManagementService#updateUser(String, UserDto)}
     */
    @Test
    void testUpdateUser4() {
        // Arrange
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        when(roleScopeResource.listAll()).thenThrow(new NotFoundException());
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        UserResource userResource = mock(UserResource.class);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(userResource.toRepresentation()).thenReturn(new UserRepresentation());
        doNothing().when(userResource).update(Mockito.<UserRepresentation>any());
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(Mockito.<String>any())).thenReturn(userResource);
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> userManagementService.updateUser("42",
                new UserDto("42", "janedoe", "Jane", "Doe", "jane.doe@example.org", "iloveyou", new ArrayList<>())));
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).users();
        verify(roleMappingResource).realmLevel();
        verify(roleScopeResource).listAll();
        verify(userResource).roles();
        verify(userResource).toRepresentation();
        verify(userResource).update(isA(UserRepresentation.class));
        verify(usersResource, atLeast(1)).get(eq("42"));
    }

    /**
     * Method under test: {@link UserManagementService#updateUser(String, UserDto)}
     */
    @Test
    void testUpdateUser5() {
        // Arrange
        ArrayList<RoleRepresentation> roleRepresentationList = new ArrayList<>();
        roleRepresentationList.add(new RoleRepresentation("ADMIN", "The characteristics of someone or something", true));
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        doNothing().when(roleScopeResource).remove(Mockito.<List<RoleRepresentation>>any());
        when(roleScopeResource.listAll()).thenReturn(roleRepresentationList);
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        UserResource userResource = mock(UserResource.class);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(userResource.toRepresentation()).thenReturn(new UserRepresentation());
        doNothing().when(userResource).update(Mockito.<UserRepresentation>any());
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(Mockito.<String>any())).thenReturn(userResource);
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act
        ResponseEntity<String> actualUpdateUserResult = userManagementService.updateUser("42",
                new UserDto("42", "janedoe", "Jane", "Doe", "jane.doe@example.org", "iloveyou", new ArrayList<>()));

        // Assert
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).users();
        verify(roleMappingResource).realmLevel();
        verify(roleScopeResource).listAll();
        verify(roleScopeResource).remove(isA(List.class));
        verify(userResource).roles();
        verify(userResource).toRepresentation();
        verify(userResource).update(isA(UserRepresentation.class));
        verify(usersResource, atLeast(1)).get(eq("42"));
        HttpStatusCode statusCode = actualUpdateUserResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("User updated successfully with role adjustments.", actualUpdateUserResult.getBody());
        assertEquals(200, actualUpdateUserResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualUpdateUserResult.hasBody());
        assertTrue(actualUpdateUserResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserManagementService#updateUser(String, UserDto)}
     */
    @Test
    void testUpdateUser6() {
        // Arrange
        ArrayList<RoleRepresentation> roleRepresentationList = new ArrayList<>();
        roleRepresentationList.add(new RoleRepresentation("ADMIN", "The characteristics of someone or something", true));
        roleRepresentationList.add(new RoleRepresentation("ADMIN", "The characteristics of someone or something", true));
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        doNothing().when(roleScopeResource).remove(Mockito.<List<RoleRepresentation>>any());
        when(roleScopeResource.listAll()).thenReturn(roleRepresentationList);
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        UserResource userResource = mock(UserResource.class);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(userResource.toRepresentation()).thenReturn(new UserRepresentation());
        doNothing().when(userResource).update(Mockito.<UserRepresentation>any());
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(Mockito.<String>any())).thenReturn(userResource);
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act
        ResponseEntity<String> actualUpdateUserResult = userManagementService.updateUser("42",
                new UserDto("42", "janedoe", "Jane", "Doe", "jane.doe@example.org", "iloveyou", new ArrayList<>()));

        // Assert
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).users();
        verify(roleMappingResource).realmLevel();
        verify(roleScopeResource).listAll();
        verify(roleScopeResource).remove(isA(List.class));
        verify(userResource).roles();
        verify(userResource).toRepresentation();
        verify(userResource).update(isA(UserRepresentation.class));
        verify(usersResource, atLeast(1)).get(eq("42"));
        HttpStatusCode statusCode = actualUpdateUserResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("User updated successfully with role adjustments.", actualUpdateUserResult.getBody());
        assertEquals(200, actualUpdateUserResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualUpdateUserResult.hasBody());
        assertTrue(actualUpdateUserResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserManagementService#updateUser(String, UserDto)}
     */
    @Test
    void testUpdateUser7() {
        // Arrange
        ArrayList<RoleRepresentation> roleRepresentationList = new ArrayList<>();
        roleRepresentationList.add(new RoleRepresentation("password", "The characteristics of someone or something", true));
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        when(roleScopeResource.listAll()).thenReturn(roleRepresentationList);
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        UserResource userResource = mock(UserResource.class);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(userResource.toRepresentation()).thenReturn(new UserRepresentation());
        doNothing().when(userResource).update(Mockito.<UserRepresentation>any());
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(Mockito.<String>any())).thenReturn(userResource);
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act
        ResponseEntity<String> actualUpdateUserResult = userManagementService.updateUser("42",
                new UserDto("42", "janedoe", "Jane", "Doe", "jane.doe@example.org", "iloveyou", new ArrayList<>()));

        // Assert
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).users();
        verify(roleMappingResource).realmLevel();
        verify(roleScopeResource).listAll();
        verify(userResource).roles();
        verify(userResource).toRepresentation();
        verify(userResource).update(isA(UserRepresentation.class));
        verify(usersResource, atLeast(1)).get(eq("42"));
        HttpStatusCode statusCode = actualUpdateUserResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("User updated successfully with role adjustments.", actualUpdateUserResult.getBody());
        assertEquals(200, actualUpdateUserResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualUpdateUserResult.hasBody());
        assertTrue(actualUpdateUserResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserManagementService#updateUser(String, UserDto)}
     */
    @Test
    void testUpdateUser8() {
        // Arrange
        ArrayList<RoleRepresentation> roleRepresentationList = new ArrayList<>();
        roleRepresentationList.add(new RoleRepresentation("ADMIN", "The characteristics of someone or something", true));
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        when(roleScopeResource.listAll()).thenReturn(roleRepresentationList);
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        UserResource userResource = mock(UserResource.class);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(userResource.toRepresentation()).thenReturn(new UserRepresentation());
        doNothing().when(userResource).update(Mockito.<UserRepresentation>any());
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(Mockito.<String>any())).thenReturn(userResource);
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        ArrayList<String> roles = new ArrayList<>();
        roles.add("ADMIN");

        // Act
        ResponseEntity<String> actualUpdateUserResult = userManagementService.updateUser("42",
                new UserDto("42", "janedoe", "Jane", "Doe", "jane.doe@example.org", "iloveyou", roles));

        // Assert
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).users();
        verify(roleMappingResource).realmLevel();
        verify(roleScopeResource).listAll();
        verify(userResource).roles();
        verify(userResource).toRepresentation();
        verify(userResource).update(isA(UserRepresentation.class));
        verify(usersResource, atLeast(1)).get(eq("42"));
        HttpStatusCode statusCode = actualUpdateUserResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("User updated successfully with role adjustments.", actualUpdateUserResult.getBody());
        assertEquals(200, actualUpdateUserResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualUpdateUserResult.hasBody());
        assertTrue(actualUpdateUserResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserManagementService#updateUser(String, UserDto)}
     */
    @Test
    void testUpdateUser9() {
        // Arrange
        ArrayList<RoleRepresentation> roleRepresentationList = new ArrayList<>();
        roleRepresentationList.add(new RoleRepresentation("ADMIN", "The characteristics of someone or something", true));
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        doNothing().when(roleScopeResource).add(Mockito.<List<RoleRepresentation>>any());
        when(roleScopeResource.listAll()).thenReturn(roleRepresentationList);
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        UserResource userResource = mock(UserResource.class);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(userResource.toRepresentation()).thenReturn(new UserRepresentation());
        doNothing().when(userResource).update(Mockito.<UserRepresentation>any());
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(Mockito.<String>any())).thenReturn(userResource);
        RoleResource roleResource = mock(RoleResource.class);
        when(roleResource.toRepresentation())
                .thenReturn(new RoleRepresentation("Name", "The characteristics of someone or something", true));
        RolesResource rolesResource = mock(RolesResource.class);
        when(rolesResource.get(Mockito.<String>any())).thenReturn(roleResource);
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.roles()).thenReturn(rolesResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        ArrayList<String> roles = new ArrayList<>();
        roles.add("CASHIER");
        roles.add("ADMIN");

        // Act
        ResponseEntity<String> actualUpdateUserResult = userManagementService.updateUser("42",
                new UserDto("42", "janedoe", "Jane", "Doe", "jane.doe@example.org", "iloveyou", roles));

        // Assert
        verify(keycloak, atLeast(1)).realm(eq("${keycloak.realm}"));
        verify(realmResource).roles();
        verify(realmResource).users();
        verify(roleMappingResource).realmLevel();
        verify(roleResource).toRepresentation();
        verify(roleScopeResource).add(isA(List.class));
        verify(roleScopeResource).listAll();
        verify(rolesResource).get(eq("CASHIER"));
        verify(userResource).roles();
        verify(userResource).toRepresentation();
        verify(userResource).update(isA(UserRepresentation.class));
        verify(usersResource, atLeast(1)).get(eq("42"));
        HttpStatusCode statusCode = actualUpdateUserResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("User updated successfully with role adjustments.", actualUpdateUserResult.getBody());
        assertEquals(200, actualUpdateUserResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualUpdateUserResult.hasBody());
        assertTrue(actualUpdateUserResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserManagementService#updateUser(String, UserDto)}
     */
    @Test
    void testUpdateUser10() {
        // Arrange
        ArrayList<RoleRepresentation> roleRepresentationList = new ArrayList<>();
        roleRepresentationList.add(new RoleRepresentation("ADMIN", "The characteristics of someone or something", true));
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        doThrow(new NotFoundException()).when(roleScopeResource).add(Mockito.<List<RoleRepresentation>>any());
        when(roleScopeResource.listAll()).thenReturn(roleRepresentationList);
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        UserResource userResource = mock(UserResource.class);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(userResource.toRepresentation()).thenReturn(new UserRepresentation());
        doNothing().when(userResource).update(Mockito.<UserRepresentation>any());
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(Mockito.<String>any())).thenReturn(userResource);
        RoleResource roleResource = mock(RoleResource.class);
        when(roleResource.toRepresentation())
                .thenReturn(new RoleRepresentation("Name", "The characteristics of someone or something", true));
        RolesResource rolesResource = mock(RolesResource.class);
        when(rolesResource.get(Mockito.<String>any())).thenReturn(roleResource);
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.roles()).thenReturn(rolesResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        ArrayList<String> roles = new ArrayList<>();
        roles.add("CASHIER");
        roles.add("ADMIN");

        // Act and Assert
        assertThrows(NotFoundException.class, () -> userManagementService.updateUser("42",
                new UserDto("42", "janedoe", "Jane", "Doe", "jane.doe@example.org", "iloveyou", roles)));
        verify(keycloak, atLeast(1)).realm(eq("${keycloak.realm}"));
        verify(realmResource).roles();
        verify(realmResource).users();
        verify(roleMappingResource).realmLevel();
        verify(roleResource).toRepresentation();
        verify(roleScopeResource).add(isA(List.class));
        verify(roleScopeResource).listAll();
        verify(rolesResource).get(eq("CASHIER"));
        verify(userResource).roles();
        verify(userResource).toRepresentation();
        verify(userResource).update(isA(UserRepresentation.class));
        verify(usersResource, atLeast(1)).get(eq("42"));
    }

    /**
     * Method under test: {@link UserManagementService#deleteUser(String)}
     */
    @Test
    void testDeleteUser() {
        // Arrange
        UserResource userResource = mock(UserResource.class);
        doNothing().when(userResource).update(Mockito.<UserRepresentation>any());
        when(userResource.toRepresentation()).thenReturn(new UserRepresentation());
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(Mockito.<String>any())).thenReturn(userResource);
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act
        ResponseEntity<String> actualDeleteUserResult = userManagementService.deleteUser("42");

        // Assert
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).users();
        verify(userResource).toRepresentation();
        verify(userResource).update(isA(UserRepresentation.class));
        verify(usersResource, atLeast(1)).get(eq("42"));
        HttpStatusCode statusCode = actualDeleteUserResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("User disabled successfully", actualDeleteUserResult.getBody());
        assertEquals(200, actualDeleteUserResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualDeleteUserResult.hasBody());
        assertTrue(actualDeleteUserResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserManagementService#deleteUser(String)}
     */
    @Test
    void testDeleteUser2() {
        // Arrange
        UserResource userResource = mock(UserResource.class);
        doThrow(new NotFoundException()).when(userResource).update(Mockito.<UserRepresentation>any());
        when(userResource.toRepresentation()).thenReturn(new UserRepresentation());
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(Mockito.<String>any())).thenReturn(userResource);
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> userManagementService.deleteUser("42"));
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).users();
        verify(userResource).toRepresentation();
        verify(userResource).update(isA(UserRepresentation.class));
        verify(usersResource, atLeast(1)).get(eq("42"));
    }

    /**
     * Method under test: {@link UserManagementService#assignRole(String, String)}
     */
    @Test
    void testAssignRole() {
        // Arrange
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        doNothing().when(roleScopeResource).add(Mockito.<List<RoleRepresentation>>any());
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        UserResource userResource = mock(UserResource.class);
        when(userResource.roles()).thenReturn(roleMappingResource);
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(Mockito.<String>any())).thenReturn(userResource);
        RoleResource roleResource = mock(RoleResource.class);
        when(roleResource.toRepresentation())
                .thenReturn(new RoleRepresentation("Name", "The characteristics of someone or something", true));
        RolesResource rolesResource = mock(RolesResource.class);
        when(rolesResource.get(Mockito.<String>any())).thenReturn(roleResource);
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.roles()).thenReturn(rolesResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act
        ResponseEntity<String> actualAssignRoleResult = userManagementService.assignRole("42", "Role Name");

        // Assert
        verify(keycloak, atLeast(1)).realm(eq("${keycloak.realm}"));
        verify(realmResource).roles();
        verify(realmResource).users();
        verify(roleMappingResource).realmLevel();
        verify(roleResource).toRepresentation();
        verify(roleScopeResource).add(isA(List.class));
        verify(rolesResource).get(eq("Role Name"));
        verify(userResource).roles();
        verify(usersResource).get(eq("42"));
        HttpStatusCode statusCode = actualAssignRoleResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("Role assigned successfully.", actualAssignRoleResult.getBody());
        assertEquals(200, actualAssignRoleResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualAssignRoleResult.hasBody());
        assertTrue(actualAssignRoleResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserManagementService#assignRole(String, String)}
     */
    @Test
    void testAssignRole2() {
        // Arrange
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        doThrow(new NotFoundException()).when(roleScopeResource).add(Mockito.<List<RoleRepresentation>>any());
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        UserResource userResource = mock(UserResource.class);
        when(userResource.roles()).thenReturn(roleMappingResource);
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(Mockito.<String>any())).thenReturn(userResource);
        RoleResource roleResource = mock(RoleResource.class);
        when(roleResource.toRepresentation())
                .thenReturn(new RoleRepresentation("Name", "The characteristics of someone or something", true));
        RolesResource rolesResource = mock(RolesResource.class);
        when(rolesResource.get(Mockito.<String>any())).thenReturn(roleResource);
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.roles()).thenReturn(rolesResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act
        ResponseEntity<String> actualAssignRoleResult = userManagementService.assignRole("42", "Role Name");

        // Assert
        verify(keycloak, atLeast(1)).realm(eq("${keycloak.realm}"));
        verify(realmResource).roles();
        verify(realmResource).users();
        verify(roleMappingResource).realmLevel();
        verify(roleResource).toRepresentation();
        verify(roleScopeResource).add(isA(List.class));
        verify(rolesResource).get(eq("Role Name"));
        verify(userResource).roles();
        verify(usersResource).get(eq("42"));
        HttpStatusCode statusCode = actualAssignRoleResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("Failed to assign role.", actualAssignRoleResult.getBody());
        assertEquals(500, actualAssignRoleResult.getStatusCodeValue());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, statusCode);
        assertTrue(actualAssignRoleResult.hasBody());
        assertTrue(actualAssignRoleResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserManagementService#unassignRole(String, String)}
     */
    @Test
    void testUnassignRole() {
        // Arrange
        RoleResource roleResource = mock(RoleResource.class);
        when(roleResource.toRepresentation())
                .thenReturn(new RoleRepresentation("Name", "The characteristics of someone or something", true));
        RolesResource rolesResource = mock(RolesResource.class);
        when(rolesResource.get(Mockito.<String>any())).thenReturn(roleResource);
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        doNothing().when(roleScopeResource).remove(Mockito.<List<RoleRepresentation>>any());
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        UserResource userResource = mock(UserResource.class);
        when(userResource.roles()).thenReturn(roleMappingResource);
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(Mockito.<String>any())).thenReturn(userResource);
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(realmResource.roles()).thenReturn(rolesResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act
        ResponseEntity<String> actualUnassignRoleResult = userManagementService.unassignRole("42", "Role Name");

        // Assert
        verify(keycloak, atLeast(1)).realm(eq("${keycloak.realm}"));
        verify(realmResource).roles();
        verify(realmResource).users();
        verify(roleMappingResource).realmLevel();
        verify(roleResource).toRepresentation();
        verify(roleScopeResource).remove(isA(List.class));
        verify(rolesResource).get(eq("Role Name"));
        verify(userResource).roles();
        verify(usersResource).get(eq("42"));
        HttpStatusCode statusCode = actualUnassignRoleResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("Role unassigned successfully", actualUnassignRoleResult.getBody());
        assertEquals(200, actualUnassignRoleResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualUnassignRoleResult.hasBody());
        assertTrue(actualUnassignRoleResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserManagementService#unassignRole(String, String)}
     */
    @Test
    void testUnassignRole2() {
        // Arrange
        RoleResource roleResource = mock(RoleResource.class);
        when(roleResource.toRepresentation()).thenThrow(new NotFoundException());
        RolesResource rolesResource = mock(RolesResource.class);
        when(rolesResource.get(Mockito.<String>any())).thenReturn(roleResource);
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.roles()).thenReturn(rolesResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act
        ResponseEntity<String> actualUnassignRoleResult = userManagementService.unassignRole("42", "Role Name");

        // Assert
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).roles();
        verify(roleResource).toRepresentation();
        verify(rolesResource).get(eq("Role Name"));
        HttpStatusCode statusCode = actualUnassignRoleResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("Failed to unassign role: HTTP 404 Not Found", actualUnassignRoleResult.getBody());
        assertEquals(500, actualUnassignRoleResult.getStatusCodeValue());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, statusCode);
        assertTrue(actualUnassignRoleResult.hasBody());
        assertTrue(actualUnassignRoleResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserManagementService#getUsers()}
     */
    @Test
    void testGetUsers() {
        // Arrange
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.list()).thenReturn(new ArrayList<>());
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act
        List<UserDto> actualUsers = userManagementService.getUsers();

        // Assert
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).users();
        verify(usersResource).list();
        assertTrue(actualUsers.isEmpty());
    }

    /**
     * Method under test: {@link UserManagementService#getUsers()}
     */
    @Test
    void testGetUsers2() {
        // Arrange
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.list()).thenThrow(new NotFoundException());
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> userManagementService.getUsers());
        verify(keycloak).realm(eq("${keycloak.realm}"));
        verify(realmResource).users();
        verify(usersResource).list();
    }

    /**
     * Method under test: {@link UserManagementService#getUsers()}
     */
    @Test
    void testGetUsers3() {
        // Arrange
        UserRepresentation userRepresentation = mock(UserRepresentation.class);
        when(userRepresentation.getEmail()).thenReturn("jane.doe@example.org");
        when(userRepresentation.getFirstName()).thenReturn("Jane");
        when(userRepresentation.getId()).thenReturn("42");
        when(userRepresentation.getLastName()).thenReturn("Doe");
        when(userRepresentation.getUsername()).thenReturn("janedoe");
        when(userRepresentation.isEnabled()).thenReturn(true);

        ArrayList<UserRepresentation> userRepresentationList = new ArrayList<>();
        userRepresentationList.add(userRepresentation);

        MappingsRepresentation mappingsRepresentation = new MappingsRepresentation();
        mappingsRepresentation.setClientMappings(new HashMap<>());
        mappingsRepresentation.setRealmMappings(new ArrayList<>());
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(roleMappingResource.getAll()).thenReturn(mappingsRepresentation);
        UserResource userResource = mock(UserResource.class);
        when(userResource.roles()).thenReturn(roleMappingResource);
        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(Mockito.<String>any())).thenReturn(userResource);
        when(usersResource.list()).thenReturn(userRepresentationList);
        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(keycloak.realm(Mockito.<String>any())).thenReturn(realmResource);

        // Act
        List<UserDto> actualUsers = userManagementService.getUsers();

        // Assert
        verify(keycloak, atLeast(1)).realm(eq("${keycloak.realm}"));
        verify(realmResource, atLeast(1)).users();
        verify(roleMappingResource).getAll();
        verify(userResource).roles();
        verify(usersResource).get(eq("42"));
        verify(usersResource).list();
        verify(userRepresentation).getEmail();
        verify(userRepresentation).getFirstName();
        verify(userRepresentation, atLeast(1)).getId();
        verify(userRepresentation).getLastName();
        verify(userRepresentation).getUsername();
        verify(userRepresentation).isEnabled();
        assertEquals(1, actualUsers.size());
        UserDto getResult = actualUsers.get(0);
        assertEquals("42", getResult.getId());
        assertEquals("Doe", getResult.getLastName());
        assertEquals("Jane", getResult.getFirstName());
        assertEquals("jane.doe@example.org", getResult.getEmail());
        assertEquals("janedoe", getResult.getUsername());
        assertNull(getResult.getPassword());
        assertTrue(getResult.getRoles().isEmpty());
    }
}
