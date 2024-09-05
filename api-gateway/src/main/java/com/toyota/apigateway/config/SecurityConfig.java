package com.toyota.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        return serverHttpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/eureka/**").permitAll()  // Allow access to Eureka
                        .pathMatchers("/product/**").hasAnyRole("ADMIN","CASHIER","MANAGER")  // Role-based access for ADMIN
                        .pathMatchers("/user/**").hasRole("ADMIN")    // Role-based access for USER
                        .pathMatchers("/sale/**").hasRole("CASHIER")   // Role-based access for SALE
                        .pathMatchers("/report/**").hasRole("MANAGER")  // Role-based access for MANAGER
                        .anyExchange().authenticated()               // All other requests must be authenticated
                )
                .oauth2ResourceServer((oauth) -> oauth
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())) // Custom converter for roles
                )
                .build();
    }


}
