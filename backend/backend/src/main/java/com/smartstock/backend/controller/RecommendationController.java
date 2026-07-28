package com.smartstock.backend.controller;

import com.smartstock.backend.dto.ReorderRecommendationResponse;
import com.smartstock.backend.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(
            RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/reorder")
    public ResponseEntity<
            List<ReorderRecommendationResponse>>
            getReorderRecommendations() {

        return ResponseEntity.ok(
                recommendationService
                        .getReorderRecommendations()
        );
    }
}