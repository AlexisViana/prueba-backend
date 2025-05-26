package com.grupo_exito.microservicio_tarjetas.card.application.usecase.interfaces;

import com.grupo_exito.microservicio_tarjetas.card.infraestructure.dto.CardCreateDTO;
import reactor.core.publisher.Mono;

public interface CreateGiftCard {

    Mono<String> execute(CardCreateDTO cardCreateDTO);
}
