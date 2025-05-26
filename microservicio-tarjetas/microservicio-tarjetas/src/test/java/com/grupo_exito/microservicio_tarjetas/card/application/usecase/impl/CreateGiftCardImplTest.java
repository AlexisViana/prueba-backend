package com.grupo_exito.microservicio_tarjetas.card.application.usecase.impl;

import com.grupo_exito.microservicio_tarjetas.card.domain.Card;
import com.grupo_exito.microservicio_tarjetas.card.infraestructure.dto.CardCreateDTO;
import com.grupo_exito.microservicio_tarjetas.card.infraestructure.repository.GiftCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateGiftCardImplTest {

    private GiftCardRepository giftCardRepository;
    private CreateGiftCardImpl createGiftCard;

    @BeforeEach
    void setUp() {
        giftCardRepository = Mockito.mock(GiftCardRepository.class);
        createGiftCard = new CreateGiftCardImpl();
        createGiftCard.giftCardRepository = giftCardRepository;
    }

    @Test
    void shouldSaveGiftCardSuccessfully() {
        CardCreateDTO dto = new CardCreateDTO();
        dto.setAmount(BigDecimal.valueOf(1000.0));
        dto.setCode("TEST123");
        dto.setExpireDate(LocalDateTime.now().plusDays(30));

        UUID id = UUID.randomUUID();

        Card savedCard = Card.builder()
                .id(id)
                .amount(dto.getAmount())
                .code(dto.getCode())
                .createDate(LocalDateTime.now())
                .expireDate(dto.getExpireDate())
                .build();

        when(giftCardRepository.save(any(Card.class))).thenReturn(Mono.just(savedCard));

        StepVerifier.create(createGiftCard.execute(dto))
                .expectNext("Tarjeta guardada con ID: "+ id)
                .verifyComplete();

        verify(giftCardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void shouldHandleSaveErrorGracefully() {
        CardCreateDTO dto = new CardCreateDTO();
        dto.setAmount(BigDecimal.valueOf(500.0));
        dto.setCode("FAIL123");
        dto.setExpireDate(LocalDateTime.now().plusDays(15));

        when(giftCardRepository.save(any(Card.class)))
                .thenReturn(Mono.error(new RuntimeException("Fallo en base de datos")));

        StepVerifier.create(createGiftCard.execute(dto))
                .expectNext("Error al guardar: Fallo en base de datos")
                .verifyComplete();

        verify(giftCardRepository, times(1)).save(any(Card.class));
    }
}