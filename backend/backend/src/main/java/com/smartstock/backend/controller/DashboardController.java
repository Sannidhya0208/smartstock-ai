package com.smartstock.backend.controller;

import com.smartstock.backend.dto.DashboardResponse;
import com.smartstock.backend.service.DashboardService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.smartstock.backend.dto.LowStockResponse;

import java.util.List;
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboardSummary() {
        return ResponseEntity.ok(
                dashboardService.getDashboardSummary());
    }
    @GetMapping("/low-stock")
    public ResponseEntity<List<LowStockResponse>> getLowStockProducts() {
        return ResponseEntity.ok(
                dashboardService.getLowStockProducts());
    }
}