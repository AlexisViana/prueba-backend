package com.grupo_exito.microservicio_tarjetas.card.application.usecase.impl;

import com.grupo_exito.microservicio_tarjetas.card.application.usecase.interfaces.UpdateGiftCard;
import com.grupo_exito.microservicio_tarjetas.card.domain.Card;
import com.grupo_exito.microservicio_tarjetas.card.infraestructure.dto.CardUpdateDTO;
import com.grupo_exito.microservicio_tarjetas.card.infraestructure.repository.GiftCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UpdateGiftCardImpl implements UpdateGiftCard {

    @Autowired
    GiftCardRepository giftCardRepository;

    @Override
    public Mono<Card> execute(UUID id, CardUpdateDTO updatedCard) {
        return giftCardRepository.findById(id)
                .flatMap(existingCard -> {
                    existingCard.setCode(updatedCard.getCode());
                    existingCard.setAmount(updatedCard.getAmount());
                    existingCard.setExpireDate(updatedCard.getExpireDate());
                    return giftCardRepository.save(existingCard);
                });
    }
}
