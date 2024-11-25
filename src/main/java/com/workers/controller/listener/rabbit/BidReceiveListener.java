package com.workers.controller.listener.rabbit;

import com.workers.business.received_bid.interfaces.BidService;
import com.workers.business.received_bid.model.BidReceiveRequest;
import com.workers.config.common.RabbitMQBidCustomerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BidReceiveListener {

    private final BidService service;

    @RabbitListener(queues = RabbitMQBidCustomerConfig.QUEUE_NAME)
    public void receiveBid(BidReceiveRequest request) {
        service.initProcess(request);
        log.debug("[BidReceiveListener] [receiveBid] Получено предложение: {}", request);}
}
