package ru.helper.worker.business.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.helper.worker.controller.events.MessageSendEvent;
import ru.helper.worker.persistence.entity.DraftOrderEntity;
import ru.helper.worker.persistence.enums.OrderStatus;
import ru.helper.worker.persistence.enums.SendProcess;
import ru.helper.worker.persistence.mapper.DraftOrderMapper;
import ru.helper.worker.persistence.repository.DraftOrderRepository;
import ru.helper.worker.rest.external.common.ExternalClientService;
import ru.helper.worker.rest.external.create_order.model.OrderCreateRequest;
import ru.helper.worker.rest.external.create_order.model.OrderCreateResponseDto;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class DraftOrderScheduler {

    private final ExternalClientService<OrderCreateRequest, ResponseEntity<OrderCreateResponseDto>> orderClient;
    private final ApplicationEventPublisher eventPublisher;
    private final DraftOrderRepository orderRepository;
    private final DraftOrderMapper draftOrderMapper;

    @Scheduled(fixedDelayString = "${order-service.scheduler.fixed-rate:3600000}")
    public void retrySendDraftOrder() {
        try {
            var futures = orderRepository.findByStatusAndSendProcess(OrderStatus.DRAFT, SendProcess.AUTO).stream()
                    .map(this::processDraftOrder) // Асинхронная обработка каждого черновика
                    .toList();

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .exceptionally(e -> {
                        log.error("Error occurred during scheduled draft processing", e);
                        return null;
                    })
                    .join();
        } catch (Exception e) {
            log.error("Error occurred while retrying to send draft orders: ", e);
        }
    }

    @Async("order_service_executor")
    public CompletableFuture<Void> processDraftOrder(DraftOrderEntity draft) {
        try {
            log.info("Processing draft order with ID: {}", draft.getId());

            // Преобразуем черновик в DTO и отправляем запрос
            var request = draftOrderMapper.toRequest(draft);
            var response = orderClient.doRequest(request);

            // Проверяем ответ
            if (response == null || !response.getStatusCode().is2xxSuccessful() || !response.hasBody()) {
                log.warn("Failed to process draft order with ID: {}", draft.getId());
                return CompletableFuture.completedFuture(null);
            }

            // Обновляем статус черновика
            Long draftId = response.getBody().draftId();
            orderRepository.findById(draftId).ifPresent(updatedDraft -> {
                updatedDraft.setStatus(OrderStatus.SENT);
                orderRepository.save(updatedDraft);
                log.info("Updated status to SENT for draft order with ID: {}", updatedDraft.getId());

                // Отправляем уведомление
                String successMessage = String.format("Ваш заказ с ID %d успешно отправлен!", updatedDraft.getId());
                eventPublisher.publishEvent(new MessageSendEvent(this, updatedDraft.getChatId(), successMessage));
                log.info("Notification sent for draft order with ID: {}", updatedDraft.getId());
            });

        } catch (Exception e) {
            log.error("Error occurred while processing draft order with ID: {}", draft.getId(), e);
        }

        return CompletableFuture.completedFuture(null);
    }
}
