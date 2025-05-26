package com.grupo_exito.microservicio_autenticacion.shared.application;

public interface TokenService <T> {
    
    String generateToken(T object);
}
