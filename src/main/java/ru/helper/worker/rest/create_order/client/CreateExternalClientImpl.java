package ru.helper.worker.rest.create_order.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.helper.worker.rest.common.ExternalClientService;
import ru.helper.worker.rest.create_order.model.OrderCreateRequestDto;
import ru.helper.worker.rest.create_order.model.OrderCreateResponseDto;
import ru.helper.worker.rest.feign.OrderFeignService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateExternalClientImpl implements ExternalClientService<OrderCreateRequestDto, ResponseEntity<OrderCreateResponseDto>> {

    private final OrderFeignService service;

    @Override
    public ResponseEntity<OrderCreateResponseDto> doRequest(OrderCreateRequestDto request) {
        try {
            return service.createOrder(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }
}
