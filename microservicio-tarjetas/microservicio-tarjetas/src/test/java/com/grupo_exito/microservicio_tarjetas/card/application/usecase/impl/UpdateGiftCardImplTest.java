package com.grupo_exito.microservicio_tarjetas.card.application.usecase.impl;

import com.grupo_exito.microservicio_tarjetas.card.domain.Card;
import com.grupo_exito.microservicio_tarjetas.card.infraestructure.dto.CardUpdateDTO;
import com.grupo_exito.microservicio_tarjetas.card.infraestructure.repository.GiftCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

class UpdateGiftCardImplTest {

    private GiftCardRepository giftCardRepository;
    private UpdateGiftCardImpl updateGiftCard;

    @BeforeEach
    void setUp() {
        giftCardRepository = mock(GiftCardRepository.class);
        updateGiftCard = new UpdateGiftCardImpl();
        updateGiftCard.giftCardRepository = giftCardRepository;
    }

    @Test
    void shouldUpdateCardWhenFound() {
        // Arrange
        UUID cardId = UUID.randomUUID();
        Card existingCard = Card.builder()
                .id(cardId)
                .code("OLD123")
                .amount(BigDecimal.valueOf(50.0))
                .expireDate(LocalDateTime.now().plusDays(10))
                .build();

        CardUpdateDTO updateDTO = new CardUpdateDTO();
        updateDTO.setCode("NEW456");
        updateDTO.setAmount(BigDecimal.valueOf(200.0));
        updateDTO.setExpireDate(LocalDateTime.now().plusDays(60));

        Card updatedCard = Card.builder()
                .id(cardId)
                .code(updateDTO.getCode())
                .amount(updateDTO.getAmount())
                .expireDate(updateDTO.getExpireDate())
                .build();

        when(giftCardRepository.findById(cardId)).thenReturn(Mono.just(existingCard));
        when(giftCardRepository.save(existingCard)).thenReturn(Mono.just(updatedCard));

        StepVerifier.create(updateGiftCard.execute(cardId, updateDTO))
                .expectNext(updatedCard)
                .verifyComplete();

        verify(giftCardRepository).findById(cardId);
        verify(giftCardRepository).save(existingCard);
    }

    @Test
    void shouldReturnEmptyWhenCardNotFound() {
        UUID cardId = UUID.randomUUID();
        CardUpdateDTO updateDTO = new CardUpdateDTO();
        updateDTO.setCode("NEW456");
        updateDTO.setAmount(BigDecimal.valueOf(200.0));
        updateDTO.setExpireDate(LocalDateTime.now().plusDays(60));

        when(giftCardRepository.findById(cardId)).thenReturn(Mono.empty());

        StepVerifier.create(updateGiftCard.execute(cardId, updateDTO))
                .verifyComplete();

        verify(giftCardRepository).findById(cardId);
        verify(giftCardRepository, never()).save(any());
    }
}