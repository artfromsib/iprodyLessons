package com.ym.orderservice.infrastructure.schedule.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ym.orderservice.application.exception.SendingAsyncMessageException;
import com.ym.orderservice.application.service.AsyncMessageService;
import com.ym.orderservice.infrastructure.persistence.entity.async.AsyncMessage;
import com.ym.orderservice.integration.delivery.dto.request.OrderPaidRequestMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;



@Component
@RequiredArgsConstructor
@Slf4j
public class AsyncMessageSenderProcessor {

    private final AsyncMessageService asyncMessageService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper mapper;

    @Transactional
    public void sendMessage(AsyncMessage message) {
        try {
            var reqMessage = mapper.readValue(message.getValue(), OrderPaidRequestMessage.class);

            kafkaTemplate.send(message.getTopic(), message.getId().getId(), reqMessage)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Error sending message: {}", message, ex);
                        } else {
                            asyncMessageService.markAsSent(message);
                            log.info("Message sent successfully: {}", message.getId().getId());
                        }
                    });
        } catch (Exception e) {
            throw new SendingAsyncMessageException("Error on sending message '%s'".formatted(message), e);
        }
    }
}