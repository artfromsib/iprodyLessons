package com.ym.orderservice.infrastructure.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ym.orderservice.application.service.AsyncMessageService;
import com.ym.orderservice.infrastructure.persistence.entity.async.AsyncMessage;
import com.ym.orderservice.infrastructure.persistence.enums.AsyncMessageStatus;
import com.ym.orderservice.infrastructure.persistence.enums.AsyncMessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.support.Acknowledgment;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Slf4j
public abstract class IdempotentKafkaListener<T> {

    private final AsyncMessageService asyncMessageService;
    private final JsonMapper mapper;

    public void consume(ConsumerRecord<String, T> consumerRecord,
                        T message,
                        Acknowledgment acknowledgment) throws JsonProcessingException {
        Header idempotentKeyHeader = consumerRecord.headers().lastHeader("X-Idempotency-Key");
        if (idempotentKeyHeader == null) {
            log.error("Idempotent key header is null for consumer record " + consumerRecord);
            acknowledgment.acknowledge();
            return;
        }

        String idempotentKey = new String(idempotentKeyHeader.value(), StandardCharsets.UTF_8);


        AsyncMessage asyncConsumedMessage = AsyncMessage.builder()
            .id(idempotentKey)
            .topic(consumerRecord.topic())
            .value(mapper.writeValueAsString(message))
            .status(AsyncMessageStatus.RECEIVED)
            .type(AsyncMessageType.INBOX)
            .build();

        try {
            asyncMessageService.saveMessage(asyncConsumedMessage);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Message with the same idempotent key is present in DB: " + idempotentKey);
            acknowledgment.acknowledge();
            return;
        }

        processConsumedMessage(message);
        acknowledgment.acknowledge();
    }

    public abstract void processConsumedMessage(T message);
}