package com.workers.rest.external.feedback.interfaces;

import org.springframework.http.ResponseEntity;
import com.workers.rest.external.bid.model.FeedbackHandymanRequest;

public interface FeedbackBidClient {

    ResponseEntity<Void> doRequest(FeedbackHandymanRequest request);
}
