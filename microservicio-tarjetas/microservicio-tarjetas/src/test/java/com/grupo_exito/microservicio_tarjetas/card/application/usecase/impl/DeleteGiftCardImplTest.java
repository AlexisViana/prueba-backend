package com.grupo_exito.microservicio_tarjetas.card.application.usecase.impl;

import com.grupo_exito.microservicio_tarjetas.card.domain.Card;
import com.grupo_exito.microservicio_tarjetas.card.infraestructure.repository.GiftCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeleteGiftCardImplTest {

    private GiftCardRepository giftCardRepository;
    private DeleteGiftCardImpl deleteGiftCard;

    @BeforeEach
    void setUp() {
        giftCardRepository = mock(GiftCardRepository.class);
        deleteGiftCard = new DeleteGiftCardImpl();
        deleteGiftCard.giftCardRepository = giftCardRepository;
    }

    @Test
    void shouldDeleteExistingCardSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        Card card = Card.builder()
                .id(id)
                .amount(BigDecimal.valueOf(100.0))
                .code("DEL123")
                .createDate(LocalDateTime.now())
                .expireDate(LocalDateTime.now().plusDays(10))
                .build();

        when(giftCardRepository.findById(id)).thenReturn(Mono.just(card));
        when(giftCardRepository.delete(card)).thenReturn(Mono.empty());

        StepVerifier.create(deleteGiftCard.execute(id))
                .expectNext(true)
                .verifyComplete();

        verify(giftCardRepository, times(1)).findById(id);
        verify(giftCardRepository, times(1)).delete(card);
    }

    @Test
    void shouldReturnFalseWhenCardDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(giftCardRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(deleteGiftCard.execute(id))
                .expectNext(false)
                .verifyComplete();

        verify(giftCardRepository, times(1)).findById(id);
        verify(giftCardRepository, never()).delete(any());
    }
}