package ru.helper.worker.controller.process;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface GenericContext {
    boolean isActive();
    void continueProcess(String input) throws TelegramApiException;
}
