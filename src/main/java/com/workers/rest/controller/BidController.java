package com.workers.rest.controller;

import com.workers.business.received_bid.interfaces.BidService;
import com.workers.business.received_bid.model.BidReceiveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
