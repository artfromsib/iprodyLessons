package src.main.java.com.ym.integration.order.listener;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import src.main.java.com.ym.entity.ShopRecord;
import src.main.java.com.ym.integration.order.dto.OrderCreationStatus;
import src.main.java.com.ym.integration.order.dto.OrderCreationStatusMessage;
import src.main.java.com.ym.repository.ShopRecordRepository;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class OrderCreationStatusListener {
    private final ShopRecordRepository shopRecordRepository;
    private final ObjectMapper mapper;


    @KafkaListener(
            topics = "${kafka.service.order.order-creation-status-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    @Transactional
    public void consume(String messageStr,
                        Acknowledgment ack) throws JsonProcessingException {
        log.info("Received message from Kafka: {}", messageStr);

        OrderCreationStatusMessage message = mapper.readValue(messageStr, OrderCreationStatusMessage.class);
        log.debug("Parsing completed. Message: {}", message);

        UUID orderId = message.orderId();
        OrderCreationStatus status = message.status();
        ShopRecord shopRecord;

        if (status == OrderCreationStatus.ORDER_CREATE) {
            shopRecord = ShopRecord.builder()
                    .id(orderId.toString())
                    .orderStatus(status.toString())
                    .customerId(message.customerId())
                    .build();
            log.info("Created new ShopRecord for ORDER_CREATE with ID: {}", shopRecord.getId());
        } else {
            shopRecord = shopRecordRepository.findById(orderId.toString())
                    .orElseThrow(() -> {
                        String errorMsg = "ShopRecord not found with ID: " + orderId;
                        log.error(errorMsg);
                        return new RuntimeException(errorMsg);
                    });
            log.info("Updating ShopRecord with ID: {}", shopRecord.getId());

            if (status == OrderCreationStatus.PAYMENT_SUCCESSFUL) {
                shopRecord.setPaymentAmount(message.amount());
                log.info("Updated amount: {}", message.amount());
            } else if (status == OrderCreationStatus.DELIVERY_CREATED) {
                shopRecord.setTrackingNumber(message.trackingNumber());
                log.info("Updated trackingNumber: {}", message.trackingNumber());
            }

            shopRecord.setOrderStatus(status.name());
            log.info("Status updated to: {}", status.name());
        }

        shopRecordRepository.save(shopRecord);
        log.info("shopRecord with ID {} has been saved", shopRecord.getId());

        ack.acknowledge();
        log.info("Message acknowledged");
    }
}
