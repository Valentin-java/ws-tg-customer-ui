package ru.helper.worker.rest.external.create_order.interfaces;

import org.springframework.http.ResponseEntity;
import ru.helper.worker.rest.external.create_order.model.OrderCreateRequest;
import ru.helper.worker.rest.external.create_order.model.OrderCreateResponseDto;

public interface CreateOrderClient {

    ResponseEntity<OrderCreateResponseDto> doRequest(OrderCreateRequest request);
}
