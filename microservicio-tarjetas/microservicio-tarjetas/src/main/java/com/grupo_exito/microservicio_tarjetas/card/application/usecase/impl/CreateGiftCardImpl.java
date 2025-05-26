package com.grupo_exito.microservicio_tarjetas.card.application.usecase.impl;

import com.grupo_exito.microservicio_tarjetas.card.application.usecase.interfaces.CreateGiftCard;
import com.grupo_exito.microservicio_tarjetas.card.domain.Card;
import com.grupo_exito.microservicio_tarjetas.card.infraestructure.dto.CardCreateDTO;
import com.grupo_exito.microservicio_tarjetas.card.infraestructure.repository.GiftCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class CreateGiftCardImpl implements CreateGiftCard {

    @Autowired
    GiftCardRepository giftCardRepository;

    @Override
    public Mono<String> execute(CardCreateDTO cardCreateDTO) {

        Card card = Card.builder()
                .amount(cardCreateDTO.getAmount())
                .code(cardCreateDTO.getCode())
                .createDate(LocalDateTime.now())
                .expireDate(cardCreateDTO.getExpireDate())
                .build();

        return giftCardRepository.save(card)
                .map(savedCard -> {
                    return "Tarjeta guardada con ID: " + savedCard.getId();
                })
                .onErrorResume(error -> {
                    String errorMsg = "Error al guardar: " + error.getMessage();
                    return Mono.just(errorMsg);
                });
    }
}
