package com.workers.controller.listener;

import com.workers.controller.message_service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.workers.config.bot.BotProperties;
import com.workers.controller.events.CommandReceivedEvent;

import static com.workers.business.create_order.model.CommonConstants.DEFAULT_MESSAGE_ERROR;

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
