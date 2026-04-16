package com.ym.orderservice.infrastructure.schedule.task;


import com.ym.orderservice.application.service.AsyncMessageService;
import com.ym.orderservice.infrastructure.persistence.entity.async.AsyncMessage;
import com.ym.orderservice.infrastructure.schedule.processor.AsyncMessageSenderProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class AsyncMessageSenderScheduledTask {

    private final AsyncMessageService asyncMessageService;
    private final AsyncMessageSenderProcessor processor;


    @Scheduled(fixedDelay = 3000)
    public void sendOutboxMessages() {
        List<AsyncMessage> messages = asyncMessageService.getUnsentOutboxMessages(50);

        for (AsyncMessage message : messages) {
            processor.sendMessage(message);
        }
    }
}