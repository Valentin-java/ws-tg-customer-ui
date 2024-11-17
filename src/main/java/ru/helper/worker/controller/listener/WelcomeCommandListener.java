package ru.helper.worker.controller.listener;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.helper.worker.controller.events.CommandReceivedEvent;
import ru.helper.worker.controller.message.MessageService;

import static ru.helper.worker.controller.model.CommonConstants.WELCOME_MESSAGE;

@Service
@RequiredArgsConstructor
public class WelcomeCommandListener {

    private static final String WELCOME_COMMAND = "/start";
    private final MessageService messageService;

    @EventListener
    @SneakyThrows
    public void handleWelcomeCommand(CommandReceivedEvent event) {
        if (WELCOME_COMMAND.equals(event.getCommand())) {
            messageService.sendMessage(event.getChatId(), WELCOME_MESSAGE);
        }
    }
}
