package ru.helper.worker.rest.external.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.helper.worker.rest.external.create_order.model.OrderCreateRequest;
import ru.helper.worker.rest.external.create_order.model.OrderCreateResponseDto;

@FeignClient(
        name = "OrderFeignService",
        url = "${feign.ws-order.order.url}"
)
public interface OrderFeignService {

    @PostMapping("/create")
    ResponseEntity<OrderCreateResponseDto> createOrder(@RequestBody OrderCreateRequest request);
}
