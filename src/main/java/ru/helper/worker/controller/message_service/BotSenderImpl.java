package ru.helper.worker.controller.message_service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.helper.worker.config.bot.BotConfig;

@Component
public class BotSenderImpl extends DefaultAbsSender implements BotSender {

    private final String botToken;

    public BotSenderImpl(BotConfig config) {
        super(new DefaultBotOptions());
        this.botToken = config.getBotToken();
    }

    @Override
    public Message sendMessage(SendMessage message) throws TelegramApiException {
        return execute(message);
    }

    @Override
    public void editMessage(EditMessageText editMessage) throws TelegramApiException {
        execute(editMessage);
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}