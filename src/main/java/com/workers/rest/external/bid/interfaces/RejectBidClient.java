package com.workers.rest.external.bid.interfaces;

import com.workers.rest.external.bid.model.BidChangeStatusRequest;
import org.springframework.http.ResponseEntity;

public interface RejectBidClient {

    ResponseEntity<Void> doRequest(BidChangeStatusRequest request);
}
