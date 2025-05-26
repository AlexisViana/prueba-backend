package com.grupo_exito.microservicio_usuarios.user.infraestructure.controller;

import com.grupo_exito.microservicio_usuarios.user.application.usecase.interfaces.DeleteUser;
import com.grupo_exito.microservicio_usuarios.user.application.usecase.interfaces.FindAllUser;
import com.grupo_exito.microservicio_usuarios.user.application.usecase.interfaces.RegisterUser;
import com.grupo_exito.microservicio_usuarios.user.application.usecase.interfaces.UpdateUser;
import com.grupo_exito.microservicio_usuarios.user.domain.User;
import com.grupo_exito.microservicio_usuarios.user.infraestructure.dto.UserCreateDto;
import com.grupo_exito.microservicio_usuarios.user.infraestructure.dto.UserUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RegisterUser registerUser;

    @Autowired
    private FindAllUser findAllUser;

    @Autowired
    private UpdateUser updateUser;

    @Autowired
    private DeleteUser deleteUser;

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> registerUser(@RequestBody Mono<UserCreateDto> dtoMono) {
        return registerUser.execute(dtoMono)
                .map(msg -> ResponseEntity.status(HttpStatus.CREATED).body(msg))
                .onErrorResume(e -> {
                    if (e instanceof IllegalStateException && e.getMessage().equals("El usuario ya existe")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()));
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage()));
                    }
                });
    }

    @GetMapping("/search-all-users")
    public Mono<ResponseEntity<Flux<User>>> getAllUsers() {
        Flux<User> users = findAllUser.execute();
        return Mono.just(ResponseEntity.ok().body(users));
    }

    @PutMapping("/update-user/{username}")
    public Mono<ResponseEntity<String>> updateUser(
            @PathVariable String username,
            @RequestBody Mono<UserUpdateDto> userUpdateDtoMono) {

        return updateUser.execute(username, userUpdateDtoMono)
                .map(message -> {
                    if ("NOT_FOUND".equals(message)) {
                        return ResponseEntity.notFound().build();
                    }
                    return ResponseEntity.ok(message);
                });
    }

    @DeleteMapping("/delete-user/{username}")
    public Mono<ResponseEntity<String>> deleteUser(@PathVariable String username) {
        return deleteUser.execute(username)
                .map(message -> {
                    if ("NOT_FOUND".equals(message)) {
                        return ResponseEntity.notFound().build();
                    }
                    return ResponseEntity.ok(message);
                });
    }
}