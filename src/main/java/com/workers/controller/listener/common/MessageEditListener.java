package com.workers.controller.listener.common;

import com.workers.controller.message_service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.workers.controller.events.MessageEditEvent;

@Service
@RequiredArgsConstructor
public class MessageEditListener {

    private final MessageService messageService;

    @Async
    @EventListener
    @SneakyThrows
    public void handleMessageEdit(MessageEditEvent event) {
        messageService.editMessage(event.getChatId(), event.getMessageId(), event.getNewText());
    }
}
