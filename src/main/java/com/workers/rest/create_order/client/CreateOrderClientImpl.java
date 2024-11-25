package com.workers.rest.create_order.client;

import com.workers.rest.create_order.interfaces.CreateOrderClient;
import com.workers.rest.create_order.model.OrderCreateRequest;
import com.workers.rest.create_order.model.OrderCreateResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.workers.rest.service.OrderFeignService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOrderClientImpl implements CreateOrderClient {

    private final OrderFeignService service;

    @Override
    public ResponseEntity<OrderCreateResponseDto> doRequest(OrderCreateRequest request) {
        try {
            return service.createOrder(request);
        } catch (Exception e) {
            log.error("Error occurred while sending order creation request: {}", request, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
