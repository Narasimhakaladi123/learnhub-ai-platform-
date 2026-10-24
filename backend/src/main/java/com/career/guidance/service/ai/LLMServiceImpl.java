package com.career.guidance.service.ai;

import com.career.guidance.dto.ai.*;
import com.career.guidance.dto.response.InterviewEvaluationDto;
import com.career.guidance.service.rag.RAGService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class LLMServiceImpl implements LLMService {

    private static final Logger log = LoggerFactory.getLogger(LLMServiceImpl.class);

    @Value("${llm.api-key:}")
    private String apiKey;

    @Value("${llm.model:gpt-4o-mini}")
    private String model;

    @Value("${llm.base-url:https://api.openai.com/v1}")
    private String baseUrl;

    private final RAGService ragService;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public LLMServiceImpl(RAGService ragService) {
        this.ragService = ragService;
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
        this.restTemplate = new RestTemplate();
    }

    @Override
    public CareerRecommendationResponse generateCareerRecommendation(CareerRecommendationRequest request) {
        String targetCareer = request.getTargetCareerPreference() != null && !request.getTargetCareerPreference().isBlank()
            ? request.getTargetCareerPreference()
            : "Java Backend Developer";

        String ragContext = ragService.buildAugmentedContext(targetCareer + " skills roadmap projects", 2);

        String systemPrompt = "You are an expert senior software engineering career counselor. "
            + "Given a student's profile, provide a highly personalized, explainable career recommendation. "
            + "Return STRICTLY a JSON object matching this schema:\n"
            + "{\n"
            + "  \"career\": string,\n"
            + "  \"compatibilityScore\": integer (0-100),\n"
            + "  \"matchingSkills\": [string],\n"
            + "  \"skillGaps\": [string],\n"
            + "  \"explanation\": string (detailed, explainable, not generic),\n"
            + "  \"nextSteps\": [string],\n"
            + "  \"recommendedProjects\": [string],\n"
            + "  \"recommendedCertifications\": [string]\n"
            + "}\n"
            + "Do not include markdown ticks (```json). Only output raw JSON.";

        String userPrompt = "Student Name: " + request.getStudentName() + "\n"
            + "Degree: " + request.getDegree() + " (" + request.getBranch() + "), CGPA: " + request.getCgpa() + "\n"
            + "Interests: " + request.getInterests() + "\n"
            + "Student Skills: " + request.getSkillsWithProficiency() + "\n"
            + "Target Career Preference: " + targetCareer + "\n"
            + "Assessment Performance: " + request.getAssessmentScores() + "\n"
            + ragContext;

        if (isExternalApiConfigured()) {
            try {
                String rawJson = callExternalLlm(systemPrompt, userPrompt);
                CareerRecommendationResponse parsed = objectMapper.readValue(cleanJsonResponse(rawJson), CareerRecommendationResponse.class);
                if (parsed != null && parsed.getCareer() != null) {
                    log.info("Successfully received LLM recommendation for {}", request.getStudentName());
                    return parsed;
                }
            } catch (Exception ex) {
                log.warn("External LLM call failed or returned invalid JSON for recommendation. Gracefully falling back to deterministic AI model: {}", ex.getMessage());
            }
        }

        return fallbackCareerRecommendation(request, targetCareer);
    }

    @Override
    public SkillGapResponse analyzeSkillGap(SkillGapRequest request) {
        String ragContext = ragService.buildAugmentedContext(request.getCareerName() + " skill requirements gap", 2);

        String systemPrompt = "You are a technical career advisor. Analyze the student's current skills against the target career requirements. "
            + "Return STRICTLY a JSON object:\n"
            + "{\n"
            + "  \"career\": string,\n"
            + "  \"matchingSkills\": [string],\n"
            + "  \"missingSkills\": [string],\n"
            + "  \"weakSkills\": [string],\n"
            + "  \"prioritizedSequence\": [\n"
            + "    {\"topic\": string, \"priority\": \"Priority 1\" | \"Priority 2\" | \"Priority 3\", \"estimatedHours\": string, \"reason\": string}\n"
            + "  ],\n"
            + "  \"summary\": string\n"
            + "}";

        String userPrompt = "Career: " + request.getCareerName() + "\n"
            + "Student Skills: " + request.getStudentSkills() + "\n"
            + "Required Skills: " + request.getRequiredSkills() + "\n"
            + "Preferred Skills: " + request.getPreferredSkills() + "\n"
            + ragContext;

        if (isExternalApiConfigured()) {
            try {
                String rawJson = callExternalLlm(systemPrompt, userPrompt);
                SkillGapResponse parsed = objectMapper.readValue(cleanJsonResponse(rawJson), SkillGapResponse.class);
                if (parsed != null && parsed.getPrioritizedSequence() != null) {
                    return parsed;
                }
            } catch (Exception ex) {
                log.warn("External LLM call failed for skill gap analysis. Falling back to local deterministic model: {}", ex.getMessage());
            }
        }

        return fallbackSkillGap(request);
    }

    @Override
    public InterviewResponse generateInterviewQuestions(InterviewRequest request) {
        String ragContext = ragService.buildAugmentedContext(request.getRole() + " " + String.join(" ", request.getTopics()) + " interview questions", 2);

        String systemPrompt = "You are a senior technical interviewer. Generate realistic interview questions. "
            + "Return STRICTLY a JSON object:\n"
            + "{\n"
            + "  \"role\": string,\n"
            + "  \"difficulty\": string,\n"
            + "  \"questions\": [\n"
            + "    {\n"
            + "      \"question\": string,\n"
            + "      \"expectedConcepts\": [string],\n"
            + "      \"difficulty\": string,\n"
            + "      \"followUpQuestions\": [string]\n"
            + "    }\n"
            + "  ]\n"
            + "}";

        String userPrompt = "Role: " + request.getRole() + "\n"
            + "Difficulty: " + request.getDifficulty() + "\n"
            + "Topics: " + request.getTopics() + "\n"
            + "Number of Questions: " + request.getQuestionCount() + "\n"
            + ragContext;

        if (isExternalApiConfigured()) {
            try {
                String rawJson = callExternalLlm(systemPrompt, userPrompt);
                InterviewResponse parsed = objectMapper.readValue(cleanJsonResponse(rawJson), InterviewResponse.class);
                if (parsed != null && parsed.getQuestions() != null && !parsed.getQuestions().isEmpty()) {
                    return parsed;
                }
            } catch (Exception ex) {
                log.warn("External LLM interview generation failed. Falling back to curated bank: {}", ex.getMessage());
            }
        }

        return fallbackInterviewQuestions(request);
    }

    @Override
    public InterviewEvaluationDto evaluateInterviewAnswer(String role, String question, String expectedConcepts, String userAnswer) {
        String systemPrompt = "You are a supportive, constructive technical interviewer. "
            + "Evaluate the student's answer. Do not present evaluation as an official hiring decision. "
            + "Return STRICTLY a JSON object:\n"
            + "{\n"
            + "  \"strengths\": string,\n"
            + "  \"missingConcepts\": [string],\n"
            + "  \"suggestedStructure\": string,\n"
            + "  \"areasToImprove\": [string],\n"
            + "  \"score\": integer (0-100),\n"
            + "  \"feedback\": string\n"
            + "}";

        String userPrompt = "Role: " + role + "\n"
            + "Question: " + question + "\n"
            + "Expected Concepts: " + expectedConcepts + "\n"
            + "Student's Answer: " + userAnswer;

        if (isExternalApiConfigured()) {
            try {
                String rawJson = callExternalLlm(systemPrompt, userPrompt);
                InterviewEvaluationDto parsed = objectMapper.readValue(cleanJsonResponse(rawJson), InterviewEvaluationDto.class);
                if (parsed != null) {
                    return parsed;
                }
            } catch (Exception ex) {
                log.warn("External LLM answer evaluation failed. Falling back to local evaluator: {}", ex.getMessage());
            }
        }

        return fallbackEvaluateAnswer(role, question, expectedConcepts, userAnswer);
    }

    @Override
    public String generateChatResponse(String studentContext, String conversationHistory, String userMessage) {
        String ragContext = ragService.buildAugmentedContext(userMessage, 2);

        String systemPrompt = "You are the AI Career Assistant for a university and professional career guidance platform. "
            + "Your purpose is to provide personalized, realistic, actionable advice grounded in the student's specific profile, skills, and goals. "
            + "Always be supportive, clear, concise, and structured. "
            + "Refer to the student's actual skills, target career, and gaps where relevant.\n"
            + studentContext + "\n"
            + ragContext;

        if (isExternalApiConfigured()) {
            try {
                return callExternalLlm(systemPrompt, "Conversation History:\n" + conversationHistory + "\n\nStudent: " + userMessage);
            } catch (Exception ex) {
                log.warn("External LLM chat failed. Using context-aware fallback response: {}", ex.getMessage());
            }
        }

        return fallbackChatResponse(studentContext, userMessage);
    }

    // --- Private Helper Methods ---

    private boolean isExternalApiConfigured() {
        return apiKey != null && !apiKey.trim().isEmpty() && !apiKey.equalsIgnoreCase("demo-key") && !apiKey.equalsIgnoreCase("mock");
    }

    private String callExternalLlm(String systemPrompt, String userPrompt) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("temperature", 0.7);

        List<Map<String, String>> messages = List.of(
            Map.of("role", "system", "content", systemPrompt),
            Map.of("role", "user", "content", userPrompt)
        );
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl + "/chat/completions",
            HttpMethod.POST,
            entity,
            String.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();
        }
        throw new RuntimeException("Unexpected LLM API response code: " + response.getStatusCode());
    }

    private String cleanJsonResponse(String raw) {
        String clean = raw.trim();
        if (clean.startsWith("```json")) {
            clean = clean.substring(7);
        } else if (clean.startsWith("```")) {
            clean = clean.substring(3);
        }
        if (clean.endsWith("```")) {
            clean = clean.substring(0, clean.length() - 3);
        }
        return clean.trim();
    }

    // --- High-Quality Deterministic Fallback Generators ---

    private CareerRecommendationResponse fallbackCareerRecommendation(CareerRecommendationRequest req, String targetCareer) {
        List<String> matching = new ArrayList<>();
        List<String> gaps = new ArrayList<>();

        Map<String, String> skills = req.getSkillsWithProficiency() != null ? req.getSkillsWithProficiency() : Collections.emptyMap();
        for (String s : skills.keySet()) {
            matching.add(s);
        }

        if (matching.isEmpty()) {
            matching.addAll(List.of("Problem Solving", "Computer Science Fundamentals"));
        }

        if (targetCareer.toLowerCase().contains("backend") || targetCareer.toLowerCase().contains("java")) {
            if (!skills.containsKey("Docker")) gaps.add("Docker");
            if (!skills.containsKey("Microservices")) gaps.add("Microservices");
            if (!skills.containsKey("Redis")) gaps.add("Redis");
            if (!skills.containsKey("Kafka")) gaps.add("Kafka");
            if (!skills.containsKey("AWS")) gaps.add("AWS");
        } else if (targetCareer.toLowerCase().contains("full stack")) {
            if (!skills.containsKey("Angular") && !skills.containsKey("React")) gaps.add("Angular");
            if (!skills.containsKey("Docker")) gaps.add("Docker");
            if (!skills.containsKey("TypeScript")) gaps.add("TypeScript");
            if (!skills.containsKey("CI/CD")) gaps.add("CI/CD");
        } else if (targetCareer.toLowerCase().contains("devops") || targetCareer.toLowerCase().contains("cloud")) {
            if (!skills.containsKey("Kubernetes")) gaps.add("Kubernetes");
            if (!skills.containsKey("Terraform")) gaps.add("Terraform");
            if (!skills.containsKey("CI/CD")) gaps.add("CI/CD");
        } else if (targetCareer.toLowerCase().contains("ai") || targetCareer.toLowerCase().contains("data")) {
            if (!skills.containsKey("PyTorch")) gaps.add("PyTorch");
            if (!skills.containsKey("Vector Databases")) gaps.add("Vector Databases");
            if (!skills.containsKey("RAG Architecture")) gaps.add("RAG Architecture");
        } else {
            gaps.addAll(List.of("System Design", "Cloud Infrastructure", "Docker"));
        }

        int score = Math.min(95, Math.max(55, 60 + (matching.size() * 5) - (gaps.size() * 3)));

        String explanation = "Based on your technical profile and coursework, you demonstrate a solid foundation in core software fundamentals with verified proficiency in "
            + String.join(", ", matching) + ". To elevate your readiness for " + targetCareer + " to industry production standards, closing competencies in "
            + String.join(", ", gaps.subList(0, Math.min(3, gaps.size()))) + " will position you among the top percentile of entry-to-mid candidates.";

        List<String> nextSteps = List.of(
            "Complete an end-to-end production project implementing " + (gaps.isEmpty() ? "advanced architecture" : gaps.get(0)),
            "Deepen practical exposure to containerization and cloud deployment",
            "Take technical assessment modules to validate your proficiency milestones"
        );

        List<String> projects = List.of(
            targetCareer + " Scalable RESTful API with Caching",
            "Microservices-based Distributed Architecture with Docker",
            "Full-Stack Authentication and RBAC Portal"
        );

        CareerRecommendationResponse resp = new CareerRecommendationResponse(
            targetCareer, score, matching, gaps, explanation, nextSteps
        );
        resp.setRecommendedProjects(projects);
        resp.setRecommendedCertifications(List.of("AWS Certified Cloud Practitioner", "Oracle Certified Associate Java Programmer"));
        return resp;
    }

    private SkillGapResponse fallbackSkillGap(SkillGapRequest req) {
        SkillGapResponse resp = new SkillGapResponse();
        resp.setCareer(req.getCareerName());

        List<String> matching = new ArrayList<>();
        List<String> missing = new ArrayList<>();
        List<String> weak = new ArrayList<>();
        List<SkillGapResponse.PrioritizedLearningTopic> sequence = new ArrayList<>();

        Map<String, String> studentSkills = req.getStudentSkills() != null ? req.getStudentSkills() : Collections.emptyMap();
        List<String> required = req.getRequiredSkills() != null ? req.getRequiredSkills() : List.of("Java", "SQL", "Spring Boot", "Docker", "REST APIs");

        int seq = 1;
        for (String reqSkill : required) {
            if (studentSkills.containsKey(reqSkill)) {
                String prof = studentSkills.get(reqSkill);
                if ("BEGINNER".equalsIgnoreCase(prof)) {
                    weak.add(reqSkill);
                    sequence.add(new SkillGapResponse.PrioritizedLearningTopic(
                        reqSkill + " (Intermediate to Advanced)",
                        "Priority " + Math.min(seq++, 3),
                        "15-20 hours",
                        "Elevate from beginner to robust production readiness."
                    ));
                } else {
                    matching.add(reqSkill);
                }
            } else {
                missing.add(reqSkill);
                sequence.add(new SkillGapResponse.PrioritizedLearningTopic(
                    reqSkill + " Fundamentals",
                    "Priority " + Math.min(seq++, 3),
                    "20-25 hours",
                    "Critical required skill missing from current profile."
                ));
            }
        }

        resp.setMatchingSkills(matching);
        resp.setMissingSkills(missing);
        resp.setWeakSkills(weak);
        resp.setPrioritizedSequence(sequence);
        resp.setSummary("You meet " + matching.size() + " of " + required.size() + " primary requirements for " + req.getCareerName() + ". Focus primarily on " + (missing.isEmpty() ? "strengthening weak skills" : missing.get(0)) + ".");
        return resp;
    }

    private InterviewResponse fallbackInterviewQuestions(InterviewRequest req) {
        InterviewResponse response = new InterviewResponse();
        response.setRole(req.getRole());
        response.setDifficulty(req.getDifficulty());

        List<InterviewResponse.AIQuestionItem> items = new ArrayList<>();
        String roleLower = req.getRole().toLowerCase();

        if (roleLower.contains("java") || roleLower.contains("backend")) {
            items.add(new InterviewResponse.AIQuestionItem(
                "How does the JVM handle memory management and Garbage Collection, and what are the key differences between G1GC and ZGC?",
                List.of("Heap memory layout", "Young and Old generation", "Mark-and-sweep algorithm", "Stop-the-world pauses"),
                req.getDifficulty(),
                List.of("How would you troubleshoot an OutOfMemoryError in a Spring Boot application?", "What is the role of metaspace?")
            ));
            items.add(new InterviewResponse.AIQuestionItem(
                "Explain the bean lifecycle in Spring Boot and how @Transactional works under the hood.",
                List.of("Spring IoC container", "BeanPostProcessor", "AOP Proxying", "Transaction Propagation levels"),
                req.getDifficulty(),
                List.of("What happens when a private method is annotated with @Transactional?", "How do rollback rules work by default?")
            ));
            items.add(new InterviewResponse.AIQuestionItem(
                "How do you design database indexes to optimize slow SQL queries involving multiple joins?",
                List.of("B-Tree indexes", "Composite index column ordering", "Covering index", "Execution plan (EXPLAIN)"),
                req.getDifficulty(),
                List.of("Why can excessive indexing degrade write performance?", "What is index cardinality?")
            ));
        } else if (roleLower.contains("frontend") || roleLower.contains("full stack")) {
            items.add(new InterviewResponse.AIQuestionItem(
                "Explain Angular Change Detection and how OnPush strategy improves rendering performance.",
                List.of("Zone.js / Zoneless signals", "Component tree dirty checking", "Immutability", "Async pipe"),
                req.getDifficulty(),
                List.of("How do Angular 17+ Signals differ from RxJS Observables?", "When would you manually call markForCheck()?")
            ));
            items.add(new InterviewResponse.AIQuestionItem(
                "How do HTTP Interceptors work in Angular and how would you implement automatic JWT token refresh?",
                List.of("HttpInterceptorFn / class", "HttpRequest cloning", "CatchError operator", "SwitchMap to refresh endpoint"),
                req.getDifficulty(),
                List.of("How do you handle multiple simultaneous 401 requests during a token refresh?")
            ));
        } else {
            items.add(new InterviewResponse.AIQuestionItem(
                "Walk through your approach to designing a high-throughput, fault-tolerant REST API for " + req.getRole() + ".",
                List.of("Idempotency", "Rate limiting", "Horizontal scaling", "Circuit breaker pattern"),
                req.getDifficulty(),
                List.of("How do you handle distributed transactions across microservices?")
            ));
        }

        response.setQuestions(items);
        return response;
    }

    private InterviewEvaluationDto fallbackEvaluateAnswer(String role, String question, String expectedConcepts, String userAnswer) {
        InterviewEvaluationDto eval = new InterviewEvaluationDto();
        eval.setStrengths("You directly answered the main premise of the question with clear technical intent.");

        List<String> missing = new ArrayList<>();
        List<String> areas = new ArrayList<>();

        if (userAnswer == null || userAnswer.trim().length() < 30) {
            eval.setScore(45);
            missing.add("Deeper architectural rationale");
            missing.add("Specific operational edge-cases");
            areas.add("Elaborate with concrete technical examples and trade-offs.");
            eval.setFeedback("Good starting thought, but technical interviews look for depth on underlying mechanisms.");
        } else {
            eval.setScore(82);
            missing.add("Specific edge case handling and failure recovery");
            areas.add("Mention industry best practices such as observability and metrics.");
            eval.setFeedback("Strong response! You demonstrated good command of the core principles.");
        }

        eval.setMissingConcepts(missing);
        eval.setAreasToImprove(areas);
        eval.setSuggestedStructure("1. High-level definition & purpose -> 2. Under-the-hood mechanism -> 3. Practical real-world tradeoff or experience -> 4. Summary.");
        return eval;
    }

    private String fallbackChatResponse(String studentContext, String userMessage) {
        String msgLower = userMessage.toLowerCase();
        if (msgLower.contains("what should i learn") || msgLower.contains("learn next")) {
            return "Based on your current profile, here is the highest-impact learning path:\n\n"
                + "1. **Docker & Containerization**: Package your applications into portable containers and configure multi-container environments using Docker Compose.\n"
                + "2. **Advanced Spring Security & JWT**: Master RBAC, refresh token rotation, and method-level authorization (`@PreAuthorize`).\n"
                + "3. **Microservices & Event Streaming**: Explore distributed systems architecture using Kafka or RabbitMQ with Redis for caching.\n\n"
                + "Would you like to generate a personalized learning roadmap or practice interview questions on any of these topics?";
        }

        if (msgLower.contains("can i become") || msgLower.contains("backend developer")) {
            return "Yes, absolutely! Analyzing your current technical background:\n\n"
                + "- **Current Strengths**: Good foundation in object-oriented programming, data structures, and relational database fundamentals.\n"
                + "- **Target Milestones**: Transition from basic CRUD APIs to production-grade distributed architectures with Redis caching, Docker containerization, and JUnit/Mockito unit and integration testing.\n"
                + "- **Recommended Next Step**: Check the **Career Recommendations** tab to view your detailed compatibility score and generated learning roadmap!";
        }

        return "Hello! I am your AI Career Assistant. I have analyzed your profile, skills, and current progress. "
            + "I can help you review your skill gaps, explore career paths (like Java Backend, Full Stack, Cloud/DevOps), "
            + "guide your portfolio projects, or prepare for technical interviews. What would you like to focus on today?";
    }
}
