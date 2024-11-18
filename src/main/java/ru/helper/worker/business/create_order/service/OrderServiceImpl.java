package ru.helper.worker.business.create_order.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.helper.worker.business.create_order.interfaces.OrderService;
import ru.helper.worker.business.create_order.process.context.OrderContext;
import ru.helper.worker.business.create_order.process.states.OrderState;
import ru.helper.worker.business.common.manager.UserContextManager;
import ru.helper.worker.controller.events.MessageSendEvent;
import ru.helper.worker.persistence.mapper.DraftOrderMapper;
import ru.helper.worker.persistence.repository.DraftOrderRepository;
import ru.helper.worker.rest.common.ExternalClientService;
import ru.helper.worker.rest.create_order.model.OrderCreateRequestDto;
import ru.helper.worker.rest.create_order.model.OrderCreateResponseDto;

import java.util.Optional;

import static ru.helper.worker.persistence.enums.OrderStatus.DRAFT;
import static ru.helper.worker.persistence.enums.OrderStatus.SENT;
import static ru.helper.worker.persistence.enums.SendProcess.AUTO;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final ExternalClientService<OrderCreateRequestDto, ResponseEntity<OrderCreateResponseDto>> orderClient;
    private final ApplicationEventPublisher eventPublisher;
    private final DraftOrderRepository orderRepository;
    private final UserContextManager contextManager;
    private final DraftOrderMapper draftOrderMapper;
    private final OrderState initOrderState;

    public OrderServiceImpl(
            ExternalClientService<OrderCreateRequestDto,
            ResponseEntity<OrderCreateResponseDto>> orderClient,
            ApplicationEventPublisher eventPublisher, @Qualifier("categorySelectionState") OrderState initOrderState,
            UserContextManager contextManager,
            DraftOrderRepository orderRepository,
            DraftOrderMapper draftOrderMapper) {
        this.orderClient = orderClient;
        this.eventPublisher = eventPublisher;
        this.initOrderState = initOrderState;
        this.contextManager = contextManager;
        this.orderRepository = orderRepository;
        this.draftOrderMapper = draftOrderMapper;
    }

    @Override
    @SneakyThrows
    public void initProcess(Long chatId, String username) {
        OrderContext context = new OrderContext(chatId);
        context.setCurrentState(initOrderState);
        context.getOrderRequest().setCustomerId(username);
        context.getOrderRequest().setChatId(chatId);
        contextManager.addContext(chatId, context);
        context.getCurrentState().enter(context);
    }

    /**
     * Шедулер для повторных попыток отправки заказов, если по какой-то причине не получилось это сделать штатно.
     */
    @Scheduled(fixedRate = 3600000)
    public void retrySendDraftOrder() {

        orderRepository.findByStatusAndSendProcess(DRAFT, AUTO).stream()
                .peek(draft -> log.info("Processing draft order with ID: {}", draft.getId()))
                .map(draftOrderMapper::toRequest)
                .map(orderClient::doRequest)
                .filter(response -> response != null && response.getStatusCode().is2xxSuccessful() && response.hasBody())
                .map(response -> {
                    Long draftId = response.getBody().draftId();
                    return orderRepository.findById(draftId);
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .peek(draft -> {
                    draft.setStatus(SENT);
                    log.info("Updating status to SENT for draft order with ID: {}", draft.getId());
                })
                .peek(draft -> {
                    // Отправка сообщения пользователю об успешной отправке заказа
                    String successMessage = String.format("Ваш заказ с ID %d успешно отправлен!", draft.getId());
                    eventPublisher.publishEvent(new MessageSendEvent(this, draft.getChatId(), successMessage));
                    log.info("Notification sent for draft order with ID: {}", draft.getId());
                })
                .forEach(orderRepository::save);
    }
}
