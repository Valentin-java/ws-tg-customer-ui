package com.workers.controller.listener;

import com.workers.controller.message_service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.workers.controller.events.MessageSendEvent;

@Service
@RequiredArgsConstructor
public class MessageSendListener {

    private final MessageService messageService;

    @Async
    @EventListener
    @SneakyThrows
    public void handleMessageSend(MessageSendEvent event) {
        Integer messageId = messageService.sendMessage(event.getChatId(), event.getText(), event.getKeys());
        event.complete(messageId);
    }
}
