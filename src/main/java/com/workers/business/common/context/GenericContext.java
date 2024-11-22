package com.workers.business.common.context;

import java.time.LocalDateTime;

public interface GenericContext {
    boolean isActive();
    void continueProcess(String input);
    LocalDateTime getContextCreated();
    Long getChatId();
}
