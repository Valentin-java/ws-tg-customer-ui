package com.workers.business.common.manager;

import com.workers.business.common.context.GenericContext;
import com.workers.business.create_order.process.context.OrderContext;
import com.workers.business.received_bid.process.context.BidContext;
import com.workers.business.received_bid.process.state.impl.BidReceiveState;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserContextManager {

    private final RedisTemplate<String, GenericContext> redisTemplate;

    public void addContext(Long chatId, GenericContext context) {
        ValueOperations<String, GenericContext> ops = redisTemplate.opsForValue();
        ops.set(chatId.toString(), context, getTTLForContext(context));
    }

    public void removeContext(Long chatId) {
        redisTemplate.delete(chatId.toString());
    }

    public GenericContext getActiveContext(Long chatId) {
        ValueOperations<String, GenericContext> ops = redisTemplate.opsForValue();
        return ops.get(chatId.toString());
    }

    public boolean hasActiveContext(Long chatId) {
        return getActiveContext(chatId) != null;
    }

    private Duration getTTLForContext(GenericContext context) {
        if (context instanceof OrderContext) {
            return Duration.ofHours(1);
        } else if (context instanceof BidContext
                && ((BidContext) context).getCurrentState() instanceof BidReceiveState) {
            return Duration.ofHours(48);
        } else {
            return Duration.ofHours(1);
        }
    }
}
