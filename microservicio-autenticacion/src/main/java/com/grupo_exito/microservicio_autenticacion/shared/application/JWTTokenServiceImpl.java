package com.grupo_exito.microservicio_autenticacion.shared.application;


import com.grupo_exito.microservicio_autenticacion.auth.infrastructure.dto.AuthResponseDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class JWTTokenServiceImpl implements TokenService<AuthResponseDTO>{

    @Value("${jwt.key-subject}")
    String keySubject;

    @Value("${jwt.key-roles}")
    String keyRoles;

    @Value("${jwt.expiration-time-hour}")
    int expirationTimeHour;

    @Value("${jwt.secret}")
    String secret;

    @Override
    public String generateToken(AuthResponseDTO authResponseDTO) {
        var calendar = Calendar.getInstance();
        var tokenDate = calendar.getTime();
        calendar.add(Calendar.MINUTE, expirationTimeHour);
        var tokenExp = calendar.getTime();
        
        return Jwts.builder()
            .setId(UUID.randomUUID().toString())
            .setIssuedAt(tokenDate)
            .setClaims(createClaims(authResponseDTO))
            .setExpiration(tokenExp)
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
            .compact();
    }

    private Map<String, Object> createClaims(AuthResponseDTO authResponseDTO) {
        var subject = new HashMap<String, Object>();
        subject.put(keySubject, authResponseDTO.getUsername());
        subject.put(keyRoles, authResponseDTO.getRoles());
        return subject;
    }


    
}
