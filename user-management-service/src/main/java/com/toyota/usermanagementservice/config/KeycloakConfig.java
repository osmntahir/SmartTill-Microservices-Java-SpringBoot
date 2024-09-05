package com.toyota.usermanagementservice.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:9090/")
                .realm("32bit-realm")
                .clientId("32bit-client")
                .clientSecret("dZve0TUZsXTXjAQ978rc38qPnTsfINgx")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .scope("openid profile email") // Add necessary scopes
                .build();

    }
}
