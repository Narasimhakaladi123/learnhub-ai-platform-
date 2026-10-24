package com.career.guidance.service.impl;

import com.career.guidance.dto.request.AIChatRequest;
import com.career.guidance.dto.response.AIChatResponse;
import com.career.guidance.entity.*;
import com.career.guidance.entity.enums.AuditAction;
import com.career.guidance.exception.ResourceNotFoundException;
import com.career.guidance.repository.*;
import com.career.guidance.service.AICareerAssistantService;
import com.career.guidance.service.AuditLogService;
import com.career.guidance.service.ai.LLMService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AICareerAssistantServiceImpl implements AICareerAssistantService {

    private final AIConversationRepository conversationRepository;
    private final AIMessageRepository messageRepository;
    private final StudentProfileRepository profileRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final StudentInterestRepository interestRepository;
    private final SkillGapRepository skillGapRepository;
    private final LLMService llmService;
    private final AuditLogService auditLogService;

    public AICareerAssistantServiceImpl(
            AIConversationRepository conversationRepository,
            AIMessageRepository messageRepository,
            StudentProfileRepository profileRepository,
            StudentSkillRepository studentSkillRepository,
            StudentInterestRepository interestRepository,
            SkillGapRepository skillGapRepository,
            LLMService llmService,
            AuditLogService auditLogService) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.profileRepository = profileRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.interestRepository = interestRepository;
        this.skillGapRepository = skillGapRepository;
        this.llmService = llmService;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional
    public AIChatResponse sendMessage(User user, AIChatRequest request) {
        StudentProfile profile = profileRepository.findByUser(user)
            .orElseGet(() -> profileRepository.save(new StudentProfile(user)));

        AIConversation conversation;
        if (request.getConversationId() != null) {
            conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found: " + request.getConversationId()));
        } else {
            Optional<AIConversation> latest = conversationRepository.findFirstByProfileOrderByUpdatedAtDesc(profile);
            conversation = latest.orElseGet(() -> conversationRepository.save(new AIConversation(profile, "Career Guidance Session")));
        }

        // Save User Message
        AIMessage userMsg = new AIMessage(conversation, "USER", request.getMessage());
        messageRepository.save(userMsg);

        // Build authenticated student context
        String studentContext = buildStudentContext(user, profile);

        // Fetch recent conversation history
        List<AIMessage> history = messageRepository.findByConversationOrderByTimestampAsc(conversation);
        StringBuilder historyBuilder = new StringBuilder();
        for (AIMessage msg : history.subList(Math.max(0, history.size() - 6), history.size())) {
            historyBuilder.append(msg.getRole()).append(": ").append(msg.getContent()).append("\n");
        }

        // Generate response from LLMService
        String assistantResponse = llmService.generateChatResponse(
            studentContext,
            historyBuilder.toString(),
            request.getMessage()
        );

        // Save Assistant Message
        AIMessage assistantMsg = new AIMessage(conversation, "ASSISTANT", assistantResponse);
        assistantMsg = messageRepository.save(assistantMsg);

        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        auditLogService.log(user.getEmail(), AuditAction.AI_CHAT, "Sent career assistant message", null);

        return new AIChatResponse(
            conversation.getId(),
            assistantMsg.getId(),
            assistantMsg.getRole(),
            assistantMsg.getContent()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<AIChatResponse> getConversationMessages(User user, UUID conversationId) {
        AIConversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new ResourceNotFoundException("Conversation not found: " + conversationId));

        return messageRepository.findByConversationOrderByTimestampAsc(conversation).stream()
            .map(m -> new AIChatResponse(conversation.getId(), m.getId(), m.getRole(), m.getContent()))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AIChatResponse> getActiveConversationHistory(User user) {
        StudentProfile profile = profileRepository.findByUser(user)
            .orElseGet(() -> profileRepository.save(new StudentProfile(user)));

        Optional<AIConversation> latest = conversationRepository.findFirstByProfileOrderByUpdatedAtDesc(profile);
        if (latest.isEmpty()) {
            return Collections.emptyList();
        }

        return messageRepository.findByConversationOrderByTimestampAsc(latest.get()).stream()
            .map(m -> new AIChatResponse(latest.get().getId(), m.getId(), m.getRole(), m.getContent()))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void clearConversation(User user, UUID conversationId) {
        AIConversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new ResourceNotFoundException("Conversation not found: " + conversationId));

        messageRepository.deleteAll(conversation.getMessages());
        conversation.getMessages().clear();
        conversationRepository.save(conversation);
    }

    private String buildStudentContext(User user, StudentProfile profile) {
        List<StudentSkill> skills = studentSkillRepository.findByProfile(profile);
        String skillsStr = skills.stream()
            .map(s -> s.getSkill().getName() + " (" + s.getProficiency() + ")")
            .collect(Collectors.joining(", "));

        List<StudentInterest> interests = interestRepository.findByProfile(profile);
        String interestsStr = interests.stream()
            .map(i -> i.getInterestArea().name())
            .collect(Collectors.joining(", "));

        List<SkillGap> gaps = skillGapRepository.findByProfileOrderBySequenceOrderAsc(profile);
        String gapsStr = gaps.stream()
            .limit(4)
            .map(SkillGap::getSkillName)
            .collect(Collectors.joining(", "));

        return "--- AUTHENTICATED STUDENT CONTEXT ---\n"
            + "Student Name: " + user.getFullName() + "\n"
            + "Target Career: " + (profile.getTargetCareer() != null ? profile.getTargetCareer() : "Software Engineer") + "\n"
            + "Readiness Score: " + profile.getReadinessPercentage() + "%\n"
            + "Academic Background: " + (profile.getDegree() != null ? profile.getDegree() : "Engineering") + " - " + (profile.getBranch() != null ? profile.getBranch() : "CSE") + "\n"
            + "Current Skills: " + (skillsStr.isEmpty() ? "None recorded yet" : skillsStr) + "\n"
            + "Identified Skill Gaps: " + (gapsStr.isEmpty() ? "None" : gapsStr) + "\n"
            + "Interests: " + (interestsStr.isEmpty() ? "General Software Engineering" : interestsStr) + "\n"
            + "--- END STUDENT CONTEXT ---";
    }
}
