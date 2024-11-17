package ru.helper.worker.controller;

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
import ru.helper.worker.config.bot.BotConfig;
import ru.helper.worker.controller.events.CommandReceivedEvent;
import ru.helper.worker.controller.manager.UserContextManager;
import ru.helper.worker.controller.model.UserInput;
import ru.helper.worker.controller.process.GenericContext;

import java.util.List;

import static ru.helper.worker.controller.util.MainControllerUtils.extractUserInput;
import static ru.helper.worker.controller.util.MainControllerUtils.getUsername;

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


            GenericContext activeContext = contextManager.getActiveContext(chatId);
            if (activeContext != null && activeContext.isActive()) {
                // Передаем управление менеджеру процессов
                activeContext.continueProcess(input);
            } else {
                eventPublisher.publishEvent(new CommandReceivedEvent(this, chatId, input, getUsername(update)));
            }
        }
    }
}
