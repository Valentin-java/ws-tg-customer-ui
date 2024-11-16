package ru.helper.worker.controller.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.helper.worker.business.create_order.interfaces.OrderService;
import ru.helper.worker.controller.events.command_event.CommandReceivedEvent;

@Service
@RequiredArgsConstructor
public class CreateOrderCommandListener {

    private final OrderService service;

    @EventListener
    public void handleCreateOrderCommand(CommandReceivedEvent event) {
        if ("/create_order".equals(event.getCommand())) {
            service.initProcess(event.getChatId(), event.getUsername());
        }
    }
}
