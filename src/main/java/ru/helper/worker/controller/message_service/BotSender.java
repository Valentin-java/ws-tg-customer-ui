package ru.helper.worker.controller.message_service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface BotSender {
    Message sendMessage(SendMessage message) throws TelegramApiException;

    void editMessage(EditMessageText editMessage) throws TelegramApiException;
}
