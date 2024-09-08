package com.toyota.usermanagementservice.config;

import org.springframework.context.ApplicationEvent;

public class KeycloakInitializationCompletedEvent extends ApplicationEvent {
    public KeycloakInitializationCompletedEvent(Object source) {
        super(source);
    }
}
