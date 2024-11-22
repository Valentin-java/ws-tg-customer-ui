package ru.helper.worker.rest.external.feedback.interfaces;

import org.springframework.http.ResponseEntity;
import ru.helper.worker.rest.external.bid.model.FeedbackHandymanRequest;

public interface FeedbackBidClient {

    ResponseEntity<Void> doRequest(FeedbackHandymanRequest request);
}
