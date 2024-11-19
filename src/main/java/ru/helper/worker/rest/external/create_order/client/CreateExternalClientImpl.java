package ru.helper.worker.rest.external.create_order.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.helper.worker.rest.external.common.ExternalClientService;
import ru.helper.worker.rest.external.create_order.model.OrderCreateRequest;
import ru.helper.worker.rest.external.create_order.model.OrderCreateResponseDto;
import ru.helper.worker.rest.external.feign.OrderFeignService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateExternalClientImpl implements ExternalClientService<OrderCreateRequest, ResponseEntity<OrderCreateResponseDto>> {

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
