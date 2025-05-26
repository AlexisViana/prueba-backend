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

import static org.mockito.Mockito.*;

class FindGiftCardImplTest {

    private GiftCardRepository giftCardRepository;
    private FindGiftCardImpl findGiftCard;

    @BeforeEach
    void setUp() {
        giftCardRepository = mock(GiftCardRepository.class);
        findGiftCard = new FindGiftCardImpl();
        findGiftCard.giftCardRepository = giftCardRepository;
    }

    @Test
    void shouldReturnCardWhenFound() {
        // Arrange
        UUID cardId = UUID.randomUUID();
        Card card = Card.builder()
                .id(cardId)
                .code("GIFT123")
                .amount(BigDecimal.valueOf(100.0))
                .createDate(LocalDateTime.now())
                .expireDate(LocalDateTime.now().plusDays(30))
                .build();

        when(giftCardRepository.findById(cardId)).thenReturn(Mono.just(card));

        StepVerifier.create(findGiftCard.execute(cardId))
                .expectNext(card)
                .verifyComplete();

        verify(giftCardRepository, times(1)).findById(cardId);
    }

    @Test
    void shouldReturnEmptyWhenCardNotFound() {
        UUID cardId = UUID.randomUUID();
        when(giftCardRepository.findById(cardId)).thenReturn(Mono.empty());

        StepVerifier.create(findGiftCard.execute(cardId))
                .verifyComplete();

        verify(giftCardRepository, times(1)).findById(cardId);
    }
}