package com.toyota.usermanagementservice.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration class for Keycloak integration.
 * This class listens for client secret events and configures Keycloak with the dynamic client secret.
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
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(grantType)
                .scope(scope)
                .build();
    }

}