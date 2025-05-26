package com.grupo_exito.microservicio_usuarios.user.application.usecase.impl;

import com.grupo_exito.microservicio_usuarios.user.application.usecase.interfaces.RegisterUser;
import com.grupo_exito.microservicio_usuarios.user.domain.Role;
import com.grupo_exito.microservicio_usuarios.user.infraestructure.dto.UserCreateDto;
import com.grupo_exito.microservicio_usuarios.user.infraestructure.repository.AuthorityRepository;
import com.grupo_exito.microservicio_usuarios.user.infraestructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RegisterUserImpl implements RegisterUser {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public Mono<String> execute(Mono<UserCreateDto> userCreateDtoMono) {
        return userCreateDtoMono.flatMap(userCreateDto -> {
            String username = userCreateDto.getUsername();
            String password = userCreateDto.getPassword();
            String role = Role.ROLE_USER.name();

            return userRepository.findByUsername(username)
                    .flatMap(existingUser -> Mono.<String>error(new IllegalStateException("El usuario ya existe")))
                    .switchIfEmpty(
                            Mono.defer(() -> {
                                String hashedPassword = "{bcrypt}" + org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());

                                return userRepository.insertUser(username, hashedPassword, true)
                                        .then(
                                                authorityRepository.findByUsernameAndAuthority(username, role)
                                                        .flatMap(existingAuth ->
                                                                userRepository.deleteById(username)
                                                                        .then(Mono.<String>error(new IllegalStateException("La autoridad ya existe para este usuario. Se revirtió la creación del usuario.")))
                                                        )
                                                        .switchIfEmpty(
                                                                authorityRepository.insertAuthority(username, role)
                                                                        .thenReturn("Usuario y autoridad creados exitosamente")
                                                        )
                                        )
                                        .onErrorResume(e ->
                                                userRepository.deleteById(username)
                                                        .then(Mono.<String>error(new RuntimeException("Error al crear usuario o autoridad. Se hizo rollback. Detalle: " + e.getMessage())))
                                        );
                            })
                    );
        });
    }
}
