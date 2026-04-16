package com.ym.orderservice.application.service.impl;


import com.ym.orderservice.application.service.AsyncMessageService;
import com.ym.orderservice.infrastructure.persistence.entity.async.AsyncMessage;
import com.ym.orderservice.infrastructure.persistence.enums.AsyncMessageStatus;
import com.ym.orderservice.infrastructure.persistence.repository.AsyncMessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AsyncMessageServiceImpl implements AsyncMessageService {

    private final AsyncMessageRepository repository;


    @Override
    @Transactional
    public void saveMessage(AsyncMessage message) {
        repository.save(message);
    }

    @Override
    public List<AsyncMessage> getUnsentOutboxMessages(int batchSize) {
        Pageable pageable = Pageable.ofSize(batchSize);
        return repository.findUnsentOutboxMessages(pageable);
    }


    @Override
    public void markAsSent(AsyncMessage message) {
        message.setStatus(AsyncMessageStatus.SENT);
        repository.save(message);
    }
}