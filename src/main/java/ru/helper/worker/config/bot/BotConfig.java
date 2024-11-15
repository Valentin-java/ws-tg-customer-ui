package ru.helper.worker.config.bot;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Data
public class BotConfig {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    public List<BotCommand> getCommandList() {
        List<BotCommand> commandList = new ArrayList<>();
        commandList.add(new BotCommand("/start", "Начать работу"));
        commandList.add(new BotCommand("/create_order", "Создать заказ"));
        commandList.add(new BotCommand("/my_active_order", "Мои активные заказы"));
        commandList.add(new BotCommand("/my_archive_order", "Заказы в архиве"));

        return commandList;
    }
}
