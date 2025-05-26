package com.grupo_exito.microservicio_usuarios.user.application.usecase.impl;

import com.grupo_exito.microservicio_usuarios.user.domain.User;
import com.grupo_exito.microservicio_usuarios.user.infraestructure.repository.AuthorityRepository;
import com.grupo_exito.microservicio_usuarios.user.infraestructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class DeleteUserImplTest {

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private DeleteUserImpl deleteUser;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        authorityRepository = mock(AuthorityRepository.class);
        deleteUser = new DeleteUserImpl();
        deleteUser.userRepository = userRepository;
        deleteUser.authorityRepository = authorityRepository;
    }

    @Test
    void shouldDeleteUserWhenFound() {
        String username = "jdoe";
        User user = User.builder().username(username).build();

        when(userRepository.findByUsername(username)).thenReturn(Mono.just(user));
        when(authorityRepository.deleteByUsername(username)).thenReturn(Mono.empty());
        when(userRepository.delete(user)).thenReturn(Mono.empty());

        StepVerifier.create(deleteUser.execute(username))
                .expectNext("Usuario eliminado correctamente")
                .verifyComplete();

        verify(userRepository).findByUsername(username);
        verify(authorityRepository).deleteByUsername(username);
        verify(userRepository).delete(user);
    }

    @Test
    void shouldReturnMessageWhenUserNotFound() {
        String username = "not_found_user";
        when(userRepository.findByUsername(username)).thenReturn(Mono.empty());

        StepVerifier.create(deleteUser.execute(username))
                .expectNext("Usuario no encontrado o ya fue eliminado")
                .verifyComplete();

        verify(userRepository).findByUsername(username);
        verify(authorityRepository, never()).deleteByUsername(any());
        verify(userRepository, never()).delete(any());
    }
}