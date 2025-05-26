package com.grupo_exito.microservicio_tarjetas.card.infraestructure.publisher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

@Slf4j
@Service
public class RabbitMQPublisherServiceImpl implements RabbitMQPublisherService {

    @Autowired
    Sender sender;

    @Value("${spring.amqp.card-exchange}")
    String ReportExchange;

    @Override
    public Mono<Boolean> sendMessage(String message) {
        return sender.send(Mono.just(new OutboundMessage(ReportExchange, "", message.getBytes())))
                .doOnSuccess(unused -> log.info("Message sent: {}", message))
                .thenReturn(true)
                .onErrorReturn(false);
    }
}
