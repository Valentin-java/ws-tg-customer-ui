package com.workers.persistence.repository;

import com.workers.persistence.entity.DraftOrderEntity;
import com.workers.persistence.enums.OrderStatus;
import com.workers.persistence.enums.SendProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DraftOrderRepository extends JpaRepository<DraftOrderEntity, Long> {

    List<DraftOrderEntity> findByStatusAndSendProcess(OrderStatus status, SendProcess sendProcess);
}
