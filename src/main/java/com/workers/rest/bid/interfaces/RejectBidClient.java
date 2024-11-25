package com.workers.rest.bid.interfaces;

import com.workers.rest.bid.model.BidChangeStatusRequest;
import org.springframework.http.ResponseEntity;

public interface RejectBidClient {

    ResponseEntity<Void> doRequest(BidChangeStatusRequest request);
}
