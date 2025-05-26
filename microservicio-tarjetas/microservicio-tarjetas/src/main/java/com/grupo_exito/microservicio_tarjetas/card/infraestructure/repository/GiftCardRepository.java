package com.grupo_exito.microservicio_tarjetas.card.infraestructure.repository;

import com.grupo_exito.microservicio_tarjetas.card.domain.Card;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface GiftCardRepository extends ReactiveCrudRepository<Card, UUID> {
}
