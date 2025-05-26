package com.grupo_exito.microservicio_autenticacion.shared.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class CustomSpringSecurityFilterChain{

    @Bean
    SecurityFilterChain customSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf((customize) -> customize.disable())
            .authorizeHttpRequests((authorize) ->
                authorize.anyRequest().permitAll()
            )
            .build();
    }

}
