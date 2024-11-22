package com.workers.rest.external.bid.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.workers.rest.external.bid.interfaces.RejectBidClient;
import com.workers.rest.external.bid.model.BidChangeStatusRequest;
import com.workers.rest.external.feign.BidFeignService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RejectBidClientImpl implements RejectBidClient {

    private final BidFeignService service;

    @Override
    public ResponseEntity<Void> doRequest(BidChangeStatusRequest request) {
        try {
            service.rejectBid(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error occurred while sending accept bid request: {}", request, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
