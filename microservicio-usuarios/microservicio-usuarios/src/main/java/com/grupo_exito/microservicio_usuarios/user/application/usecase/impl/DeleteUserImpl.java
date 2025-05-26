package com.grupo_exito.microservicio_usuarios.user.application.usecase.impl;

import com.grupo_exito.microservicio_usuarios.user.application.usecase.interfaces.DeleteUser;
import com.grupo_exito.microservicio_usuarios.user.infraestructure.repository.AuthorityRepository;
import com.grupo_exito.microservicio_usuarios.user.infraestructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteUserImpl implements DeleteUser {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public Mono<String> execute(String username) {
        return userRepository.findByUsername(username)
                .flatMap(existingUser ->
                        authorityRepository.deleteByUsername(existingUser.getUsername())
                                .then(userRepository.delete(existingUser))
                                .thenReturn("Usuario eliminado correctamente")
                )
                .switchIfEmpty(Mono.just("Usuario no encontrado o ya fue eliminado"));
    }
}
