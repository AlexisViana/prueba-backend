package com.grupo_exito.microservicio_usuarios.user.infraestructure.dto;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateDto {

    String username;
    String password;
    boolean enabled;
}
