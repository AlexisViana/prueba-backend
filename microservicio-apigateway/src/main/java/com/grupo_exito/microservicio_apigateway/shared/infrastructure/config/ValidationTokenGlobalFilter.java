package com.grupo_exito.microservicio_apigateway.shared.infrastructure.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValidationTokenGlobalFilter implements GlobalFilter, Ordered{

    @Value("${jwt.secret}")
    String secret;

    @Value("${jwt.algorithm}")
    String algorithm;

    @Value("${jwt.prefix}")
    String tokenPrefix;

    @Value("${jwt.authorization-header-name}")
    String authorizationHeaderName;

    @Value("${routes.public}")
    String publicPathsString;

    @Autowired
    CorsConfigurationSource customCorsConfigurationSource;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (publicPathValidator(exchange)) return chain.filter(exchange);
        return authorizationHeaderValidationProcess(exchange, chain);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private Mono<Void> authorizationHeaderValidationProcess(ServerWebExchange exchange, GatewayFilterChain chain) {
        return authorizationHeaderValidation(exchange, chain);
    }

    private Mono<Void> authorizationHeaderValidation(ServerWebExchange exchange, GatewayFilterChain chain){
        var header = exchange.getRequest().getHeaders();
        var authorizationHeader = header.getValuesAsList(authorizationHeaderName).stream().findFirst();
        if (authorizationHeader.isEmpty() || !authorizationHeader.get().startsWith(tokenPrefix)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        var token = authorizationHeader.get().split(" ")[1].trim();
        return tokenClaimsValidation(exchange, chain, token);
    }

    private Mono<Void> tokenClaimsValidation(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
        var jwtParser = getTokenJwtParser();
        var validationResponse = claimsDelegatedValidation(exchange, token, jwtParser);
        if(validationResponse) return chain.filter(exchange);

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private JwtParser getTokenJwtParser() {
        var key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parser().verifyWith(key).build();
    }

    private boolean claimsDelegatedValidation(ServerWebExchange exchange, String token, JwtParser jwtParser) {
        try {
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    private boolean publicPathValidator(ServerWebExchange serverWebExchange) {
        String[] publicPaths = publicPathsString.split(",");
        var contextPath = serverWebExchange.getRequest().getURI().getPath();
        return Arrays.asList(publicPaths).contains(contextPath);
    }

    @Bean
    SecurityWebFilterChain customSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        return http
            .csrf((customize) -> customize.disable())
            .cors((corsCustomize) -> corsCustomize.configurationSource(customCorsConfigurationSource))
            .authorizeExchange((authorize) ->
                authorize.anyExchange().permitAll()
            )
            .httpBasic((httpBasicCustomizer) -> httpBasicCustomizer.disable())
            .formLogin((formLoginCustomizer) -> formLoginCustomizer.disable())
            .build();
    }
}
