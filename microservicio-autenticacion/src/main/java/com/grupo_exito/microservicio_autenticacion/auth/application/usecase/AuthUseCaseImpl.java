package com.grupo_exito.microservicio_autenticacion.auth.application.usecase;


import com.grupo_exito.microservicio_autenticacion.auth.infrastructure.dto.AuthRequestDTO;
import com.grupo_exito.microservicio_autenticacion.auth.infrastructure.dto.AuthResponseDTO;
import com.grupo_exito.microservicio_autenticacion.shared.application.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AuthUseCaseImpl implements QueryAuthUseCaseService<AuthRequestDTO, AuthResponseDTO>{

    TokenService<AuthResponseDTO> tokenService;

    AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDTO executeQuery(AuthRequestDTO authRequestDTO, HttpServletRequest request,
            HttpServletResponse response) {
        var token = UsernamePasswordAuthenticationToken.unauthenticated(authRequestDTO.getUsername(), authRequestDTO.getPassword());
        var authentication = authenticationManager.authenticate(token);
        return prepareResponseObject(authentication);
    }

    private AuthResponseDTO prepareResponseObject(Authentication authentication) {
        var authResponse = AuthResponseDTO.builder()
            .username(authentication.getPrincipal().toString())
            .status(authentication.isAuthenticated())
            .roles(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
            .build();
        authResponse.setToken(tokenService.generateToken(authResponse));
        return authResponse;
    }

}
