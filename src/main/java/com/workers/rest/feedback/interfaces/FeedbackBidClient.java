package com.workers.rest.feedback.interfaces;

import org.springframework.http.ResponseEntity;
import com.workers.rest.bid.model.FeedbackHandymanRequest;

public interface FeedbackBidClient {

    ResponseEntity<Void> doRequest(FeedbackHandymanRequest request);
}
