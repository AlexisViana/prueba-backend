package com.grupo_exito.microservicio_apigateway.shared.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;

import lombok.experimental.FieldDefaults;

@Component
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CustomCorsReactiveConfigurationSource implements CorsConfigurationSource{
    
    @Value("${cors.allowed-origins}")
    String[] origins;
    
    @Value("${cors.allowed-methods}")
    String[] methods;
    
    @Value("${cors.allowed-headers}")
    String[] headers;
    
    @Value("${cors.allow-credentials}")
    boolean credentials;

    @Override
    public CorsConfiguration getCorsConfiguration(ServerWebExchange exchange) {
        var corsConfiguration = new CorsConfiguration();
        originsAllowedAgreggation(corsConfiguration);
        methodsAllowedAgreggation(corsConfiguration);
        optionsAllowedAgreggation(corsConfiguration);
        corsConfiguration.setAllowCredentials(credentials);
        return corsConfiguration;
    }

    private void optionsAllowedAgreggation(CorsConfiguration corsConfiguration) {
        for (String method : methods) {
            corsConfiguration.addAllowedMethod(method);
        }
    }

    private void methodsAllowedAgreggation(CorsConfiguration corsConfiguration) {
        for (String header: headers) {
            corsConfiguration.addAllowedHeader(header);
        }
    }

    private void originsAllowedAgreggation(CorsConfiguration corsConfiguration) {
        for (String origin: origins) {
            corsConfiguration.addAllowedOrigin(origin);
        }
    }
    
}
