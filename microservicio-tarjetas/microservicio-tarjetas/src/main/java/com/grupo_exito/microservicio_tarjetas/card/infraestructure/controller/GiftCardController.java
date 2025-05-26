package com.grupo_exito.microservicio_tarjetas.card.infraestructure.controller;

import com.grupo_exito.microservicio_tarjetas.card.application.usecase.interfaces.*;
import com.grupo_exito.microservicio_tarjetas.card.domain.Card;
import com.grupo_exito.microservicio_tarjetas.card.infraestructure.dto.CardCreateDTO;
import com.grupo_exito.microservicio_tarjetas.card.infraestructure.dto.CardUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/card")
public class GiftCardController {

    @Autowired
    private CreateGiftCard createGiftCard;

    @Autowired
    private FindGiftCard findGiftCard;

    @Autowired
    private DeleteGiftCard deleteGiftCard;

    @Autowired
    private UpdateGiftCard updateGiftCard;

    @Autowired
    private EmitGiftCard emitGiftCard;

    @PostMapping("/create")
    public Mono<ResponseEntity<String>> createGiftCard(@RequestBody CardCreateDTO dto) {
        return createGiftCard.execute(dto)
                .map(msg -> ResponseEntity.status(HttpStatus.CREATED).body(msg))
                .onErrorResume(e -> Mono.just(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage()))
                );
    }

    @GetMapping("/gift-card/{id}")
    public Mono<ResponseEntity<Card>> findById(@PathVariable UUID id) {
        return findGiftCard.execute(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/gift-card/{id}")
    public Mono<ResponseEntity<Void>> deleteGiftCard(@PathVariable UUID id) {
        return deleteGiftCard.execute(id)
                .map(deleted -> deleted
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.notFound().build()
                );
    }

    @PutMapping("/gift-card/{id}")
    public Mono<ResponseEntity<Card>> updateCard(@PathVariable UUID id, @RequestBody CardUpdateDTO dto) {
        return updateGiftCard.execute(id, dto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/emit/{id}")
    public Mono<ResponseEntity<String>> emitCard(@PathVariable UUID id) {
        return emitGiftCard.execute(id)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    if (error instanceof IllegalStateException) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    return Mono.just(ResponseEntity.internalServerError().body("Error al emitir tarjeta"));
                });
    }

}