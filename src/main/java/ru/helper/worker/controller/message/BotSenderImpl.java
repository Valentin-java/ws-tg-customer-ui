package ru.helper.worker.controller.message;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
    public void sendMessage(SendMessage message) throws TelegramApiException {
        execute(message);
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}