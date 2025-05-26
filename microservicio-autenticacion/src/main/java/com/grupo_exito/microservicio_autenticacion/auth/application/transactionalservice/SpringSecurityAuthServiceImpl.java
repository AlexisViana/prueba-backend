package com.grupo_exito.microservicio_autenticacion.auth.application.transactionalservice;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SpringSecurityAuthServiceImpl implements AuthenticationManager {

    org.springframework.security.core.userdetails.UserDetailsService userDetailsService; 
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            var user = userDetailsService.loadUserByUsername(authentication.getPrincipal().toString());
            passwordValidation(user, authentication.getCredentials().toString());
            return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), user.getAuthorities());
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException("Credenciales invalidas");
        } 
    }
    
    private void passwordValidation(UserDetails user, String password) {
        var res = passwordEncoder.matches(password, user.getPassword());
        if (!res) throw new RuntimeException("Credenciales invalidas");
    }

}
