package com.grupo_exito.microservicio_tarjetas.card.infraestructure.config;

import com.rabbitmq.client.ConnectionFactory;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.*;

@Configuration
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RabbitMQConfig {

    @Value("${spring.amqp.host}")
    String host;

    @Value("${spring.amqp.port}")
    int port;

    @Value("${spring.amqp.username}")
    String username;

    @Value("${spring.amqp.password}")
    String password;

    @Bean
    public ConnectionFactory rabbitMQconnectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.useNio();
        return connectionFactory;
    }

    @Bean
    public ReceiverOptions receiverOptions(ConnectionFactory rabbitMQconnectionFactory) {
        ReceiverOptions receiverOptions = new ReceiverOptions();
        receiverOptions.connectionFactory(rabbitMQconnectionFactory);
        receiverOptions.connectionSubscriptionScheduler(Schedulers.boundedElastic());
        return receiverOptions;
    }

    @Bean
    public Receiver receiver(ReceiverOptions receiverOptions) {
        return RabbitFlux.createReceiver(receiverOptions);
    }


    @Bean
    public SenderOptions senderOptions(ConnectionFactory connectionFactory) {
        SenderOptions senderOptions = new SenderOptions();
        senderOptions.connectionFactory(connectionFactory);
        senderOptions.connectionSubscriptionScheduler(Schedulers.boundedElastic());
        return senderOptions;
    }

    @Bean
    public Sender sender(SenderOptions senderOptions) {
        return RabbitFlux.createSender(senderOptions);
    }
}
