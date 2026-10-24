package com.career.guidance.controller;

import com.career.guidance.dto.response.ApiResponse;
import com.career.guidance.dto.response.CareerPathDto;
import com.career.guidance.service.CareerPathService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/careers")
@Tag(name = "Career Paths", description = "Browse industry career paths, required competencies, and salary benchmarks")
public class CareerPathController {

    private final CareerPathService careerPathService;

    public CareerPathController(CareerPathService careerPathService) {
        this.careerPathService = careerPathService;
    }

    @GetMapping
    @Operation(summary = "Get all available career paths")
    public ResponseEntity<ApiResponse<List<CareerPathDto>>> getAllCareers(@RequestParam(required = false) String category) {
        List<CareerPathDto> careers = (category != null && !category.isBlank())
            ? careerPathService.getCareersByCategory(category)
            : careerPathService.getAllCareers();
        return ResponseEntity.ok(ApiResponse.ok(careers));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get career path details by ID")
    public ResponseEntity<ApiResponse<CareerPathDto>> getCareerById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(careerPathService.getCareerById(id)));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get career path details by URL slug")
    public ResponseEntity<ApiResponse<CareerPathDto>> getCareerBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.ok(careerPathService.getCareerBySlug(slug)));
    }
}
