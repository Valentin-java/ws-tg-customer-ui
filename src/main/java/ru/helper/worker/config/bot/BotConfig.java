package ru.helper.worker.config.bot;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

@Data
@Configuration
public class BotConfig {

    private final BotProperties botProperties;

    public String getBotName() {
        return botProperties.getName();
    }

    public String getBotToken() {
        return botProperties.getToken();
    }

    public List<BotCommand> getCommandList() {
        return botProperties.getCommandList().entrySet().stream()
                .map(entry -> new BotCommand(entry.getKey(), entry.getValue()))
                .toList();
    }
}
