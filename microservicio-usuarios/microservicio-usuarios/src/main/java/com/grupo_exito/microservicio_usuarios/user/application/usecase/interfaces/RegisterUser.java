package com.grupo_exito.microservicio_usuarios.user.application.usecase.interfaces;

import com.grupo_exito.microservicio_usuarios.user.infraestructure.dto.UserCreateDto;
import reactor.core.publisher.Mono;

public interface RegisterUser {

    Mono<String> execute(Mono<UserCreateDto> userCreateDtoMono);

}
