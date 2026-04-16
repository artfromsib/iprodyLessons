package com.ym.orderservice.application.service;

import com.ym.orderservice.infrastructure.persistence.entity.async.AsyncMessage;

import java.util.List;

public interface AsyncMessageService {

    void saveMessage(AsyncMessage message);

    List<AsyncMessage> getUnsentOutboxMessages(int batchSize);

    void markAsSent(AsyncMessage message);
}

