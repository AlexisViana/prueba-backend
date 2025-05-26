package com.grupo_exito.microservicio_tarjetas.card.application.usecase.impl;

import com.grupo_exito.microservicio_tarjetas.card.application.usecase.interfaces.FindGiftCard;
import com.grupo_exito.microservicio_tarjetas.card.domain.Card;
import com.grupo_exito.microservicio_tarjetas.card.infraestructure.repository.GiftCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class FindGiftCardImpl implements FindGiftCard {

    @Autowired
    GiftCardRepository giftCardRepository;

    @Override
    public Mono<Card> execute(UUID id) {
        return giftCardRepository.findById(id);
    }
}
