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
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/product/**").hasAnyRole("ADMIN","CASHIER","MANAGER")
                        .pathMatchers("/user/**").hasRole("ADMIN")
                        .pathMatchers("/sale/getAll").hasRole("MANAGER")
                        .pathMatchers("/report/**").hasRole("MANAGER")
                        .pathMatchers("/sale/add/**").hasRole("CASHIER")
                        .pathMatchers("/sale/update/**").hasRole("CASHIER")
                        .pathMatchers("/sale/delete/**").hasRole("CASHIER")
                        .pathMatchers("/sale/getById/**").hasRole("MANAGER")
                        .pathMatchers("/campaign-product/**").hasRole("CASHIER")
                        .pathMatchers("/campaign/**").hasRole("CASHIER")
                        .pathMatchers("/sold-product/**").hasRole("CASHIER")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer((oauth) -> oauth
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())) // Custom converter for roles
                )
                .build();

}