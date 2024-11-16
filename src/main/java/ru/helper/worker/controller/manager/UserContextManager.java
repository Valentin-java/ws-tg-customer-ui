package ru.helper.worker.controller.manager;

import org.springframework.stereotype.Service;
import ru.helper.worker.controller.process.UserContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserContextManager {

    private final Map<Long, UserContext> userContexts = new ConcurrentHashMap<>();

    public void addContext(Long chatId, UserContext context) {
        userContexts.put(chatId, context);
    }

    public void removeContext(Long chatId) {
        userContexts.remove(chatId);
    }

    public UserContext getActiveContext(Long chatId) {
        return userContexts.get(chatId);
    }

    public boolean hasActiveContext(Long chatId) {
        UserContext context = userContexts.get(chatId);
        return context != null && context.isActive();
    }
}
