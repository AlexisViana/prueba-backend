package com.grupo_exito.microservicio_tarjetas.card.infraestructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CardUpdateDTO {

    String code;
    BigDecimal amount;
    LocalDateTime expireDate;

    public CardUpdateDTO() {
    }
}
