package com.toyota.usermanagementservice.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Configuration class for Keycloak integration.
 * This class sets up the Keycloak client to interact with Keycloak's Admin REST API.
 * It provides a Keycloak bean that can be used throughout the application for Keycloak-related operations.
 */


@Configuration
public class KeycloakConfig {

    /**
     * The URL of the Keycloak server.
     */
    @Value("${keycloak.server-url}")
    private String serverUrl;

    /**
     * The realm used for Keycloak authentication.
     */
    @Value("${keycloak.realm}")
    private String realm;

    /**
     * The client ID used to authenticate against Keycloak.
     */
    @Value("${keycloak.client-id}")
    private String clientId;

    /**
     * The client secret used for authenticating the client.
     */
    @Value("${keycloak.client-secret}")
    private String clientSecret;

    /**
     * The OAuth2 grant type used for authentication (e.g., client_credentials).
     */
    @Value("${keycloak.grant-type}")
    private String grantType;

    /**
     * The scope requested from Keycloak during authentication (e.g., openid, profile, email).
     */
    @Value("${keycloak.scope}")
    private String scope;

    /**
     * Creates and configures a Keycloak instance to interact with the Keycloak server.
     * This Keycloak client will be used for administrative tasks, such as creating realms,
     * clients, users, and assigning roles.
     *
     * @return A configured {@link Keycloak} instance.
     */
    @Bean
    public Keycloak keycloak() throws Exception {
        String clientSecret = readSecretFromFile("client-secret.txt");
        System.out.println("clientSecret = " + clientSecret);
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl) // URL of the Keycloak server from application.properties
                .realm(realm) // Realm from application.properties
                .clientId(clientId) // Client ID from application.properties
                .clientSecret(clientSecret)
                .grantType(grantType) // OAuth2 grant type from application.properties
                .scope(scope) // Scopes from application.properties
                .build();
    }
    private String readSecretFromFile(String filePath) throws IOException {
        return Files.readString(Paths.get(filePath));
    }
}
