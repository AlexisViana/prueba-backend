package com.grupo_exito.microservicio_usuarios.user.application.usecase.impl;

import com.grupo_exito.microservicio_usuarios.user.application.usecase.interfaces.UpdateUser;
import com.grupo_exito.microservicio_usuarios.user.infraestructure.dto.UserUpdateDto;
import com.grupo_exito.microservicio_usuarios.user.infraestructure.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateUserImpl implements UpdateUser {

    @Autowired
    UserRepository userRepository;

    @Override
    public Mono<String> execute(String username, Mono<UserUpdateDto> userUpdateDtoMono) {
        return userRepository.findByUsername(username)
                .flatMap(existingUser ->
                        userUpdateDtoMono.flatMap(dto -> {
                            existingUser.setPassword("{bcrypt}" + BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
                            existingUser.setEnabled(dto.isEnabled());
                            return userRepository.save(existingUser)
                                    .thenReturn("Usuario actualizado correctamente");
                        })
                )
                .switchIfEmpty(Mono.just("NOT_FOUND"));
    }
}
