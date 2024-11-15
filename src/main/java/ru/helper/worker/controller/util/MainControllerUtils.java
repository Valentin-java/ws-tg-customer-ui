package ru.helper.worker.controller.util;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.helper.worker.controller.model.UserInput;

public class MainControllerUtils {

    public static String getUsername(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChat().getUserName();
        }
        return null;
    }

    public static UserInput extractUserInput(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String input = update.getMessage().getText();
            return new UserInput(chatId, input);
        } else if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String input = update.getCallbackQuery().getData();
            return new UserInput(chatId, input);
        }
        return null;
    }
}
