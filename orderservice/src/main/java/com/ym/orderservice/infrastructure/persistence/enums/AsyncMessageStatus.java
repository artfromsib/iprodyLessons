package com.ym.orderservice.infrastructure.persistence.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AsyncMessageStatus {
    CREATED("CREATED"), SENT("SENT"), RECEIVED("RECEIVED"), PROCESSED("PROCESSED");

    private final String code;
}
