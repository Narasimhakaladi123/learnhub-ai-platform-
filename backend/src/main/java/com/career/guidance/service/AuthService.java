package com.career.guidance.service;

import com.career.guidance.dto.request.LoginRequest;
import com.career.guidance.dto.request.RegisterRequest;
import com.career.guidance.dto.response.AuthResponse;
import com.career.guidance.entity.User;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    User getCurrentAuthenticatedUser();
}
