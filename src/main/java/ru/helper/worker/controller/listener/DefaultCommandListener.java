package ru.helper.worker.controller.listener;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.helper.worker.controller.events.command_event.CommandReceivedEvent;
import ru.helper.worker.controller.message.MessageService;

import static ru.helper.worker.controller.model.CommonConstants.DEFAULT_MESSAGE_ERROR;

@Service
@RequiredArgsConstructor
public class DefaultCommandListener {

    private final MessageService messageService;

    @EventListener
    @SneakyThrows
    public void handleDefaultCommand(CommandReceivedEvent event) {
        if ("/start".equals(event.getCommand())) {
            messageService.sendMessage(event.getChatId(), DEFAULT_MESSAGE_ERROR);
        }
    }
}
