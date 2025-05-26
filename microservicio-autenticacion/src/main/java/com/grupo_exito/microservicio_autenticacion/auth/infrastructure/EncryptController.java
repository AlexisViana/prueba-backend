package com.grupo_exito.microservicio_autenticacion.auth.infrastructure;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/encrypt")
@AllArgsConstructor
public class EncryptController {
    
    PasswordEncoder passwordEncoder;

    @GetMapping
	@ResponseBody
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public String userCreateProcess(@RequestParam("value") String value) {
        return passwordEncoder.encode(value);
    }
}
