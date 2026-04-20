package com.ym.orderservice.integration.delivery.listener;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.ym.orderservice.application.service.AsyncMessageService;
import com.ym.orderservice.infrastructure.listener.IdempotentKafkaListener;
import com.ym.orderservice.integration.delivery.dto.response.DeliveryCreatedResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

@Component
@Slf4j
public class DeliveryCreatedListener extends IdempotentKafkaListener<DeliveryCreatedResponseMessage> {

    public DeliveryCreatedListener(AsyncMessageService asyncMessageService,
                                   JsonMapper mapper) {
        super(asyncMessageService, mapper);
    }

    @KafkaListener(
        topics = "${kafka.service.delivery.delivery-creation-topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    @Override
    public void consume(ConsumerRecord<String, DeliveryCreatedResponseMessage> consumerRecord,
                        DeliveryCreatedResponseMessage message,
                        Acknowledgment ack) throws JsonProcessingException {
        super.consume(consumerRecord, message, ack);
    }

    @Override
    @Transactional
    public void processConsumedMessage(DeliveryCreatedResponseMessage message) {
        log.info("Consumed delivery created response message: " + message);
    }
}