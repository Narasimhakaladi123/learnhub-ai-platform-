package com.career.guidance.controller;

import com.career.guidance.dto.response.ApiResponse;
import com.career.guidance.dto.response.SkillDto;
import com.career.guidance.entity.enums.SkillCategory;
import com.career.guidance.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/skills")
@Tag(name = "Skills Catalog", description = "Query technical skills, taxonomy categories, and search")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    @Operation(summary = "Get list of all skills, or filter by category/query")
    public ResponseEntity<ApiResponse<List<SkillDto>>> getSkills(
            @RequestParam(required = false) SkillCategory category,
            @RequestParam(required = false) String search) {
        List<SkillDto> skills;
        if (search != null && !search.isBlank()) {
            skills = skillService.searchSkills(search);
        } else if (category != null) {
            skills = skillService.getSkillsByCategory(category);
        } else {
            skills = skillService.getAllSkills();
        }
        return ResponseEntity.ok(ApiResponse.ok(skills));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get skill details by ID")
    public ResponseEntity<ApiResponse<SkillDto>> getSkillById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(skillService.getSkillById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COUNSELOR')")
    @Operation(summary = "Create a new technical skill entry")
    public ResponseEntity<ApiResponse<SkillDto>> createSkill(@Valid @RequestBody SkillDto dto) {
        SkillDto created = skillService.createSkill(dto);
        return new ResponseEntity<>(ApiResponse.ok("Skill created successfully", created), HttpStatus.CREATED);
    }
}
