package ru.helper.worker.controller.events.command_event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CommandReceivedEvent extends ApplicationEvent {

    private final Long chatId;
    private final String command;
    private final String username;

    public CommandReceivedEvent(Object source, Long chatId, String command, String username) {
        super(source);
        this.chatId = chatId;
        this.command = command;
        this.username = username;
    }
}