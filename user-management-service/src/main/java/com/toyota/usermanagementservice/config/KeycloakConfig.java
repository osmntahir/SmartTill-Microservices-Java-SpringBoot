package com.toyota.usermanagementservice.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Keycloak integration.
 * This class sets up the Keycloak client to interact with Keycloak's Admin REST API.
 * It provides a Keycloak bean that can be used throughout the application for Keycloak-related operations.
 */
@Configuration
public class KeycloakConfig {

    /**
     * Creates and configures a Keycloak instance to interact with the Keycloak server.
     *
     * @return A configured {@link Keycloak} instance.
     */
    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:9090/") // URL of the Keycloak server
                .realm("32bit-realm") // Keycloak realm to authenticate against
                .clientId("32bit-client") // Client ID for Keycloak authentication
                .clientSecret("dZve0TUZsXTXjAQ978rc38qPnTsfINgx") // Client secret for authentication
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS) // OAuth2 grant type for client credentials flow
                .scope("openid profile email") // Scopes requested from Keycloak
                .build();
    }
}
