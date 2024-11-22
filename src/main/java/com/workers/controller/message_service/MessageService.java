package com.workers.controller.message_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final BotSender botSender;

    public Integer sendMessage(Long chatId, String text) throws TelegramApiException {
        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .build();
        Message sentMessage = botSender.sendMessage(message);
        return sentMessage.getMessageId();
    }

    public Integer sendMessage(Long chatId, String text, InlineKeyboardMarkup keyboard) throws TelegramApiException {
        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .replyMarkup(keyboard)
                .build();
        Message sentMessage = botSender.sendMessage(message);
        return sentMessage.getMessageId();
    }

    public void editMessage(Long chatId, Integer messageId, String newText) throws TelegramApiException {
        EditMessageText editMessage = EditMessageText.builder()
                .chatId(chatId.toString())
                .messageId(messageId)
                .text(newText)
                .build();
        botSender.editMessage(editMessage);
    }
}
