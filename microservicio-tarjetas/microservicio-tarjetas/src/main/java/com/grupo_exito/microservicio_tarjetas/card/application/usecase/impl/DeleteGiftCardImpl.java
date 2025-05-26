package com.grupo_exito.microservicio_tarjetas.card.application.usecase.impl;

import com.grupo_exito.microservicio_tarjetas.card.application.usecase.interfaces.DeleteGiftCard;
import com.grupo_exito.microservicio_tarjetas.card.infraestructure.repository.GiftCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class DeleteGiftCardImpl implements DeleteGiftCard {

    @Autowired
    GiftCardRepository giftCardRepository;

    @Override
    public Mono<Boolean> execute(UUID id) {
        return giftCardRepository.findById(id)
                .flatMap(card -> giftCardRepository.delete(card).thenReturn(true))
                .switchIfEmpty(Mono.just(false));
    }
}
