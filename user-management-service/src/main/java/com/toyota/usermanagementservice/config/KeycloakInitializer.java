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
import org.springframework.beans.factory.annotation.Value;
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

/**
 * This class initializes Keycloak configurations, including setting up realms, clients, roles,
 * and an admin user. It runs once the application starts and ensures that Keycloak is correctly configured.
 */
@Component
public class KeycloakInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakInitializer.class);
    private Keycloak adminKeycloak;

    /**
     * Path to the file where the client secret will be stored, set in the application.properties.
     */
    @Value("${keycloak.client-secret-path}")
    private String clientSecretPath;

    /**
     * The main method that runs at application startup. It connects to Keycloak, creates realms,
     * clients, roles, and users if they do not already exist. It also writes the client secret to a file.
     *
     * @param args the application arguments.
     * @throws Exception if there is an issue initializing Keycloak.
     */
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
            writeSecretToFile(clientSecret, clientSecretPath); // Use the path from application.properties
        } else {
            logger.info("Client '32bit_client' already exists in '32bit_realm'.");
        }

        String secretFromFile = readSecretFromFile(clientSecretPath);
        logger.info("Secret from file: {}", secretFromFile);

        // Assign roles to the service account of the client
        assignRealmManagementRolesToServiceAccount("32bit_realm", "32bit_client", List.of("view-users", "manage-users", "manage-realm"));

        // Create realm roles if they don't exist
        createRealmRoles("32bit_realm", List.of("ADMIN", "CASHIER", "MANAGER"));

        // Create an admin user with the "ADMIN" role
        createUser("32bit_realm", "admin", "admin123", "ADMIN");
    }

    /**
     * Retrieves the client secret for the given client in the specified realm.
     *
     * @param realmName the name of the realm.
     * @param clientId the ID of the client.
     * @return the client secret.
     */
    private String getClientSecret(String realmName, String clientId) {
        ClientRepresentation client = adminKeycloak.realm(realmName)
                .clients()
                .findByClientId(clientId)
                .get(0);
        ClientResource clientResource = adminKeycloak.realm(realmName).clients().get(client.getId());
        return clientResource.getSecret().getValue();
    }

    /**
     * Writes the client secret to a file at the specified path.
     *
     * @param secret the client secret to write.
     * @param filePath the path to the file.
     * @throws IOException if an I/O error occurs writing to or creating the file.
     */
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

    /**
     * Reads the client secret from a file at the specified path.
     *
     * @param filePath the path to the file containing the client secret.
     * @return the client secret read from the file.
     * @throws IOException if an I/O error occurs reading the file.
     */
    private String readSecretFromFile(String filePath) throws IOException {
        String absoluteFilePath = System.getProperty("user.dir") + "/" + filePath;
        return Files.readString(Paths.get(absoluteFilePath));
    }

    /**
     * Checks if the realm exists in Keycloak.
     *
     * @param realmName the name of the realm to check.
     * @return true if the realm exists, false otherwise.
     */
    private boolean realmExists(String realmName) {
        return adminKeycloak.realms().findAll().stream().anyMatch(realm -> realm.getRealm().equals(realmName));
    }

    /**
     * Creates a new realm in Keycloak with the specified name.
     *
     * @param realmName the name of the realm to create.
     */
    private void createRealm(String realmName) {
        RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setRealm(realmName);
        realmRepresentation.setEnabled(true);
        adminKeycloak.realms().create(realmRepresentation);
        logger.info("Realm '{}' created.", realmName);
    }

    /**
     * Checks if a client exists in the specified realm.
     *
     * @param realmName the name of the realm.
     * @param clientId the ID of the client.
     * @return true if the client exists, false otherwise.
     */
    private boolean clientExists(String realmName, String clientId) {
        return !adminKeycloak.realm(realmName).clients().findByClientId(clientId).isEmpty();
    }

    /**
     * Creates a new client in the specified realm with the given client ID.
     *
     * @param realmName the name of the realm.
     * @param clientId the ID of the client to create.
     */
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

    /**
     * Creates realm roles if they don't already exist in the specified realm.
     *
     * @param realmName the name of the realm.
     * @param roles the list of roles to create.
     */
    private void createRealmRoles(String realmName, List<String> roles) {
        RealmResource realmResource = adminKeycloak.realm(realmName);
        for (String role : roles) {
            try {
                RoleRepresentation existingRole = realmResource.roles().get(role).toRepresentation();
                if (existingRole != null) {
                    logger.info("Role '{}' already exists.", role);
                    continue;
                }
            } catch (jakarta.ws.rs.NotFoundException e) {
                RoleRepresentation roleRepresentation = new RoleRepresentation();
                roleRepresentation.setName(role);
                roleRepresentation.setDescription(role + " role");
                realmResource.roles().create(roleRepresentation);
                logger.info("Role '{}' created.", role);
            }
        }
    }

    /**
     * Creates a new user in the specified realm and assigns a role to the user.
     *
     * @param realmName the name of the realm.
     * @param username the username for the new user.
     * @param password the password for the new user.
     * @param roleName the name of the role to assign to the user.
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
                logger.info("User '{}' created and assigned role '{}'.", username, roleName);
            } else {
                throw new RuntimeException("Failed to create user: " + response.getStatusInfo());
            }
        } else {
            logger.info("User '{}' already exists.", username);
        }
    }

    /**
     * Checks if a user exists in the specified realm.
     *
     * @param realmName the name of the realm.
     * @param username the username to check.
     * @return true if the user exists, false otherwise.
     */
    private boolean userExists(String realmName, String username) {
        return !adminKeycloak.realm(realmName).users().search(username).isEmpty();
    }

    /**
     * Assigns realm-management roles to the service account of the client in the specified realm.
     *
     * @param realmName the name of the realm.
     * @param clientId the ID of the client.
     * @param roles the list of roles to assign.
     */
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

    /**
     * Bean to initialize Keycloak when the application starts.
     *
     * @return A message indicating that Keycloak has been initialized.
     * @throws Exception if there is an issue during initialization.
     */
    @Bean
    public String initializeKeycloak() throws Exception {
        run(null);
        return "Keycloak Initialized";
    }
}
