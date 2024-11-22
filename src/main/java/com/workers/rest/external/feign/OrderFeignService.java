package com.workers.rest.external.feign;

import com.workers.rest.external.create_order.model.OrderCreateRequest;
import com.workers.rest.external.create_order.model.OrderCreateResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "OrderFeignService",
        url = "${feign.ws-order.order.url}"
)
public interface OrderFeignService {

    @PostMapping("/create")
    ResponseEntity<OrderCreateResponseDto> createOrder(@RequestBody OrderCreateRequest request);
}
