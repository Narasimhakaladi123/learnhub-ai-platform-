package com.career.guidance.config;

import com.career.guidance.entity.*;
import com.career.guidance.entity.enums.*;
import com.career.guidance.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final StudentProfileRepository profileRepository;
    private final CareerPathRepository careerPathRepository;
    private final SkillRepository skillRepository;
    private final CareerSkillRepository careerSkillRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final AssessmentRepository assessmentRepository;
    private final QuestionRepository questionRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UserRepository userRepository,
            StudentProfileRepository profileRepository,
            CareerPathRepository careerPathRepository,
            SkillRepository skillRepository,
            CareerSkillRepository careerSkillRepository,
            StudentSkillRepository studentSkillRepository,
            AssessmentRepository assessmentRepository,
            QuestionRepository questionRepository,
            ProjectRepository projectRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.careerPathRepository = careerPathRepository;
        this.skillRepository = skillRepository;
        this.careerSkillRepository = careerSkillRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.assessmentRepository = assessmentRepository;
        this.questionRepository = questionRepository;
        this.projectRepository = projectRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0 && careerPathRepository.count() > 0) {
            log.info("Database already seeded with LearnHub initial data. Skipping initialization.");
            return;
        }

        log.info("Starting LearnHub database initialization and seed data generation...");

        // 1. Seed Skills
        Skill java = getOrCreateSkill("Java", SkillCategory.BACKEND, "Modern object-oriented programming with Java 17+, multithreading, and JVM internals");
        Skill springBoot = getOrCreateSkill("Spring Boot", SkillCategory.BACKEND, "Microservices, Spring Data JPA, Spring Security, RESTful Web Services");
        Skill react = getOrCreateSkill("React.js", SkillCategory.FRONTEND, "Modern component architecture, Hooks, State management, SPA engineering");
        Skill typeScript = getOrCreateSkill("TypeScript", SkillCategory.FRONTEND, "Strongly-typed JavaScript for scalable frontend development");
        Skill mysql = getOrCreateSkill("MySQL", SkillCategory.DATABASE, "Relational schema design, ACID transactions, complex queries, indexing");
        Skill docker = getOrCreateSkill("Docker", SkillCategory.DEVOPS, "Containerization, multi-stage Dockerfiles, Docker Compose networking");
        Skill kubernetes = getOrCreateSkill("Kubernetes", SkillCategory.CLOUD, "Cluster orchestration, deployments, pods, services, ingress");
        Skill aws = getOrCreateSkill("AWS", SkillCategory.CLOUD, "Cloud architecture with EC2, S3, RDS, Lambda, VPC");
        Skill python = getOrCreateSkill("Python", SkillCategory.AI_ML, "Data structures, scripting, NumPy, Pandas, scikit-learn, PyTorch");
        Skill git = getOrCreateSkill("Git & GitHub", SkillCategory.GENERAL, "Version control, branching strategies, PR workflows, code review");
        Skill restApi = getOrCreateSkill("RESTful APIs", SkillCategory.BACKEND, "API contracts, HTTP semantics, JSON payloads, Swagger OpenAPI");
        Skill systemDesign = getOrCreateSkill("System Design", SkillCategory.BACKEND, "High-availability architectures, caching, load balancing, sharding");

        // 2. Seed Career Paths
        CareerPath fullStackJava = getOrCreateCareerPath(
            "Full Stack Java Developer",
            "full-stack-java-developer",
            "Architect and construct modern end-to-end web applications and cloud microservices utilizing Spring Boot, React, and MySQL.",
            "SOFTWARE_DEVELOPMENT",
            "$95,000 - $145,000",
            "High (22% projected 5-year growth)"
        );

        CareerPath cloudDevOps = getOrCreateCareerPath(
            "Cloud & DevOps Platform Engineer",
            "cloud-devops-engineer",
            "Automate robust continuous integration, deployment pipelines, and cloud container orchestration on AWS/Azure.",
            "CLOUD_COMPUTING",
            "$110,000 - $160,000",
            "Very High (28% projected 5-year growth)"
        );

        CareerPath aiEngineer = getOrCreateCareerPath(
            "AI & GenAI Solutions Engineer",
            "ai-ml-engineer",
            "Deploy state-of-the-art LLM pipelines, RAG systems, and production machine learning models.",
            "DATA_AI",
            "$125,000 - $180,000",
            "Explosive (35% projected 5-year growth)"
        );

        // 3. Link Career Skills
        linkCareerSkill(fullStackJava, java, RequirementLevel.REQUIRED, SkillStage.ENTRY, ProficiencyLevel.ADVANCED, 10);
        linkCareerSkill(fullStackJava, springBoot, RequirementLevel.REQUIRED, SkillStage.INTERMEDIATE, ProficiencyLevel.ADVANCED, 10);
        linkCareerSkill(fullStackJava, react, RequirementLevel.REQUIRED, SkillStage.INTERMEDIATE, ProficiencyLevel.INTERMEDIATE, 9);
        linkCareerSkill(fullStackJava, mysql, RequirementLevel.REQUIRED, SkillStage.ENTRY, ProficiencyLevel.INTERMEDIATE, 8);
        linkCareerSkill(fullStackJava, restApi, RequirementLevel.REQUIRED, SkillStage.ENTRY, ProficiencyLevel.ADVANCED, 9);
        linkCareerSkill(fullStackJava, docker, RequirementLevel.PREFERRED, SkillStage.ADVANCED, ProficiencyLevel.BEGINNER, 6);
        linkCareerSkill(fullStackJava, git, RequirementLevel.REQUIRED, SkillStage.ENTRY, ProficiencyLevel.INTERMEDIATE, 7);

        linkCareerSkill(cloudDevOps, docker, RequirementLevel.REQUIRED, SkillStage.ENTRY, ProficiencyLevel.ADVANCED, 10);
        linkCareerSkill(cloudDevOps, kubernetes, RequirementLevel.REQUIRED, SkillStage.INTERMEDIATE, ProficiencyLevel.ADVANCED, 10);
        linkCareerSkill(cloudDevOps, aws, RequirementLevel.REQUIRED, SkillStage.INTERMEDIATE, ProficiencyLevel.ADVANCED, 9);
        linkCareerSkill(cloudDevOps, git, RequirementLevel.REQUIRED, SkillStage.ENTRY, ProficiencyLevel.ADVANCED, 8);

        linkCareerSkill(aiEngineer, python, RequirementLevel.REQUIRED, SkillStage.ENTRY, ProficiencyLevel.ADVANCED, 10);
        linkCareerSkill(aiEngineer, restApi, RequirementLevel.REQUIRED, SkillStage.INTERMEDIATE, ProficiencyLevel.INTERMEDIATE, 8);
        linkCareerSkill(aiEngineer, docker, RequirementLevel.PREFERRED, SkillStage.INTERMEDIATE, ProficiencyLevel.INTERMEDIATE, 7);

        // 4. Seed Users
        if (!userRepository.existsByEmail("admin@learnhub.com")) {
            User admin = new User("admin@learnhub.com", passwordEncoder.encode("Admin@123"), "Administrator", RoleType.ROLE_ADMIN);
            userRepository.save(admin);
        }

        if (!userRepository.existsByEmail("counselor@learnhub.com")) {
            User counselor = new User("counselor@learnhub.com", passwordEncoder.encode("Counselor@123"), "Sarah Mitchell (Lead Career Advisor)", RoleType.ROLE_COUNSELOR);
            userRepository.save(counselor);
        }

        if (!userRepository.existsByEmail("student@learnhub.com")) {
            User student = new User("student@learnhub.com", passwordEncoder.encode("Student@123"), "Alex Mercer", RoleType.ROLE_STUDENT);
            userRepository.save(student);

            StudentProfile profile = new StudentProfile(student);
            profile.setDegree("B.Tech Computer Science");
            profile.setBranch("Computer Science & Engineering");
            profile.setCollege("Stanford Institute of Technology");
            profile.setGraduationYear(2025);
            profile.setCgpa(new BigDecimal("8.75"));
            profile.setTargetCareer("Full Stack Java Developer");
            profile.setReadinessPercentage(78);
            profile.setBio("Passionate aspiring Software Engineer specializing in backend Java systems, microservices architectures, and modern React interfaces.");
            profile.setLocation("San Francisco, CA");
            profileRepository.save(profile);

            // Seed user skills
            studentSkillRepository.save(new StudentSkill(profile, java, ProficiencyLevel.INTERMEDIATE, 14));
            studentSkillRepository.save(new StudentSkill(profile, springBoot, ProficiencyLevel.INTERMEDIATE, 10));
            studentSkillRepository.save(new StudentSkill(profile, mysql, ProficiencyLevel.INTERMEDIATE, 12));
            studentSkillRepository.save(new StudentSkill(profile, git, ProficiencyLevel.ADVANCED, 24));
        }

        // 5. Seed Quizzes & Technical Assessments
        if (assessmentRepository.count() == 0) {
            Assessment javaQuiz = new Assessment(
                "Java & Spring Boot Core Proficiency",
                "java-spring-boot-core",
                "BACKEND",
                DifficultyLevel.INTERMEDIATE,
                15,
                70,
                "Evaluate knowledge of Java memory management, Spring Bean scopes, JPA caching, and REST best practices."
            );
            assessmentRepository.save(javaQuiz);

            questionRepository.save(new Question(
                javaQuiz,
                "Which HTTP status code is most appropriate when a resource is successfully created via a POST request?",
                QuestionType.MCQ,
                "[\"200 OK\", \"201 Created\", \"204 No Content\", \"202 Accepted\"]",
                "[\"201 Created\"]",
                "HTTP 201 indicates that the request succeeded and resulted in the creation of a new resource.",
                10
            ));

            questionRepository.save(new Question(
                javaQuiz,
                "In Spring Framework, what is the default scope of a Spring Bean?",
                QuestionType.MCQ,
                "[\"Prototype\", \"Request\", \"Singleton\", \"Session\"]",
                "[\"Singleton\"]",
                "By default, Spring manages beans as Singletons per Spring ApplicationContext container.",
                10
            ));

            questionRepository.save(new Question(
                javaQuiz,
                "What is the main benefit of connection pooling in database applications?",
                QuestionType.MCQ,
                "[\"Eliminates database queries\", \"Reuses existing physical connections to avoid expensive connection creation overhead\", \"Encrypts all database tables\", \"Bypasses SQL syntax checks\"]",
                "[\"Reuses existing physical connections to avoid expensive connection creation overhead\"]",
                "HikariCP connection pool reuses database connections to significantly lower latency and database load.",
                10
            ));

            Assessment reactQuiz = new Assessment(
                "Modern React & Frontend Engineering",
                "modern-react-frontend",
                "FRONTEND",
                DifficultyLevel.INTERMEDIATE,
                15,
                70,
                "Test core concepts around useEffect lifecycles, React DOM reconciliation, and unidirectional data flow."
            );
            assessmentRepository.save(reactQuiz);

            questionRepository.save(new Question(
                reactQuiz,
                "What does an empty dependency array `[]` in `useEffect` indicate?",
                QuestionType.MCQ,
                "[\"The effect runs on every re-render\", \"The effect runs only once after the initial render\", \"The effect is never executed\", \"The effect triggers an infinite loop\"]",
                "[\"The effect runs only once after the initial render\"]",
                "An empty dependency array tells React the effect does not depend on any state or props, so it only runs on mount.",
                10
            ));
        }

        // 6. Seed Recommended Projects
        if (projectRepository.count() == 0) {
            projectRepository.save(new Project(
                "Distributed E-Commerce Microservices Platform",
                "Build an event-driven architecture featuring inventory, product catalog, payment processing, and JWT authentication.",
                DifficultyLevel.ADVANCED,
                "Java 17, Spring Boot, MySQL, Docker, Redis",
                "Microservice decomposition, ACID transactions, JWT security, Docker containerization",
                "Authentication service, Order lifecycle state machine, Redis caching layer",
                4,
                "full-stack-java-developer"
            ));

            projectRepository.save(new Project(
                "AI Resume Matcher & Real-Time Interview Bot",
                "Full-stack web application that parses candidate PDF resumes, evaluates ATS compatibility, and conducts mock interviews.",
                DifficultyLevel.INTERMEDIATE,
                "Spring Boot, React.js, Tailwind CSS, OpenAI / RAG, MySQL",
                "RAG pipeline, PDF parsing, REST API design, Responsive UI",
                "Live scoring algorithm, Markdown chat interface, Skill gap radar charts",
                3,
                "full-stack-java-developer"
            ));

            projectRepository.save(new Project(
                "Automated Cloud CI/CD & Kubernetes Ingress Pipeline",
                "Deploy a multi-tier microservice cluster on AWS EKS using Helm charts, GitHub Actions, and Prometheus monitoring.",
                DifficultyLevel.ADVANCED,
                "Docker, Kubernetes, AWS EKS, GitHub Actions, Terraform",
                "Container orchestration, GitOps automation, Cloud monitoring, Zero-downtime rolling updates",
                "Automated staging/prod pipelines, SSL cert manager, Grafana observability dashboards",
                4,
                "cloud-devops-engineer"
            ));
        }

        log.info("LearnHub database successfully seeded with canonical careers, skills, assessments, and sample accounts.");
    }

    private Skill getOrCreateSkill(String name, SkillCategory category, String description) {
        return skillRepository.findByNameIgnoreCase(name)
            .orElseGet(() -> skillRepository.save(new Skill(name, category, description)));
    }

    private CareerPath getOrCreateCareerPath(String name, String slug, String description, String category, String salary, String outlook) {
        return careerPathRepository.findBySlug(slug)
            .orElseGet(() -> careerPathRepository.save(new CareerPath(name, slug, description, category, salary, outlook)));
    }

    private void linkCareerSkill(CareerPath career, Skill skill, RequirementLevel req, SkillStage stage, ProficiencyLevel minProf, int weight) {
        if (careerSkillRepository.findByCareerPathAndSkill(career, skill).isEmpty()) {
            careerSkillRepository.save(new CareerSkill(career, skill, req, stage, minProf, weight));
        }
    }
}
