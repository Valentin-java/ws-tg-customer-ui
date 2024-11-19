package ru.helper.worker.rest.external.bid.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.helper.worker.rest.external.bid.model.FeedbackHandymanRequest;
import ru.helper.worker.rest.external.common.ExternalClientService;
import ru.helper.worker.rest.external.feign.BidFeignService;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackBidClient implements ExternalClientService<FeedbackHandymanRequest, ResponseEntity<Void>> {

    private final BidFeignService service;

    @Override
    public ResponseEntity<Void> doRequest(FeedbackHandymanRequest request) {
        try {
            service.feedbackBid(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error occurred while sending feedback bid request: {}", request, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
