package com.workers.controller.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderProcessCompletedEvent extends ApplicationEvent {

    private final Long chatId;

    public OrderProcessCompletedEvent(Object source, Long chatId) {
        super(source);
        this.chatId = chatId;
    }
}
