package ru.helper.worker.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.helper.worker.business.received_bid.interfaces.BidService;
import ru.helper.worker.business.received_bid.model.BidReceiveRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/bid")
public class BidController {

    private final BidService service;

    @PostMapping("/receive")
    public ResponseEntity<Void> receiveBid(@RequestBody BidReceiveRequest request) {
        service.initProcess(request);
        return ResponseEntity.ok().build();
    }
}
