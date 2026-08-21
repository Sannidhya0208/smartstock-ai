package com.smartstock.backend.controller;

import com.smartstock.backend.dto.DemandForecastResponse;
import com.smartstock.backend.service.DemandForecastService;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forecast")
public class DemandForecastController {

    private final DemandForecastService forecastService;

    public DemandForecastController(
            DemandForecastService forecastService
    ) {
        this.forecastService =
                forecastService;
    }

    @PreAuthorize(
            "hasAnyRole('OWNER','MANAGER')"
    )
    @GetMapping("/inventory/{inventoryId}")
    public ResponseEntity<DemandForecastResponse>
            forecastDemand(
                    @PathVariable
                    Long inventoryId,

                    @RequestParam(
                            required = false,
                            defaultValue = "7"
                    )
                    Integer days,

                    Authentication authentication
            ) {

        return ResponseEntity.ok(
                forecastService.forecastDemand(
                        inventoryId,
                        days,
                        authentication.getName()
                )
        );
    }
}