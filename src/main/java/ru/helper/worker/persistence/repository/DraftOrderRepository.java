package ru.helper.worker.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.helper.worker.persistence.entity.DraftOrderEntity;
import ru.helper.worker.persistence.enums.OrderStatus;
import ru.helper.worker.persistence.enums.SendProcess;

import java.util.List;

public interface DraftOrderRepository extends JpaRepository<DraftOrderEntity, Long> {

    List<DraftOrderEntity> findByStatusAndSendProcess(OrderStatus status, SendProcess sendProcess);
}
