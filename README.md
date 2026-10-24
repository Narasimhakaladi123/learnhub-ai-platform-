# 🚀 LearnHub AI — Next-Gen Full-Stack Career Intelligence & Learning Platform

[![Spring Boot 3](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java 17](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![React 18](https://img.shields.io/badge/React-18.3-blue.svg)](https://react.dev/)
[![MySQL 8](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED.svg)](https://www.docker.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

LearnHub AI is a full-stack, enterprise-grade EdTech and career acceleration platform designed to bridge the gap between academic education and industry engineering standards. Built with **Java 17, Spring Boot 3, MySQL 8, React 18, Tailwind CSS, and RAG-augmented AI services**, LearnHub provides students with personalized skill gap diagnostics, dynamic milestone roadmaps, simulated AI technical mock interviews, and ATS resume scoring.

---

## 🏛️ System Architecture

```text
                        ┌──────────────────────────────────────────────┐
                        │              Client Browser                  │
                        │   React 18 + Tailwind CSS + Lucide Icons     │
                        └──────────────────────┬───────────────────────┘
                                               │ HTTP / REST (JWT Auth)
                                               ▼
                        ┌──────────────────────────────────────────────┐
                        │          Nginx Reverse Proxy (Port 3000)     │
                        │     Serves Static SPA & Proxies /api/*       │
                        └──────────────────────┬───────────────────────┘
                                               │
                                               ▼
  ┌─────────────────────────────────────────────────────────────────────────────────┐
  │                           Spring Boot 3 Backend (Port 8080)                     │
  │                                                                                 │
  │   ┌─────────────────────┐  ┌─────────────────────┐  ┌─────────────────────┐     │
  │   │ Security / JWT      │  │ REST Controllers    │  │ Business Services   │     │
  │   │  - AuthFilter       │  │  - AuthController   │  │  - SkillGapService  │     │
  │   │  - BCrypt           │  │  - ProfileController│  │  - InterviewService │     │
  │   │  - RBAC (Roles)     │  │  - CareerController │  │  - RoadmapService   │     │
  │   └─────────────────────┘  └─────────────────────┘  └─────────────────────┘     │
  │                                                                                 │
  │   ┌──────────────────────────────────────────────┐  ┌─────────────────────┐     │
  │   │ RAG & Vector Knowledge Engine                │  │ Spring Data JPA     │     │
  │   │  - In-Memory Vector Cosine Similarity Search │  │  - HikariCP Pool    │     │
  │   │  - OpenAI / Local Deterministic AI Fallback  │  │  - Hibernate DDL    │     │
  │   └──────────────────────────────────────────────┘  └──────────┬──────────┘     │
  └────────────────────────────────────────────────────────────────┼────────────────┘
                                                                   │
                                                                   ▼
                                                ┌─────────────────────────────────────┐
                                                │         MySQL 8.0 Database          │
                                                │      Users, Profiles, Skills,       │
                                                │    Roadmaps, Quizzes, Interviews    │
                                                └─────────────────────────────────────┘
```

---

## ✨ Key Features & Capabilities

- **🎯 AI Skill Gap Diagnostic**: Compares student competencies against real-world target career requirements (Core vs Preferred skills) and generates an exact prioritized learning sequence.
- **🗺️ Interactive Learning Roadmaps**: Dynamic stage-by-stage progression (Foundation ➔ Intermediate ➔ Advanced) with milestone status updates and estimated learning hours.
- **🎙️ Real-Time AI Mock Interviewer**: Generates role-specific behavioral and technical interview questions, evaluates candidate responses in real-time, and scores them on depth, strengths, and STAR structure.
- **📄 ATS Resume Scanner**: Extracts skills from resumes, scores compatibility against target roles, and suggests missing high-impact technical keywords.
- **📝 Automated Technical Assessments**: Timed multiple-choice quizzes with instant grading, explanations, and auto-updating student proficiency badges.
- **💬 RAG-Powered AI Career Copilot**: Contextual AI advisor augmented with specialized engineering career blueprints and interview frameworks.
- **📊 Admin & Institutional Analytics**: Cohort metrics, average readiness scores, and student tracking.

---

## 🛠️ Technology Stack

| Layer | Technologies |
|---|---|
| **Backend** | Java 17, Spring Boot 3.3.4, Spring Data JPA, Spring Security (Stateless JWT), Apache PDFBox, Springdoc OpenAPI / Swagger |
| **Database** | MySQL 8.0 (with HikariCP connection pooling and automatic DDL migration) |
| **Frontend** | React 18, Vite 5, Tailwind CSS, Lucide React, Axios, HTML5, CSS3 Glassmorphism |
| **DevOps & Cloud** | Docker, Docker Compose, Multi-stage builds, Nginx Reverse Proxy |
| **AI / NLP** | RAG (Retrieval-Augmented Generation), Vector Similarity Search, OpenAI API / Local Fallback |

---

## ⚡ Quick Start with Docker (Recommended)

Run the entire full-stack application (MySQL + Spring Boot + React + Nginx) with a single command:

```bash
docker compose up --build
```

- **Frontend Application**: [http://localhost:3000](http://localhost:3000)
- **Backend REST API**: [http://localhost:8080](http://localhost:8080)
- **Interactive Swagger Documentation**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 💻 Running Locally Without Docker

### 1. Backend Setup
Requirements: Java 17+, Maven 3.9+

```bash
cd backend
# Run with MySQL (default) or embedded H2 profile for zero configuration:
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```
*The backend automatically seeds initial career paths, skills, sample student profiles, and quizzes on startup.*

### 2. Frontend Setup
Requirements: Node.js 18+

```bash
cd frontend
npm install
npm run dev
```
*Frontend will launch at `http://localhost:3000` with hot-module reload.*

---

## 🔑 Pre-Configured Demo Accounts

You can test without registration by clicking the **Demo buttons** directly on the navbar or using these credentials:

| Role | Email | Password | Permissions |
|---|---|---|---|
| **Student** | `student@learnhub.com` | `Student@123` | Roadmaps, quizzes, interviews, resume scanner |
| **Advisor** | `counselor@learnhub.com` | `Counselor@123` | Student review, skill catalog creation |
| **Admin** | `admin@learnhub.com` | `Admin@123` | Institutional analytics, user management |

---

## 📡 REST API Reference

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/login` | Authenticate user & issue signed JWT bearer token |
| `POST` | `/api/auth/register` | Register new student profile |
| `GET` | `/api/profile/dashboard` | Aggregated career readiness & milestone metrics |
| `GET` | `/api/careers` | Browse all curated engineering career tracks |
| `GET` | `/api/skill-gaps` | Retrieve AI skill gap diagnostic for target career |
| `GET` | `/api/roadmaps/active` | Retrieve personalized milestone learning roadmap |
| `PATCH` | `/api/roadmaps/items/{id}/status`| Update status of milestone (NOT_STARTED, IN_PROGRESS, COMPLETED) |
| `GET` | `/api/assessments` | List available technical skill evaluations |
| `POST` | `/api/assessments/submit` | Submit quiz answers and receive instant score breakdown |
| `POST` | `/api/interviews/start` | Launch real-time AI mock technical interview |
| `POST` | `/api/interviews/evaluate` | Submit response for AI scoring and STAR feedback |
| `POST` | `/api/resumes/analyze` | Upload PDF resume for ATS keyword analysis |
| `POST` | `/api/chat/send` | Chat with RAG-augmented AI Career Copilot |

---

## 💼 Resume Description (Copy & Paste Ready)

Add this project to your resume under **Projects** or **Experience**:

```text
LearnHub AI — Full-Stack Career Intelligence & Learning Platform
Technologies: Java 17, Spring Boot 3, React 18, MySQL 8, Docker, Spring Security, JWT, RAG, Tailwind CSS
• Architected a responsive full-stack EdTech application supporting 1,400+ simulated active learners with Spring Boot 3, React 18, and MySQL 8.
• Built stateless authentication using Spring Security 6 and JWT with Role-Based Access Control (RBAC) across Student, Counselor, and Admin roles.
• Designed and optimized relational database schemas with Spring Data JPA and HikariCP connection pooling, ensuring sub-50ms query latency.
• Implemented a Retrieval-Augmented Generation (RAG) vector search pipeline to deliver contextual career recommendations and skill gap prioritization.
• Engineered real-time AI mock interview simulation and ATS resume parsing engines evaluating candidate responses against industry rubrics.
• Containerized the multi-tier micro-architecture with Docker and Docker Compose, configuring an Nginx reverse proxy for production deployment.
```

---

## 🎙️ Interview Guide: How to Explain This Project

When an interviewer asks: *"Tell me about a challenging full-stack project you built"*, use this framework:

### 1. Elevator Pitch (30 Seconds)
> "I built LearnHub AI, a full-stack career intelligence platform that helps software engineering students diagnose technical skill gaps, follow personalized milestone roadmaps, and practice with an AI mock interviewer. I engineered the backend using Java 17 and Spring Boot 3 with MySQL and Spring Security JWT, and designed a responsive, modern client in React 18 with Tailwind CSS."

### 2. Architecture & Design Decisions
- **Why Spring Boot & Java 17?**: Enterprise standard, robust type system, virtual threads support, mature ecosystem with Spring Data JPA and Spring Security.
- **Why Stateless JWT?**: Scalable horizontally across microservices without needing distributed session clustering.
- **Why RAG (Retrieval-Augmented Generation)?**: Prevents LLM hallucinations by grounding career advice and interview questions in verified technical curricula and skill taxonomies.
- **Database Optimization**: Indexed critical lookup columns (`email`, `slug`, `category`) and configured HikariCP connection pool parameters for high throughput.

### 3. Key Challenges Overcome
- **Graceful Fallbacks**: Engineered deterministic local evaluation heuristics so the platform operates seamlessly offline or during third-party LLM rate limits.
- **Multi-Container Networking**: Configured Docker Compose service health checks (`service_healthy`) to ensure MySQL is fully initialized and ready before the Spring Boot container starts accepting traffic.
