package com.career.guidance.service.impl;

import com.career.guidance.dto.request.LoginRequest;
import com.career.guidance.dto.request.RegisterRequest;
import com.career.guidance.dto.response.AuthResponse;
import com.career.guidance.entity.StudentProfile;
import com.career.guidance.entity.User;
import com.career.guidance.entity.enums.AuditAction;
import com.career.guidance.entity.enums.RoleType;
import com.career.guidance.exception.BadRequestException;
import com.career.guidance.exception.UnauthorizedException;
import com.career.guidance.repository.StudentProfileRepository;
import com.career.guidance.repository.UserRepository;
import com.career.guidance.security.JwtTokenProvider;
import com.career.guidance.service.AuditLogService;
import com.career.guidance.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final StudentProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AuditLogService auditLogService;

    public AuthServiceImpl(UserRepository userRepository,
                           StudentProfileRepository profileRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider tokenProvider,
                           AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Password and confirmation password do not match");
        }

        if (userRepository.existsByEmail(request.getEmail().toLowerCase().trim())) {
            throw new BadRequestException("Email is already registered: " + request.getEmail());
        }

        RoleType role = RoleType.ROLE_STUDENT;
        if (request.getRole() != null && request.getRole() != RoleType.ROLE_STUDENT) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleType.ROLE_ADMIN.name()));
            if (isAdmin) {
                role = request.getRole();
            }
        }

        User user = new User(
            request.getEmail().toLowerCase().trim(),
            passwordEncoder.encode(request.getPassword()),
            request.getFullName().trim(),
            role
        );
        user = userRepository.save(user);

        if (role == RoleType.ROLE_STUDENT) {
            StudentProfile profile = new StudentProfile(user);
            profileRepository.save(profile);
        }

        auditLogService.log(user.getEmail(), AuditAction.USER_REGISTER, "User registered with role: " + role, null);

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getEmail(), request.getPassword())
        );

        String token = tokenProvider.generateToken(authentication);

        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail().toLowerCase().trim(),
                request.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
            .orElseThrow(() -> new UnauthorizedException("User not found"));

        auditLogService.log(user.getEmail(), AuditAction.USER_LOGIN, "Successful login", null);

        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole());
    }

    @Override
    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UnauthorizedException("No authenticated user found in current session");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UnauthorizedException("Authenticated user not found: " + email));
    }
}
