package com.grupo_exito.microservicio_tarjetas.card.application.usecase.interfaces;

import com.grupo_exito.microservicio_tarjetas.card.domain.Card;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface FindGiftCard {

    Mono<Card> execute(UUID id);

}
