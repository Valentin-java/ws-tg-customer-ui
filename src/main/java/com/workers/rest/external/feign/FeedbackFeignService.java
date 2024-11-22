package com.workers.rest.external.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.workers.rest.external.bid.model.FeedbackHandymanRequest;

@FeignClient(
        name = "FeedbackFeignService",
        url = "${feign.ws-order.feedback.url}"
)
public interface FeedbackFeignService {

    @PostMapping("/feedback")
    ResponseEntity<Void> feedbackBid(@RequestBody FeedbackHandymanRequest request);
}
