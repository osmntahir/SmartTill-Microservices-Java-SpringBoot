package com.toyota.usermanagementservice.config;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Component
public class KeycloakInitializer implements ApplicationRunner {

    private Keycloak adminKeycloak;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Connect to Keycloak using admin credentials
        adminKeycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:9090") // Keycloak server URL
                .realm("master") // Admin realm, usually "master"
                .clientId("admin-cli") // Admin client ID
                .username("admin") // Admin username
                .password("admin") // Admin password
                .grantType("password") // Grant type for password-based auth
                .build();

        // Create realm if it doesn't exist
        if (!realmExists("new_realm")) {
            createRealm("new_realm");
        }

        // Create client if it doesn't exist
        if (!clientExists("new_realm", "new_client")) {
            createClient("new_realm", "new_client");
            String clientSecret = getClientSecret("new_realm", "new_client");
            writeSecretToFile(clientSecret, "client-secret.txt");
        }

        String secretFromFile = readSecretFromFile("client-secret.txt");
        System.out.println("Secret from file: " + secretFromFile);

        // Assign roles to the service account of the client
        assignRealmManagementRolesToServiceAccount("new_realm", "new_client", List.of("view-users", "manage-users", "manage-realm"));

        // Create realm roles if they don't exist
        createRealmRoles("new_realm", List.of("ADMIN", "CASHIER", "MANAGER"));

        // Create an admin user with the "ADMIN" role
        createUser("new_realm", "admin", "admin123", "ADMIN");
    }

    private String getClientSecret(String realmName, String clientId) {
        ClientRepresentation client = adminKeycloak.realm(realmName)
                .clients()
                .findByClientId(clientId)
                .get(0);
        ClientResource clientResource = adminKeycloak.realm(realmName).clients().get(client.getId());
        return clientResource.getSecret().getValue();
    }

    private void writeSecretToFile(String secret, String filePath) throws IOException {
        String absoluteFilePath = System.getProperty("user.dir") + "/" + filePath;


        if (!Files.exists(Paths.get(absoluteFilePath))) {

            Files.createFile(Paths.get(absoluteFilePath));
            System.out.println("File created: " + absoluteFilePath);
        }

        try (FileWriter writer = new FileWriter(absoluteFilePath)) {
            writer.write(secret);
            System.out.println("Client secret written to file: " + absoluteFilePath);
        }
    }

    private String readSecretFromFile(String filePath) throws IOException {
        String absoluteFilePath = System.getProperty("user.dir") + "/" + filePath;


        if (!Files.exists(Paths.get(absoluteFilePath))) {
            Files.createFile(Paths.get(absoluteFilePath));
            System.out.println("File created: " + absoluteFilePath);
        }

        return Files.readString(Paths.get(absoluteFilePath));
    }

    /**
     * Checks if a realm exists.
     * @param realmName The name of the realm to check.
     * @return true if the realm exists, false otherwise.
     */
    private boolean realmExists(String realmName) {
        return adminKeycloak.realms().findAll().stream().anyMatch(realm -> realm.getRealm().equals(realmName));
    }

    /**
     * Creates a new realm.
     * @param realmName The name of the realm to create.
     */
    private void createRealm(String realmName) {
        RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setRealm(realmName);
        realmRepresentation.setEnabled(true);
        adminKeycloak.realms().create(realmRepresentation);
    }

    /**
     * Checks if a client exists in the specified realm.
     * @param realmName The realm name.
     * @param clientId The client ID to check.
     * @return true if the client exists, false otherwise.
     */
    private boolean clientExists(String realmName, String clientId) {
        return !adminKeycloak.realm(realmName).clients().findByClientId(clientId).isEmpty();
    }

    /**
     * Creates a new client in the specified realm.
     * @param realmName The realm name.
     * @param clientId The client ID.
     */
    private void createClient(String realmName, String clientId) {
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId(clientId);
        clientRepresentation.setDirectAccessGrantsEnabled(true);
        clientRepresentation.setServiceAccountsEnabled(true);
        clientRepresentation.setPublicClient(false);
        clientRepresentation.setEnabled(true);
        adminKeycloak.realm(realmName).clients().create(clientRepresentation);
    }

    /**
     * Creates realm roles if they don't already exist.
     * @param realmName The realm name.
     * @param roles The list of roles to create.
     */
    private void createRealmRoles(String realmName, List<String> roles) {
        RealmResource realmResource = adminKeycloak.realm(realmName);
        for (String role : roles) {
            try {
                // Check if the role already exists
                RoleRepresentation existingRole = realmResource.roles().get(role).toRepresentation();
                if (existingRole != null) {
                    System.out.println("Role already exists: " + role);
                    continue;
                }
            } catch (jakarta.ws.rs.NotFoundException e) {
                // Create the role if not found
                RoleRepresentation roleRepresentation = new RoleRepresentation();
                roleRepresentation.setName(role);
                roleRepresentation.setDescription(role + " role");
                realmResource.roles().create(roleRepresentation);
                System.out.println("Role created: " + role);
            }
        }
    }

    /**
     * Creates a new user in the specified realm and assigns a role.
     * @param realmName The realm name.
     * @param username The username of the user.
     * @param password The password for the user.
     * @param roleName The role to assign to the user.
     */
    private void createUser(String realmName, String username, String password, String roleName) {
        if (!userExists(realmName, username)) {
            UserRepresentation user = new UserRepresentation();
            user.setUsername(username);
            user.setEnabled(true);

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setTemporary(false);
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);

            Response response = adminKeycloak.realm(realmName).users().create(user);
            if (response.getStatus() == 201) {
                String userId = response.getLocation().getPath().replaceAll(".*/(.*)$", "$1");
                UserResource userResource = adminKeycloak.realm(realmName).users().get(userId);
                userResource.resetPassword(credential);

                RoleRepresentation role = adminKeycloak.realm(realmName).roles().get(roleName).toRepresentation();
                userResource.roles().realmLevel().add(Collections.singletonList(role));
            } else {
                throw new RuntimeException("Failed to create user: " + response.getStatusInfo());
            }
        }
    }

    /**
     * Checks if a user exists in the specified realm.
     * @param realmName The realm name.
     * @param username The username to check.
     * @return true if the user exists, false otherwise.
     */
    private boolean userExists(String realmName, String username) {
        return !adminKeycloak.realm(realmName).users().search(username).isEmpty();
    }

    /**
     * Assigns realm-management roles to the service account of a client.
     * @param realmName The realm name.
     * @param clientId The client ID.
     * @param roles The roles to assign.
     */
    private void assignRealmManagementRolesToServiceAccount(String realmName, String clientId, List<String> roles) {
        RealmResource realmResource = adminKeycloak.realm(realmName);
        ClientRepresentation clientRepresentation = realmResource.clients().findByClientId(clientId).get(0);
        ClientResource clientResource = realmResource.clients().get(clientRepresentation.getId());

        UserRepresentation serviceAccountUserRepresentation = clientResource.getServiceAccountUser();
        String serviceAccountUserId = serviceAccountUserRepresentation.getId();
        UserResource serviceAccountUserResource = realmResource.users().get(serviceAccountUserId);

        List<ClientRepresentation> realmManagementClients = adminKeycloak.realm("new_realm").clients().findByClientId("realm-management");
        if (realmManagementClients.isEmpty()) {
            throw new RuntimeException("realm-management client not found!");
        }

        ClientResource realmManagementClientResource = adminKeycloak.realm("new_realm").clients().get(realmManagementClients.get(0).getId());

        for (String roleName : roles) {
            RoleResource roleResource = realmManagementClientResource.roles().get(roleName);
            RoleRepresentation role;
            try {
                role = roleResource.toRepresentation();
            } catch (jakarta.ws.rs.NotFoundException e) {
                throw new RuntimeException("Role not found: " + roleName, e);
            }
            serviceAccountUserResource.roles().clientLevel(realmManagementClientResource.toRepresentation().getId()).add(Collections.singletonList(role));
        }
    }

}
