package com.career.guidance.controller;

import com.career.guidance.dto.response.ApiResponse;
import com.career.guidance.dto.response.ResumeAnalysisDto;
import com.career.guidance.entity.User;
import com.career.guidance.service.AuthService;
import com.career.guidance.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resumes")
@Tag(name = "Resume ATS Analyzer", description = "PDF resume upload, text extraction, ATS scoring, and keyword suggestions")
public class ResumeController {

    private final ResumeService resumeService;
    private final AuthService authService;

    public ResumeController(ResumeService resumeService, AuthService authService) {
        this.resumeService = resumeService;
        this.authService = authService;
    }

    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload PDF resume and perform ATS keyword & gap analysis")
    public ResponseEntity<ApiResponse<ResumeAnalysisDto>> uploadAndAnalyze(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "targetRole", required = false) String targetRole) {
        User user = authService.getCurrentAuthenticatedUser();
        ResumeAnalysisDto analysis = resumeService.uploadAndAnalyzeResume(user, file, targetRole);
        return ResponseEntity.ok(ApiResponse.ok("Resume analyzed successfully", analysis));
    }

    @GetMapping("/latest")
    @Operation(summary = "Get the latest resume ATS analysis report")
    public ResponseEntity<ApiResponse<ResumeAnalysisDto>> getLatestAnalysis() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok(resumeService.getLatestAnalysis(user)));
    }

    @GetMapping("/{analysisId}")
    @Operation(summary = "Get specific resume ATS analysis by ID")
    public ResponseEntity<ApiResponse<ResumeAnalysisDto>> getAnalysisById(@PathVariable UUID analysisId) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok(resumeService.getAnalysisById(user, analysisId)));
    }

    @GetMapping("/history")
    @Operation(summary = "Get full history of uploaded resumes and scores")
    public ResponseEntity<ApiResponse<List<ResumeAnalysisDto>>> getAllResumes() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok(resumeService.getAllUserResumes(user)));
    }
}
