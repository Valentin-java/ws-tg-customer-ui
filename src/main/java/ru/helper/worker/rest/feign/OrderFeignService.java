package ru.helper.worker.rest.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.helper.worker.rest.create_order.model.OrderCreateRequestDto;
import ru.helper.worker.rest.create_order.model.OrderCreateResponseDto;

@FeignClient(
        name = "OrderFeignService",
        url = "${feign.ws-order.url}"
)
public interface OrderFeignService {

    @PostMapping("/create")
    ResponseEntity<OrderCreateResponseDto> createOrder(@RequestBody OrderCreateRequestDto request);
}
