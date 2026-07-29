package com.smartstock.backend.controller;

import com.smartstock.backend.dto.AiInsightResponse;
import com.smartstock.backend.service.AiInsightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.smartstock.backend.dto.AiChatRequest;
import com.smartstock.backend.dto.AiChatResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

import com.smartstock.backend.dto.AiDashboardResponse;

@RestController
@RequestMapping("/api/ai")
public class AiInsightController {

    private final AiInsightService aiInsightService;

    public AiInsightController(
            AiInsightService aiInsightService) {

        this.aiInsightService = aiInsightService;
    }

    @GetMapping("/insights")
    public ResponseEntity<AiInsightResponse> generateInventoryInsights() {

        return ResponseEntity.ok(
                aiInsightService.generateInventoryInsights());
    }

    @PostMapping("/chat")
    public ResponseEntity<AiChatResponse> askInventoryQuestion(
            @Valid @RequestBody AiChatRequest request) {

        AiChatResponse response = aiInsightService.askInventoryQuestion(
                request.getQuestion());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard-summary")
    public ResponseEntity<AiDashboardResponse> getDashboardSummary() {

        AiDashboardResponse response = aiInsightService.generateDashboardSummary();

        return ResponseEntity.ok(response);
    }
}