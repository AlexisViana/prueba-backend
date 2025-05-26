package com.grupo_exito.microservicio_usuarios.user.infraestructure.repository;

import com.grupo_exito.microservicio_usuarios.user.domain.Authority;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AuthorityRepository extends ReactiveCrudRepository<Authority, String> {

    Mono<Void> deleteByUsername(String username);

    Mono<Authority> findByUsernameAndAuthority(String username, String authority);

    @Query("INSERT INTO authorities (username, authority) VALUES (:username, :authority)")
    Mono<Void> insertAuthority(String username, String authority);

}
