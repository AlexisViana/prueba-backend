package com.grupo_exito.microservicio_usuarios.user.application.usecase.impl;

import com.grupo_exito.microservicio_usuarios.user.domain.Authority;
import com.grupo_exito.microservicio_usuarios.user.domain.Role;
import com.grupo_exito.microservicio_usuarios.user.domain.User;
import com.grupo_exito.microservicio_usuarios.user.infraestructure.dto.UserCreateDto;
import com.grupo_exito.microservicio_usuarios.user.infraestructure.repository.AuthorityRepository;
import com.grupo_exito.microservicio_usuarios.user.infraestructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RegisterUserImplTest {

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private RegisterUserImpl registerUser;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        authorityRepository = mock(AuthorityRepository.class);
        registerUser = new RegisterUserImpl();
        registerUser.userRepository = userRepository;
        registerUser.authorityRepository = authorityRepository;
    }

    @Test
    void shouldReturnErrorIfUserAlreadyExists() {
        UserCreateDto dto = UserCreateDto.builder()
                .username("existingUser")
                .password("password123")
                .build();

        when(userRepository.findByUsername("existingUser"))
                .thenReturn(Mono.just(new User()));

        StepVerifier.create(registerUser.execute(Mono.just(dto)))
                .expectErrorSatisfies(error ->
                        assertTrue(error instanceof IllegalStateException &&
                                error.getMessage().equals("El usuario ya existe"))
                )
                .verify();

        verify(userRepository).findByUsername("existingUser");
        verifyNoMoreInteractions(userRepository, authorityRepository);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        UserCreateDto dto = UserCreateDto.builder()
                .username("newUser")
                .password("password123")
                .build();

        when(userRepository.findByUsername("newUser")).thenReturn(Mono.empty());
        when(userRepository.insertUser(eq("newUser"), startsWith("{bcrypt}"), eq(true)))
                .thenReturn(Mono.empty());
        when(authorityRepository.findByUsernameAndAuthority("newUser", Role.ROLE_USER.name()))
                .thenReturn(Mono.empty());
        when(authorityRepository.insertAuthority("newUser", Role.ROLE_USER.name()))
                .thenReturn(Mono.empty());

        StepVerifier.create(registerUser.execute(Mono.just(dto)))
                .expectNext("Usuario y autoridad creados exitosamente")
                .verifyComplete();

        verify(userRepository).insertUser(eq("newUser"), startsWith("{bcrypt}"), eq(true));
        verify(authorityRepository).insertAuthority("newUser", Role.ROLE_USER.name());
    }

}