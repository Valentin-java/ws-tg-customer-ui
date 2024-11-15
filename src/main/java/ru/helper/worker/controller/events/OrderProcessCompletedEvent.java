package ru.helper.worker.controller.events;

import org.springframework.context.ApplicationEvent;

public class OrderProcessCompletedEvent extends ApplicationEvent {

    private final Long chatId;

    public OrderProcessCompletedEvent(Object source, Long chatId) {
        super(source);
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }

}
