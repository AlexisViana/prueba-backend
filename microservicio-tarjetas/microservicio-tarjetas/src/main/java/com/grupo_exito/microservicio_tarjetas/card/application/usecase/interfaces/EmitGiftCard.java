package com.grupo_exito.microservicio_tarjetas.card.application.usecase.interfaces;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface EmitGiftCard {

    Mono<String> execute(UUID id);

}
