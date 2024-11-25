package com.workers.rest.create_order.interfaces;

import com.workers.rest.create_order.model.OrderCreateRequest;
import com.workers.rest.create_order.model.OrderCreateResponseDto;
import org.springframework.http.ResponseEntity;

public interface CreateOrderClient {

    ResponseEntity<OrderCreateResponseDto> doRequest(OrderCreateRequest request);
}
