package ru.helper.worker.controller.listener;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.helper.worker.controller.events.MessageSendEvent;
import ru.helper.worker.controller.message.MessageService;

@Service
@RequiredArgsConstructor
public class MessageSendListener {

    private final MessageService messageService;

    @Async
    @EventListener
    @SneakyThrows
    public void handleMessageSend(MessageSendEvent event) {
        messageService.sendMessage(event.getChatId(), event.getText(), event.getKeys());
    }
}
