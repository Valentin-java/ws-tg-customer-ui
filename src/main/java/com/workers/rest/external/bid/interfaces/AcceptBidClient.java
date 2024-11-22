package com.workers.rest.external.bid.interfaces;

import org.springframework.http.ResponseEntity;
import com.workers.rest.external.bid.model.BidChangeStatusRequest;

public interface AcceptBidClient {

    ResponseEntity<Void> doRequest(BidChangeStatusRequest request);
}
