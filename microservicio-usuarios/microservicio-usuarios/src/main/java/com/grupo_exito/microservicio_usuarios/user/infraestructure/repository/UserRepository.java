package com.grupo_exito.microservicio_usuarios.user.infraestructure.repository;

import com.grupo_exito.microservicio_usuarios.user.domain.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, String> {

    Mono<User> findByUsername(String username);

    @Query("INSERT INTO users (username, password, enabled) VALUES (:username, :password, :enabled)")
    Mono<Void> insertUser(String username, String password, boolean enabled);
}
