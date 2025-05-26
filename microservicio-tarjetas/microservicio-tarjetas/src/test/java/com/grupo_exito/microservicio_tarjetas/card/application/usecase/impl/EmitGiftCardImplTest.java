package com.grupo_exito.microservicio_tarjetas.card.application.usecase.impl;

import com.grupo_exito.microservicio_tarjetas.card.domain.Card;
import com.grupo_exito.microservicio_tarjetas.card.infraestructure.publisher.RabbitMQPublisherService;
import com.grupo_exito.microservicio_tarjetas.card.infraestructure.repository.GiftCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

class EmitGiftCardImplTest {

    private GiftCardRepository giftCardRepository;
    private RabbitMQPublisherService rabbitMQPublisherService;
    private EmitGiftCardImpl emitGiftCard;

    @BeforeEach
    void setUp() {
        giftCardRepository = mock(GiftCardRepository.class);
        rabbitMQPublisherService = mock(RabbitMQPublisherService.class);

        emitGiftCard = new EmitGiftCardImpl();
        emitGiftCard.giftCardRepository = giftCardRepository;
        emitGiftCard.reportRabbitMQPublisherService = rabbitMQPublisherService;
    }

    @Test
    void shouldEmitMessageSuccessfullyWhenCardExists() {
        UUID cardId = UUID.randomUUID();
        Card card = Card.builder()
                .id(cardId)
                .amount(BigDecimal.valueOf(150.0))
                .code("XYZ987")
                .createDate(LocalDateTime.now())
                .expireDate(LocalDateTime.now().plusMonths(6))
                .build();

        when(giftCardRepository.findById(cardId)).thenReturn(Mono.just(card));
        when(rabbitMQPublisherService.sendMessage(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(emitGiftCard.execute(cardId))
                .expectNext("Mensaje enviado")
                .verifyComplete();

        verify(giftCardRepository, times(1)).findById(cardId);
        verify(rabbitMQPublisherService, times(1)).sendMessage(anyString());
    }

    @Test
    void shouldReturnErrorWhenCardDoesNotExist() {
        UUID cardId = UUID.randomUUID();
        when(giftCardRepository.findById(cardId)).thenReturn(Mono.empty());

        StepVerifier.create(emitGiftCard.execute(cardId))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalStateException &&
                                throwable.getMessage().equals("Tarjeta no encontrada")
                )
                .verify();

        verify(giftCardRepository, times(1)).findById(cardId);
        verify(rabbitMQPublisherService, never()).sendMessage(anyString());
    }
}