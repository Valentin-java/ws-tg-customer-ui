package ru.helper.worker.controller.listener;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.helper.worker.controller.events.MessageEditEvent;
import ru.helper.worker.controller.message_service.MessageService;

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
