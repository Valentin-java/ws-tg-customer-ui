package ru.helper.worker.controller.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.function.Consumer;

@Getter
public class MessageSendEvent extends ApplicationEvent {

    private final Long chatId;
    private final String text;
    private final InlineKeyboardMarkup keys;
    private Consumer<Integer> callback;

    public MessageSendEvent(Object source, Long chatId, String text) {
        super(source);
        this.chatId = chatId;
        this.text = text;
        keys = null;
    }

    public MessageSendEvent(Object source, Long chatId, String text, InlineKeyboardMarkup keys) {
        super(source);
        this.chatId = chatId;
        this.text = text;
        this.keys = keys;
    }

    public void setCallback(Consumer<Integer> callback) {
        this.callback = callback;
    }

    public void complete(Integer messageId) {
        if (callback != null) {
            callback.accept(messageId); // Передача результата через callback
        }
    }
}
