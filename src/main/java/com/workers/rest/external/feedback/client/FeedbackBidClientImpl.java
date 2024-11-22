package com.workers.rest.external.feedback.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.workers.rest.external.bid.model.FeedbackHandymanRequest;
import com.workers.rest.external.feedback.interfaces.FeedbackBidClient;
import com.workers.rest.external.feign.FeedbackFeignService;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackBidClientImpl implements FeedbackBidClient {

    private final FeedbackFeignService service;

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
