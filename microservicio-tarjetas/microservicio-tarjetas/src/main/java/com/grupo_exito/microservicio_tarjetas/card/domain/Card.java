package com.grupo_exito.microservicio_tarjetas.card.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table("cards")
public class Card {

    @Id
    UUID id;
    String code;
    BigDecimal amount;
    LocalDateTime createDate;
    LocalDateTime expireDate;

}
