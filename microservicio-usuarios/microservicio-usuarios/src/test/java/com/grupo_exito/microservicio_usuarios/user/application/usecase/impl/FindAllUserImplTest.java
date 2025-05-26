package com.grupo_exito.microservicio_usuarios.user.application.usecase.impl;

import com.grupo_exito.microservicio_usuarios.user.domain.User;
import com.grupo_exito.microservicio_usuarios.user.infraestructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class FindAllUserImplTest {

    private UserRepository userRepository;
    private FindAllUserImpl findAllUser;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        findAllUser = new FindAllUserImpl();
        findAllUser.userRepository = userRepository;
    }

    @Test
    void shouldReturnAllUsers() {
        List<User> userList = Arrays.asList(
                User.builder().username("user1").build(),
                User.builder().username("user2").build()
        );

        when(userRepository.findAll()).thenReturn(Flux.fromIterable(userList));

        StepVerifier.create(findAllUser.execute())
                .expectNext(userList.get(0))
                .expectNext(userList.get(1))
                .verifyComplete();

        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnEmptyWhenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(findAllUser.execute())
                .verifyComplete();

        verify(userRepository).findAll();
    }
}
