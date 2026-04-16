package com.ym.orderservice.infrastructure.persistence.entity.async;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AsyncMessageId implements Serializable {

    private String id;

    private String topic;
}
