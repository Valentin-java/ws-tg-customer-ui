package ru.helper.worker.controller.listener;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.helper.worker.controller.events.command_event.CommandReceivedEvent;
import ru.helper.worker.controller.message.MessageService;

import static ru.helper.worker.controller.model.CommonConstants.WELCOME_MESSAGE;

@Service
@RequiredArgsConstructor
public class WelcomeCommandListener {

    private final MessageService messageService;

    @EventListener
    @SneakyThrows
    public void handleWelcomeCommand(CommandReceivedEvent event) {
        if ("/start".equals(event.getCommand())) {
            messageService.sendMessage(event.getChatId(), WELCOME_MESSAGE);
        }
    }
}
