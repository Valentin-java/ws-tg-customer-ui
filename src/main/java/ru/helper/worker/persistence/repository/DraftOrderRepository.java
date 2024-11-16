package ru.helper.worker.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.helper.worker.persistence.entity.DraftOrderEntity;

public interface DraftOrderRepository extends JpaRepository<DraftOrderEntity, Long> {
}
