package com.grupo_exito.microservicio_autenticacion.auth.infrastructure.controller;

import com.grupo_exito.microservicio_autenticacion.auth.application.usecase.QueryAuthUseCaseService;
import com.grupo_exito.microservicio_autenticacion.auth.infrastructure.dto.AuthRequestDTO;
import com.grupo_exito.microservicio_autenticacion.auth.infrastructure.dto.AuthResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class AuthController {

    QueryAuthUseCaseService<AuthRequestDTO, AuthResponseDTO> queryAuthUseCaseService;

    @PostMapping
	@ResponseBody
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public AuthResponseDTO userCreateProcess(@RequestBody AuthRequestDTO authRequestDTO, HttpServletRequest request, HttpServletResponse response) {
        return queryAuthUseCaseService.executeQuery(authRequestDTO, request, response);
    }

}
