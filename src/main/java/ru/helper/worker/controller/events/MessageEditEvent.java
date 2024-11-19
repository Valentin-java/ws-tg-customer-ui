package ru.helper.worker.controller.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MessageEditEvent extends ApplicationEvent {

    private final Long chatId;
    private final Integer messageId;
    private final String newText;

    public MessageEditEvent(Object source, Long chatId, Integer messageId, String newText) {
        super(source);
        this.chatId = chatId;
        this.messageId = messageId;
        this.newText = newText;
    }
}
