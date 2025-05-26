package com.grupo_exito.microservicio_usuarios.user.application.usecase.interfaces;

import reactor.core.publisher.Mono;

public interface DeleteUser {

    Mono<String> execute(String username);

}
