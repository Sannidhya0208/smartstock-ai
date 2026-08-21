package com.smartstock.backend.controller;

import com.smartstock.backend.dto.AnalyticsResponse;
import com.smartstock.backend.service.AnalyticsService;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(
            AnalyticsService analyticsService
    ) {
        this.analyticsService =
                analyticsService;
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER')"
    )
    @GetMapping
    public ResponseEntity<AnalyticsResponse>
            getAnalytics(
                    Authentication authentication
            ) {

        return ResponseEntity.ok(
                analyticsService.getAnalytics(
                        authentication.getName()
                )
        );
    }
}