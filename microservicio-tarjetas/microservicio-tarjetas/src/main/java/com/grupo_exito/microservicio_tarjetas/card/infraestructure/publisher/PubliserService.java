package com.grupo_exito.microservicio_tarjetas.card.infraestructure.publisher;

import reactor.core.publisher.Mono;

public interface PubliserService {

    Mono<Boolean> sendMessage(String message);

}
