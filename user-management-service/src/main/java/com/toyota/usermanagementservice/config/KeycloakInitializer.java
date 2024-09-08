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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Component()
public class KeycloakInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakInitializer.class);
    private Keycloak adminKeycloak;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Connect to Keycloak using admin credentials
        adminKeycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:9090")
                .realm("master")
                .clientId("admin-cli")
                .username("admin")
                .password("admin")
                .grantType("password")
                .build();

        // Create realm if it doesn't exist
        if (!realmExists("32bit_realm")) {
            createRealm("32bit_realm");
        } else {
            logger.info("Realm '32bit_realm' already exists.");
        }

        // Create client if it doesn't exist
        if (!clientExists("32bit_realm", "32bit_client")) {
            createClient("32bit_realm", "32bit_client");
            String clientSecret = getClientSecret("32bit_realm", "32bit_client");
            writeSecretToFile(clientSecret, "client-secret.txt");
        } else {
            logger.info("Client '32bit_client' already exists in '32bit_realm'.");
        }

        String secretFromFile = readSecretFromFile("client-secret.txt");
        logger.info("Secret from file: {}", secretFromFile);

        // Assign roles to the service account of the client
        assignRealmManagementRolesToServiceAccount("32bit_realm", "32bit_client", List.of("view-users", "manage-users", "manage-realm"));

        // Create realm roles if they don't exist
        createRealmRoles("32bit_realm", List.of("ADMIN", "CASHIER", "MANAGER"));

        // Create an admin user with the "ADMIN" role
        createUser("32bit_realm", "admin", "admin123", "ADMIN");
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
            logger.info("File created: {}", absoluteFilePath);
        }

        try (FileWriter writer = new FileWriter(absoluteFilePath)) {
            writer.write(secret);
            logger.info("Client secret written to file: {}", absoluteFilePath);
        }
    }

    private String readSecretFromFile(String filePath) throws IOException {
        String absoluteFilePath = System.getProperty("user.dir") + "/" + filePath;

        return Files.readString(Paths.get(absoluteFilePath));
    }

    private boolean realmExists(String realmName) {
        return adminKeycloak.realms().findAll().stream().anyMatch(realm -> realm.getRealm().equals(realmName));
    }

    private void createRealm(String realmName) {
        RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setRealm(realmName);
        realmRepresentation.setEnabled(true);
        adminKeycloak.realms().create(realmRepresentation);
        logger.info("Realm '{}' created.", realmName);
    }

    private boolean clientExists(String realmName, String clientId) {
        return !adminKeycloak.realm(realmName).clients().findByClientId(clientId).isEmpty();
    }

    private void createClient(String realmName, String clientId) {
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId(clientId);
        clientRepresentation.setDirectAccessGrantsEnabled(true);
        clientRepresentation.setServiceAccountsEnabled(true);
        clientRepresentation.setPublicClient(false);
        clientRepresentation.setEnabled(true);
        adminKeycloak.realm(realmName).clients().create(clientRepresentation);
        logger.info("Client '{}' created in realm '{}'.", clientId, realmName);
    }

    private void createRealmRoles(String realmName, List<String> roles) {
        RealmResource realmResource = adminKeycloak.realm(realmName);
        for (String role : roles) {
            try {
                // Check if the role already exists
                RoleRepresentation existingRole = realmResource.roles().get(role).toRepresentation();
                if (existingRole != null) {
                    logger.info("Role '{}' already exists.", role);
                    continue;
                }
            } catch (jakarta.ws.rs.NotFoundException e) {
                // Create the role if not found
                RoleRepresentation roleRepresentation = new RoleRepresentation();
                roleRepresentation.setName(role);
                roleRepresentation.setDescription(role + " role");
                realmResource.roles().create(roleRepresentation);
                logger.info("Role '{}' created.", role);
            }
        }
    }

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
                logger.info("User '{}' created and assigned role '{}'.", username, roleName);
            } else {
                throw new RuntimeException("Failed to create user: " + response.getStatusInfo());
            }
        } else {
            logger.info("User '{}' already exists.", username);
        }
    }

    private boolean userExists(String realmName, String username) {
        return !adminKeycloak.realm(realmName).users().search(username).isEmpty();
    }

    private void assignRealmManagementRolesToServiceAccount(String realmName, String clientId, List<String> roles) {
        RealmResource realmResource = adminKeycloak.realm(realmName);
        ClientRepresentation clientRepresentation = realmResource.clients().findByClientId(clientId).get(0);
        ClientResource clientResource = realmResource.clients().get(clientRepresentation.getId());

        UserRepresentation serviceAccountUserRepresentation = clientResource.getServiceAccountUser();
        String serviceAccountUserId = serviceAccountUserRepresentation.getId();
        UserResource serviceAccountUserResource = realmResource.users().get(serviceAccountUserId);

        List<ClientRepresentation> realmManagementClients = adminKeycloak.realm(realmName).clients().findByClientId("realm-management");
        if (realmManagementClients.isEmpty()) {
            throw new RuntimeException("realm-management client not found!");
        }

        ClientResource realmManagementClientResource = adminKeycloak.realm(realmName).clients().get(realmManagementClients.get(0).getId());

        for (String roleName : roles) {
            RoleResource roleResource = realmManagementClientResource.roles().get(roleName);
            RoleRepresentation role;
            try {
                role = roleResource.toRepresentation();
            } catch (jakarta.ws.rs.NotFoundException e) {
                throw new RuntimeException("Role not found: " + roleName, e);
            }
            serviceAccountUserResource.roles().clientLevel(realmManagementClientResource.toRepresentation().getId()).add(Collections.singletonList(role));
            logger.info("Role '{}' assigned to service account.", roleName);
        }
    }
    @Bean
    public String initializeKeycloak() throws Exception {
        run(null);
        return "Keycloak Initialized";
    }
    
}
