package ru.helper.worker.controller.listener;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.helper.worker.config.bot.BotProperties;
import ru.helper.worker.controller.events.CommandReceivedEvent;
import ru.helper.worker.controller.message.MessageService;

import static ru.helper.worker.controller.model.CommonConstants.DEFAULT_MESSAGE_ERROR;

@Service
@RequiredArgsConstructor
public class DefaultCommandListener {

    private final MessageService messageService;
    private final BotProperties botProperties;

    @Async
    @EventListener
    @SneakyThrows
    public void handleDefaultCommand(CommandReceivedEvent event) {
        var commands = botProperties.getCommandList().keySet().stream().toList();

        if (!commands.contains(event.getCommand().replaceAll("/", ""))) {
            messageService.sendMessage(event.getChatId(), DEFAULT_MESSAGE_ERROR);
        }
    }
}
