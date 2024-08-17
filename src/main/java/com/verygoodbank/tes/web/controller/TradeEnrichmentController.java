package com.verygoodbank.tes.web.controller;


import com.verygoodbank.tes.web.model.TradeRequest;
import com.verygoodbank.tes.web.model.TradeResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class TradeEnrichmentController {

    @PostMapping(
            value = "/enrich",
            produces = "application/json",
            consumes = "application/json"
    )
    public TradeResponse enrichTrades(@RequestBody List<TradeRequest> trades) {
        return null;
    }

}


