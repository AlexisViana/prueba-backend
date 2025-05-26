package com.grupo_exito.microservicio_usuarios.user.application.usecase.interfaces;

import com.grupo_exito.microservicio_usuarios.user.domain.User;
import reactor.core.publisher.Flux;

public interface FindAllUser {

    Flux<User> execute();

}
