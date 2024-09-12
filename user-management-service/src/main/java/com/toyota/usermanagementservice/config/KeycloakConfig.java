package com.toyota.usermanagementservice.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Configuration class for Keycloak integration.
 * This class listens for client secret events and configures Keycloak with the dynamic client secret.
 */
@Component
public class KeycloakConfig {

    private static final Logger logger = Logger.getLogger(KeycloakConfig.class.getName());

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

    private Keycloak keycloak;

    /**
     * Event listener for client secret events. When the event is triggered, the client secret is used to configure Keycloak.
     *
     * @param event the event containing the client secret.
     */
    @EventListener
    public void onClientSecretEvent(ClientSecretEvent event) {
        String clientSecret = event.getClientSecret();
        logger.info("Client secret received: " + clientSecret);


        keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(grantType)
                .scope(scope)
                .build();

        logger.info("Keycloak instance created with dynamic client secret.");
    }
    @Bean
    public Keycloak getKeycloak() {
        return keycloak;
    }

}
