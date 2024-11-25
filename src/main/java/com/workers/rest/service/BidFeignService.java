package com.workers.rest.service;

import com.workers.rest.bid.model.BidChangeStatusRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "BidFeignService",
        url = "${feign.ws-order.bid.url}"
)
public interface BidFeignService {

    @PostMapping("/accept")
    ResponseEntity<Void> acceptBid(@RequestBody BidChangeStatusRequest request);

    @PostMapping("/reject")
    ResponseEntity<Void> rejectBid(@RequestBody BidChangeStatusRequest request);
}
