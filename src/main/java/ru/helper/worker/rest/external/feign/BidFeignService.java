package ru.helper.worker.rest.external.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.helper.worker.rest.external.bid.model.BidChangeStatusRequest;
import ru.helper.worker.rest.external.bid.model.FeedbackHandymanRequest;

@FeignClient(
        name = "BidFeignService",
        url = "${feign.ws-order.bid.url}"
)
public interface BidFeignService {

    @PostMapping("/accept")
    ResponseEntity<Void> acceptBid(@RequestBody BidChangeStatusRequest request);

    @PostMapping("/reject")
    ResponseEntity<Void> rejectBid(@RequestBody BidChangeStatusRequest request);

    @PostMapping("/feedback")
    ResponseEntity<Void> feedbackBid(@RequestBody FeedbackHandymanRequest request);
}
