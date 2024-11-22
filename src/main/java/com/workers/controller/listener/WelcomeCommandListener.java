package com.workers.controller.listener;

import com.workers.controller.message_service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.workers.controller.events.CommandReceivedEvent;

import static com.workers.business.create_order.model.CommonConstants.WELCOME_MESSAGE;

@Service
@RequiredArgsConstructor
public class WelcomeCommandListener {

    private static final String WELCOME_COMMAND = "/start";
    private final MessageService messageService;

    @Async
    @EventListener
    @SneakyThrows
    public void handleWelcomeCommand(CommandReceivedEvent event) {
        if (WELCOME_COMMAND.equals(event.getCommand())) {
            messageService.sendMessage(event.getChatId(), WELCOME_MESSAGE);
        }
    }
}
