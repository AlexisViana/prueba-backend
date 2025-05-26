package com.grupo_exito.microservicio_usuarios.user.application.usecase.impl;

import com.grupo_exito.microservicio_usuarios.user.domain.User;
import com.grupo_exito.microservicio_usuarios.user.infraestructure.dto.UserUpdateDto;
import com.grupo_exito.microservicio_usuarios.user.infraestructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateUserImplTest {

    private UserRepository userRepository;
    private UpdateUserImpl updateUser;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        updateUser = new UpdateUserImpl();
        updateUser.userRepository = userRepository;
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        String username = "testUser";
        UserUpdateDto dto = UserUpdateDto.builder()
                .password("newPassword")
                .enabled(true)
                .build();
        User existingUser = new User();
        existingUser.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Mono.just(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(existingUser));

        StepVerifier.create(updateUser.execute(username, Mono.just(dto)))
                .expectNext("Usuario actualizado correctamente")
                .verifyComplete();

        verify(userRepository).findByUsername(username);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() {
        String username = "nonexistentUser";
        UserUpdateDto dto = UserUpdateDto.builder()
                .password("somePassword")
                .enabled(false)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Mono.empty());

        StepVerifier.create(updateUser.execute(username, Mono.just(dto)))
                .expectNext("NOT_FOUND")
                .verifyComplete();

        verify(userRepository).findByUsername(username);
        verify(userRepository, never()).save(any());
    }
}