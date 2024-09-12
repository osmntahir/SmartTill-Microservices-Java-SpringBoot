package com.toyota.usermanagementservice.config;

import org.springframework.context.ApplicationEvent;

public class ClientSecretEvent extends ApplicationEvent {

    private final String clientSecret;

    public ClientSecretEvent(Object source, String clientSecret) {
        super(source);
        this.clientSecret = clientSecret;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
