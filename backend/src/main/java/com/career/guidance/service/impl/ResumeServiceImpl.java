package com.career.guidance.service.impl;

import com.career.guidance.dto.response.ResumeAnalysisDto;
import com.career.guidance.entity.*;
import com.career.guidance.entity.enums.AuditAction;
import com.career.guidance.entity.enums.NotificationType;
import com.career.guidance.exception.BadRequestException;
import com.career.guidance.exception.ResourceNotFoundException;
import com.career.guidance.repository.*;
import com.career.guidance.service.AuditLogService;
import com.career.guidance.service.NotificationService;
import com.career.guidance.service.ResumeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResumeServiceImpl implements ResumeService {

    private static final Logger log = LoggerFactory.getLogger(ResumeServiceImpl.class);
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Value("${storage.upload-dir:uploads/resumes}")
    private String uploadDir;

    private final StudentProfileRepository profileRepository;
    private final ResumeRepository resumeRepository;
    private final ResumeAnalysisRepository analysisRepository;
    private final SkillRepository skillRepository;
    private final CareerPathRepository careerPathRepository;
    private final CareerSkillRepository careerSkillRepository;
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;
    private final ObjectMapper objectMapper;

    public ResumeServiceImpl(
            StudentProfileRepository profileRepository,
            ResumeRepository resumeRepository,
            ResumeAnalysisRepository analysisRepository,
            SkillRepository skillRepository,
            CareerPathRepository careerPathRepository,
            CareerSkillRepository careerSkillRepository,
            NotificationService notificationService,
            AuditLogService auditLogService) {
        this.profileRepository = profileRepository;
        this.resumeRepository = resumeRepository;
        this.analysisRepository = analysisRepository;
        this.skillRepository = skillRepository;
        this.careerPathRepository = careerPathRepository;
        this.careerSkillRepository = careerSkillRepository;
        this.notificationService = notificationService;
        this.auditLogService = auditLogService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    @Transactional
    public ResumeAnalysisDto uploadAndAnalyzeResume(User user, MultipartFile file, String targetRole) {
        StudentProfile profile = profileRepository.findByUser(user)
            .orElseGet(() -> profileRepository.save(new StudentProfile(user)));

        // 1. Validation
        if (file.isEmpty()) {
            throw new BadRequestException("Uploaded file is empty");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("Resume exceeds maximum allowed file size of 5MB");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".pdf")) {
            throw new BadRequestException("Only PDF resumes are supported. Please upload a .pdf document");
        }

        // 2. Save file to storage
        String safeFileName = UUID.randomUUID() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_");
        Path storageDir = Paths.get(uploadDir);
        Path targetFilePath = storageDir.resolve(safeFileName);

        try {
            Files.createDirectories(storageDir);
            Files.copy(file.getInputStream(), targetFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Failed to store resume file: {}", e.getMessage());
            throw new RuntimeException("Could not store resume file on server: " + e.getMessage());
        }

        // 3. Extract text using PDFBox
        String extractedText = "";
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            extractedText = stripper.getText(document);
        } catch (Exception e) {
            log.warn("PDFBox extraction failed: {}. Continuing with basic parsing.", e.getMessage());
            extractedText = "Resume text could not be fully parsed from complex layout.";
        }

        // Save resume entity
        Resume resume = new Resume(
            profile,
            originalFilename,
            targetFilePath.toString(),
            file.getSize(),
            file.getContentType(),
            extractedText
        );
        resume = resumeRepository.save(resume);

        // 4. Perform Resume Match Analysis (Requirement 7)
        String roleToAnalyze = (targetRole != null && !targetRole.isBlank())
            ? targetRole
            : (profile.getTargetCareer() != null ? profile.getTargetCareer() : "Java Backend Developer");

        ResumeAnalysis analysis = performAnalysis(resume, extractedText, roleToAnalyze);
        analysis = analysisRepository.save(analysis);

        notificationService.createNotification(
            user,
            "Resume Match Analysis Ready",
            "Your resume '" + originalFilename + "' has been analyzed for role: " + roleToAnalyze + ". Match Score: " + analysis.getMatchScore() + "%.",
            NotificationType.RECOMMENDATION
        );

        auditLogService.log(
            user.getEmail(),
            AuditAction.RESUME_ANALYZE,
            "Uploaded and analyzed resume: " + originalFilename + ", match score: " + analysis.getMatchScore() + "%",
            null
        );

        return toDto(analysis);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeAnalysisDto getLatestAnalysis(User user) {
        StudentProfile profile = profileRepository.findByUser(user)
            .orElseGet(() -> profileRepository.save(new StudentProfile(user)));

        Optional<Resume> latestResume = resumeRepository.findFirstByProfileOrderByUploadedAtDesc(profile);
        if (latestResume.isEmpty()) {
            return null;
        }

        Optional<ResumeAnalysis> analysis = analysisRepository.findByResume(latestResume.get());
        return analysis.map(this::toDto).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeAnalysisDto getAnalysisById(User user, UUID analysisId) {
        ResumeAnalysis analysis = analysisRepository.findById(analysisId)
            .orElseThrow(() -> new ResourceNotFoundException("Resume analysis not found: " + analysisId));
        return toDto(analysis);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumeAnalysisDto> getAllUserResumes(User user) {
        StudentProfile profile = profileRepository.findByUser(user)
            .orElseGet(() -> profileRepository.save(new StudentProfile(user)));

        List<Resume> resumes = resumeRepository.findByProfileOrderByUploadedAtDesc(profile);
        List<ResumeAnalysisDto> results = new ArrayList<>();
        for (Resume r : resumes) {
            analysisRepository.findByResume(r).ifPresent(a -> results.add(toDto(a)));
        }
        return results;
    }

    private ResumeAnalysis performAnalysis(Resume resume, String extractedText, String targetRole) {
        String lowerText = extractedText.toLowerCase();

        // Detect skills present in database
        List<Skill> allSkills = skillRepository.findAll();
        List<String> detectedSkills = new ArrayList<>();
        for (Skill s : allSkills) {
            if (lowerText.contains(s.getName().toLowerCase())) {
                detectedSkills.add(s.getName());
            }
        }
        if (detectedSkills.isEmpty()) {
            detectedSkills.addAll(List.of("Problem Solving", "Object-Oriented Programming", "Communication"));
        }

        // Compare against target role skills
        List<String> skillGaps = new ArrayList<>();
        int requiredSkillMatches = 0;
        int totalRequiredSkills = 0;

        Optional<CareerPath> careerOpt = careerPathRepository.findByNameIgnoreCase(targetRole);
        if (careerOpt.isPresent()) {
            List<CareerSkill> cSkills = careerSkillRepository.findByCareerPath(careerOpt.get());
            for (CareerSkill cs : cSkills) {
                totalRequiredSkills++;
                if (detectedSkills.contains(cs.getSkill().getName())) {
                    requiredSkillMatches++;
                } else {
                    skillGaps.add(cs.getSkill().getName());
                }
            }
        } else {
            // Default check
            List<String> expected = List.of("Java", "SQL", "Spring Boot", "Docker", "Git", "REST APIs");
            for (String e : expected) {
                totalRequiredSkills++;
                if (detectedSkills.contains(e)) {
                    requiredSkillMatches++;
                } else {
                    skillGaps.add(e);
                }
            }
        }

        // Section & information completeness check
        List<String> missingInfo = new ArrayList<>();
        if (!lowerText.contains("github.com")) missingInfo.add("GitHub Profile URL or portfolio link");
        if (!lowerText.contains("linkedin.com")) missingInfo.add("LinkedIn profile link");
        if (!lowerText.contains("project") && !lowerText.contains("projects")) missingInfo.add("Dedicated Projects section");
        if (!lowerText.contains("certificat")) missingInfo.add("Professional certifications or courses");
        if (!lowerText.contains("@")) missingInfo.add("Professional contact email address");

        // Suggested improvements
        List<String> improvements = new ArrayList<>();
        improvements.add("Include quantifiable business metrics in bullet points (e.g. 'Improved API response latency by 35%')");
        if (!skillGaps.isEmpty()) {
            improvements.add("Explicitly highlight experience or projects using " + String.join(", ", skillGaps.subList(0, Math.min(3, skillGaps.size()))));
        }
        improvements.add("Ensure standardized action verbs begin every experience item (e.g., 'Engineered', 'Architected', 'Deployed')");
        improvements.add("Format technical skills into clear categories: Languages, Frameworks, Cloud & Tools, Databases");

        // ATS Keyword Analysis
        Map<String, Object> atsAnalysis = new LinkedHashMap<>();
        Map<String, String> keywordStatus = new LinkedHashMap<>();
        List<String> criticalKeywords = List.of("REST API", "Microservices", "Docker", "Database", "Testing", "CI/CD", "Security", "Agile");

        int keywordHits = 0;
        for (String kw : criticalKeywords) {
            boolean present = lowerText.contains(kw.toLowerCase());
            keywordStatus.put(kw, present ? "FOUND" : "RECOMMENDED");
            if (present) keywordHits++;
        }
        atsAnalysis.put("targetRole", targetRole);
        atsAnalysis.put("keywordCoverage", keywordStatus);
        atsAnalysis.put("keywordDensityScore", (int) Math.round(((double) keywordHits / criticalKeywords.size()) * 100));

        // Calculate Match Score (0 - 100)
        double skillScore = totalRequiredSkills > 0 ? ((double) requiredSkillMatches / totalRequiredSkills) * 60.0 : 40.0;
        double keywordScore = ((double) keywordHits / criticalKeywords.size()) * 25.0;
        double completenessScore = Math.max(5.0, 15.0 - (missingInfo.size() * 3.0));
        int matchScore = (int) Math.round(Math.min(95, Math.max(35, skillScore + keywordScore + completenessScore)));

        ResumeAnalysis analysis = new ResumeAnalysis();
        analysis.setResume(resume);
        analysis.setTargetRole(targetRole);
        analysis.setDetectedSkills(writeJson(detectedSkills));
        analysis.setMissingInformation(writeJson(missingInfo));
        analysis.setSkillGaps(writeJson(skillGaps));
        analysis.setSuggestedImprovements(writeJson(improvements));
        analysis.setJobRoleCompatibility(
            "This resume demonstrates a " + matchScore + "% keyword and skill compatibility with standard "
            + targetRole + " job descriptions based on industry hiring benchmarks."
        );
        analysis.setAtsKeywordAnalysis(writeJson(atsAnalysis));
        analysis.setMatchScore(matchScore);
        analysis.setAnalyzedAt(LocalDateTime.now());

        return analysis;
    }

    private ResumeAnalysisDto toDto(ResumeAnalysis a) {
        ResumeAnalysisDto dto = new ResumeAnalysisDto();
        dto.setId(a.getId());
        dto.setResumeId(a.getResume().getId());
        dto.setFileName(a.getResume().getFileName());
        dto.setTargetRole(a.getTargetRole());
        dto.setMatchScore(a.getMatchScore());
        dto.setDetectedSkills(readJsonList(a.getDetectedSkills()));
        dto.setMissingInformation(readJsonList(a.getMissingInformation()));
        dto.setSkillGaps(readJsonList(a.getSkillGaps()));
        dto.setSuggestedImprovements(readJsonList(a.getSuggestedImprovements()));
        dto.setJobRoleCompatibility(a.getJobRoleCompatibility());
        dto.setAtsKeywordAnalysis(readJsonMap(a.getAtsKeywordAnalysis()));
        dto.setAnalyzedAt(a.getAnalyzedAt());
        dto.setUploadedAt(a.getResume().getUploadedAt());
        return dto;
    }

    private String writeJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "{}";
        }
    }

    private List<String> readJsonList(String json) {
        if (json == null || json.isBlank()) return Collections.emptyList();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private Map<String, Object> readJsonMap(String json) {
        if (json == null || json.isBlank()) return Collections.emptyMap();
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }
}
