package ru.helper.worker.controller.manager;

import org.springframework.stereotype.Service;
import ru.helper.worker.controller.process.GenericContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserContextManager {

    private final Map<Long, GenericContext> userContexts = new ConcurrentHashMap<>();

    public void addContext(Long chatId, GenericContext context) {
        userContexts.put(chatId, context);
    }

    public void removeContext(Long chatId) {
        userContexts.remove(chatId);
    }

    public GenericContext getActiveContext(Long chatId) {
        return userContexts.get(chatId);
    }

    public boolean hasActiveContext(Long chatId) {
        GenericContext context = userContexts.get(chatId);
        return context != null && context.isActive();
    }
}
