package com.ym.orderservice.infrastructure.persistence.entity.common;

import jakarta.persistence.Column;
import org.hibernate.annotations.ColumnDefault;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.domain.Persistable;

import java.time.OffsetDateTime;

@MappedSuperclass
public abstract class PersistableEntity<ID> implements Persistable<ID> {

    @Column(name = "created_at", insertable = false, updatable = false)
    @ColumnDefault("now()")
    private OffsetDateTime createdAt;

    @Override
    public boolean isNew() {
        return createdAt == null;
    }
}
