package com.ym.orderservice.infrastructure.persistence.repository;

import com.ym.orderservice.infrastructure.persistence.entity.async.AsyncMessage;
import com.ym.orderservice.infrastructure.persistence.entity.async.AsyncMessageId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AsyncMessageRepository extends JpaRepository<AsyncMessage, AsyncMessageId> {

    @Query("SELECT m FROM AsyncMessage m WHERE m.status = 'CREATED' AND m.type = 'OUTBOX' ORDER BY m.createdAt")
    List<AsyncMessage> findUnsentOutboxMessages(Pageable pageable);
}