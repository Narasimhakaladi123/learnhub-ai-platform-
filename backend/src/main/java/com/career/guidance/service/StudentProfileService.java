package com.career.guidance.service;

import com.career.guidance.dto.request.*;
import com.career.guidance.dto.response.DashboardSummaryDto;
import com.career.guidance.dto.response.UserProfileResponse;
import com.career.guidance.entity.User;

public interface StudentProfileService {
    UserProfileResponse getProfile(User user);
    UserProfileResponse updateProfile(User user, UpdateProfileRequest request);
    UserProfileResponse addSkill(User user, AddSkillRequest request);
    UserProfileResponse removeSkill(User user, java.util.UUID skillId);
    UserProfileResponse addEducation(User user, AddEducationRequest request);
    UserProfileResponse addExperience(User user, AddExperienceRequest request);
    UserProfileResponse addInternship(User user, AddInternshipRequest request);
    UserProfileResponse addProject(User user, AddProjectRequest request);
    UserProfileResponse addCertification(User user, AddCertificationRequest request);
    UserProfileResponse updateInterests(User user, UpdateInterestsRequest request);
    DashboardSummaryDto getDashboardSummary(User user);
}
