package com.smartstock.backend.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartstock.backend.dto.AiChatRequest;
import com.smartstock.backend.dto.AiChatResponse;
import com.smartstock.backend.dto.AiDashboardResponse;
import com.smartstock.backend.dto.AiInsightResponse;

import com.smartstock.backend.service.AiInsightService;

@RestController
@RequestMapping("/api/ai")
public class AiInsightController {

    private final AiInsightService aiInsightService;

    public AiInsightController(
            AiInsightService aiInsightService
    ) {
        this.aiInsightService =
                aiInsightService;
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER')"
    )
    @GetMapping("/insights")
    public ResponseEntity<AiInsightResponse>
            generateInventoryInsights(
                    Authentication authentication
            ) {

        return ResponseEntity.ok(
                aiInsightService
                        .generateInventoryInsights(
                                authentication.getName()
                        )
        );
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER')"
    )
    @PostMapping("/chat")
    public ResponseEntity<AiChatResponse>
            askInventoryQuestion(
                    @Valid
                    @RequestBody
                    AiChatRequest request,

                    Authentication authentication
            ) {

        AiChatResponse response =
                aiInsightService
                        .askInventoryQuestion(
                                request.getQuestion(),
                                authentication.getName()
                        );

        return ResponseEntity.ok(
                response
        );
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER')"
    )
    @GetMapping("/dashboard-summary")
    public ResponseEntity<AiDashboardResponse>
            getDashboardSummary(
                    Authentication authentication
            ) {

        AiDashboardResponse response =
                aiInsightService
                        .generateDashboardSummary(
                                authentication.getName()
                        );

        return ResponseEntity.ok(
                response
        );
    }
}