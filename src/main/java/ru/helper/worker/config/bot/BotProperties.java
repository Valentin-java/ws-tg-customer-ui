package ru.helper.worker.config.bot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "bot")
public class BotProperties {

    private String name;
    private String token;
    private Map<String, String> commandList;
}
