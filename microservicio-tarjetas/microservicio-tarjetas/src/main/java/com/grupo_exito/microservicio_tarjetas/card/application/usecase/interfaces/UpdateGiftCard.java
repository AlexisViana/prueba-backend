package com.grupo_exito.microservicio_tarjetas.card.application.usecase.interfaces;

import com.grupo_exito.microservicio_tarjetas.card.domain.Card;
import com.grupo_exito.microservicio_tarjetas.card.infraestructure.dto.CardUpdateDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UpdateGiftCard {

    Mono<Card> execute(UUID id, CardUpdateDTO cardUpdateDTO);

}
