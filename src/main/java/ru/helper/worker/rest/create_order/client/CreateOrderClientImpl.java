package ru.helper.worker.rest.create_order.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.helper.worker.rest.common.OrderClientService;
import ru.helper.worker.rest.create_order.model.OrderCreateRequestDto;
import ru.helper.worker.rest.create_order.model.OrderCreateResponseDto;
import ru.helper.worker.rest.feign.OrderFeignService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOrderClientImpl implements OrderClientService<OrderCreateRequestDto, OrderCreateResponseDto> {

    private final OrderFeignService service;

    @Override
    public OrderCreateResponseDto doRequest(OrderCreateRequestDto request) {
        try {
            return service.createOrder(request).getBody();
        } catch (Exception e) {
            log.error("Что-то пошло не так создании заказа: {}", request.shortDescription());
            return null;
        }
    }
}
