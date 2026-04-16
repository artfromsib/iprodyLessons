package com.ym.orderservice.infrastructure.persistence.entity.async;


import com.ym.orderservice.infrastructure.persistence.entity.common.PersistableEntity;
import com.ym.orderservice.infrastructure.persistence.enums.AsyncMessageStatus;
import com.ym.orderservice.infrastructure.persistence.enums.AsyncMessageType;
import com.ym.orderservice.infrastructure.persistence.entity.async.AsyncMessageId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.domain.Persistable;


import java.time.OffsetDateTime;

@Entity
@Table(name = "async_messages")
@Getter
@Setter
@EqualsAndHashCode(of = {"id", "topic"}, callSuper = false)
@IdClass(AsyncMessageId.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsyncMessage extends PersistableEntity<AsyncMessageId> {


    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Id
    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "headers")
    private String headers;

    @Column(name = "val", nullable = false)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AsyncMessageType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AsyncMessageStatus status;

    @Override
    @Transient
    public AsyncMessageId getId() {
        return new AsyncMessageId(id, topic);
    }
}
