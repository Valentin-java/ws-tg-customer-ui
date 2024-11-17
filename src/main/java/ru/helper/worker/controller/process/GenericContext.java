package ru.helper.worker.controller.process;

import java.time.LocalDateTime;

public interface GenericContext {
    boolean isActive();
    void continueProcess(String input);
    LocalDateTime getContextCreated();
    Long getChatId();
}
