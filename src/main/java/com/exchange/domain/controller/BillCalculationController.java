package com.exchange.domain.controller;

import com.exchange.domain.model.dto.BillRequest;
import com.exchange.domain.model.dto.BillResponse;
import com.exchange.domain.service.BillCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class BillCalculationController {

    private final BillCalculationService calculationService;

    public BillCalculationController(BillCalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @PostMapping("/calculate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BillResponse> calculateBill(@RequestBody BillRequest request) {
        BillResponse response = calculationService.calculateBill(request);
        return ResponseEntity.ok(response);
    }
}
