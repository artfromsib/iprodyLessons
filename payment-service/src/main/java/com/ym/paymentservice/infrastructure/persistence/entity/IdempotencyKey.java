package com.ym.paymentservice.infrastructure.persistence.entity;

import com.ym.paymentservice.domain.model.enums.KeyStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "idempotency_key")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "key")
public class IdempotencyKey {
    @Id
    @Column(name = "key_value")
    private String key;

    @Enumerated(EnumType.STRING)
    private KeyStatus status;
    @Lob
    private String response;

    private int statusCode;

    public IdempotencyKey(String key, KeyStatus status) {
        this.key = key;
        this.status = status;
    }
}
