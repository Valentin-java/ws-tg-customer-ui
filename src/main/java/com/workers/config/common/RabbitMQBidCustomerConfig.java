package com.workers.config.common;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQBidCustomerConfig {

    public static final String QUEUE_NAME = "bid.receive.queue";
    public static final String EXCHANGE_NAME = "bid.receive.exchange";
    public static final String ROUTING_KEY = "bid.receive.routingkey";

    @Bean
    public Queue bidReceiveQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange bidReceiveExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding bidReceiveBinding(Queue bidReceiveQueue, DirectExchange bidReceiveExchange) {
        return BindingBuilder.bind(bidReceiveQueue)
                .to(bidReceiveExchange)
                .with(ROUTING_KEY);
    }
}
