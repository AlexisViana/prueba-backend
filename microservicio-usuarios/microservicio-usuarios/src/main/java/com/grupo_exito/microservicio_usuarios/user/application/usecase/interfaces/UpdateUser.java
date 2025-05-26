package com.grupo_exito.microservicio_usuarios.user.application.usecase.interfaces;

import com.grupo_exito.microservicio_usuarios.user.infraestructure.dto.UserUpdateDto;
import reactor.core.publisher.Mono;

public interface UpdateUser {

    Mono<String> execute(String username, Mono<UserUpdateDto> userUpdateDtoMono);
}
