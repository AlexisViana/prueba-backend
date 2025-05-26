package com.grupo_exito.microservicio_autenticacion.auth.application.usecase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface QueryAuthUseCaseService <R,O>{
    
    O executeQuery(R object, HttpServletRequest request, HttpServletResponse response);
}
