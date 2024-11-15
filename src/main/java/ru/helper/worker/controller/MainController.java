package ru.helper.worker.controller;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.helper.worker.business.interfaces.OrderService;
import ru.helper.worker.config.bot.BotConfig;
import ru.helper.worker.controller.context.OrderContext;
import ru.helper.worker.controller.model.UserInput;

import java.util.List;

import static ru.helper.worker.controller.model.CommonConstants.DEFAULT_MESSAGE_ERROR;
import static ru.helper.worker.controller.model.CommonConstants.WELCOME_MESSAGE;
import static ru.helper.worker.controller.util.MainControllerUtils.extractUserInput;
import static ru.helper.worker.controller.util.MainControllerUtils.getUsername;

@Slf4j
@Service
public class MainController extends TelegramLongPollingBot {

    private final BotConfig config;
    private final OrderService orderService;

    @PostConstruct
    @SneakyThrows
    private void initCommandList() {
        List<BotCommand> commandList = config.getCommandList();
        this.execute(new SetMyCommands(commandList, new BotCommandScopeDefault(), null));
    }

    public MainController(BotConfig config, OrderService orderService) {
        super(config.getBotToken());
        this.config = config;
        this.orderService = orderService;
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

            OrderContext context = orderService.getContext(chatId);
            if (context != null && context.getCurrentState() != null) {
                context.continueProcessInput(input);

            } else {
                String username = getUsername(update);
                handleCommand(chatId, input, username);
            }
        }
    }

    private void handleCommand(Long chatId, String input, String username) throws TelegramApiException {
        switch (input) {
            case "/start" -> sendMessage(chatId, WELCOME_MESSAGE);
            case "/create_order" -> orderService.initProcess(chatId, username);
            default -> sendMessage(chatId, DEFAULT_MESSAGE_ERROR);
        }
    }

    public void sendMessage(Long chatId, String text) throws TelegramApiException {
        execute(SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build());
    }
}
