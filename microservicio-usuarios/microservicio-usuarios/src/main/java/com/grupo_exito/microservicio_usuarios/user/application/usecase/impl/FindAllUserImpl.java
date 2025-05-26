package com.grupo_exito.microservicio_usuarios.user.application.usecase.impl;

import com.grupo_exito.microservicio_usuarios.user.application.usecase.interfaces.FindAllUser;
import com.grupo_exito.microservicio_usuarios.user.domain.User;
import com.grupo_exito.microservicio_usuarios.user.infraestructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class FindAllUserImpl implements FindAllUser {

    @Autowired
    UserRepository userRepository;

    @Override
    public Flux<User> execute() {
        return userRepository.findAll();
    }
}
