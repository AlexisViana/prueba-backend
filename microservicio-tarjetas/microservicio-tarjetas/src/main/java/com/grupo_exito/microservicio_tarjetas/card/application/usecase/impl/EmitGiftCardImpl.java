package com.grupo_exito.microservicio_tarjetas.card.application.usecase.impl;

import com.grupo_exito.microservicio_tarjetas.card.application.usecase.interfaces.EmitGiftCard;
import com.grupo_exito.microservicio_tarjetas.card.infraestructure.publisher.RabbitMQPublisherService;
import com.grupo_exito.microservicio_tarjetas.card.infraestructure.repository.GiftCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class EmitGiftCardImpl implements EmitGiftCard {

    @Autowired
    GiftCardRepository giftCardRepository;

    @Autowired
    RabbitMQPublisherService reportRabbitMQPublisherService;

    String mensajeJson = "{"
            + "\"to\": \"cliente@ejemplo.com\","
            + "\"subject\": \"Bienvenido a nuestro servicio\","
            + "\"body\": \"Hola Juan Pérez,\\n\\nGracias por registrarte. Tu cuenta ha sido activada exitosamente.\\n\\nSaludos,\\nEquipo de Soporte\","
            + "\"from\": \"notificaciones@miempresa.com\","
            + "\"attachments\": [],"
            + "\"sendDate\": \"2025-05-23T14:30:00\""
            + "}";

    @Override
    public Mono<String> execute(UUID id) {
        return giftCardRepository.findById(id)
                .flatMap(card ->
                        reportRabbitMQPublisherService.sendMessage(mensajeJson)
                                .thenReturn("Mensaje enviado")
                )
                .switchIfEmpty(Mono.error(new IllegalStateException("Tarjeta no encontrada")));
    }
}
