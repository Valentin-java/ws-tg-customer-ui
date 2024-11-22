package com.workers.rest.external.bid.client;

import com.workers.rest.external.bid.interfaces.AcceptBidClient;
import com.workers.rest.external.bid.model.BidChangeStatusRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.workers.rest.external.feign.BidFeignService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcceptBidClientImpl implements AcceptBidClient {

    private final BidFeignService service;

    @Override
    public ResponseEntity<Void> doRequest(BidChangeStatusRequest request) {
        try {
            service.acceptBid(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error occurred while sending accept bid request: {}", request, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
