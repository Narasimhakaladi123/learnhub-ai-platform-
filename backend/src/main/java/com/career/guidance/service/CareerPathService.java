package com.career.guidance.service;

import com.career.guidance.dto.response.CareerPathDto;

import java.util.List;
import java.util.UUID;

public interface CareerPathService {
    List<CareerPathDto> getAllCareers();
    CareerPathDto getCareerById(UUID id);
    CareerPathDto getCareerBySlug(String slug);
    List<CareerPathDto> getCareersByCategory(String category);
}
