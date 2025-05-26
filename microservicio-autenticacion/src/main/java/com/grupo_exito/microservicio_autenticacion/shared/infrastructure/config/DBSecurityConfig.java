package com.grupo_exito.microservicio_autenticacion.shared.infrastructure.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@Configuration
public class DBSecurityConfig {

    @Value("${spring.jdbc.url}")
    String url;

    @Value("${spring.jdbc.username}")
    String username;

    @Value("${spring.jdbc.password}")
    String password;

    @Value("${spring.jdbc.driver-class}")
    String driverClass;

    @Bean
    DataSource dataSource() {
        var dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(url);
        dataSourceBuilder.driverClassName(driverClass);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);                           
        return dataSourceBuilder.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

}
