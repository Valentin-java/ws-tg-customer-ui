package com.workers.controller;

import com.workers.business.common.manager.UserContextManager;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import com.workers.config.bot.BotConfig;
import com.workers.controller.events.CommandReceivedEvent;
import com.workers.controller.model.UserInput;

import java.util.List;

import static com.workers.controller.util.MainControllerUtils.extractUserInput;
import static com.workers.controller.util.MainControllerUtils.getUsername;

@Slf4j
@Service
public class MainController extends TelegramLongPollingBot {

    private final BotConfig config;
    private final UserContextManager contextManager;
    private final ApplicationEventPublisher eventPublisher;

    @PostConstruct
    @SneakyThrows
    private void initCommandList() {
        List<BotCommand> commandList = config.getCommandList();
        this.execute(new SetMyCommands(commandList, new BotCommandScopeDefault(), null));
    }

    public MainController(BotConfig config,
                          UserContextManager contextManager,
                          ApplicationEventPublisher eventPublisher) {
        super(config.getBotToken());
        this.config = config;
        this.contextManager = contextManager;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    /**
     * Здесь бот принимает сообщения.
     * @param update Update received
     */
    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        UserInput userInput = extractUserInput(update);

        if (userInput != null) {
            Long chatId = userInput.getChatId();
            String input = userInput.getInput();

            if (contextManager.hasActiveContext(chatId)) {
                contextManager.getActiveContext(chatId).continueProcess(input);
            } else {
                eventPublisher.publishEvent(new CommandReceivedEvent(this, chatId, input, getUsername(update)));
            }
        }
    }
}
