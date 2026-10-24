package com.career.guidance.service;

import com.career.guidance.dto.response.ResumeAnalysisDto;
import com.career.guidance.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ResumeService {
    ResumeAnalysisDto uploadAndAnalyzeResume(User user, MultipartFile file, String targetRole);
    ResumeAnalysisDto getLatestAnalysis(User user);
    ResumeAnalysisDto getAnalysisById(User user, UUID analysisId);
    List<ResumeAnalysisDto> getAllUserResumes(User user);
}
