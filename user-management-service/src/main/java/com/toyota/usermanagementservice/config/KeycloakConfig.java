package com.toyota.usermanagementservice.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Configuration class for Keycloak integration.
 * It ensures that the Keycloak bean is created after KeycloakInitializer.
 */

@Configuration
@DependsOn("initializeKeycloak") // Ensure KeycloakInitializer completes first
public class KeycloakConfig {

    Logger logger = Logger.getLogger(KeycloakConfig.class.getName());
    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.grant-type}")
    private String grantType;

    @Value("${keycloak.scope}")
    private String scope;

    @Bean
    public Keycloak keycloak() throws IOException {

        String clientSecret = readSecretFromFile("client-secret.txt");
        logger.info("Creating Keycloak instance");
        logger.info("Client secret: " + clientSecret);

        // Create and return Keycloak instance
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(grantType)
                .scope(scope)
                .build();
    }

    private String readSecretFromFile(String filePath) throws IOException {
        String absoluteFilePath = System.getProperty("user.dir") + "/" + filePath;
        return Files.readString(Paths.get(absoluteFilePath));
    }
}
