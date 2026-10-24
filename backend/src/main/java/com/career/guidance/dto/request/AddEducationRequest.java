package com.career.guidance.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AddEducationRequest {

    @NotBlank(message = "Degree is required")
    private String degree;

    @NotBlank(message = "Branch is required")
    private String branch;

    @NotBlank(message = "College is required")
    private String college;

    @NotNull(message = "Graduation year is required")
    private Integer graduationYear;

    @DecimalMin(value = "0.00", message = "CGPA must be at least 0.0")
    @DecimalMax(value = "10.00", message = "CGPA cannot exceed 10.0")
    private BigDecimal cgpa;

    private Integer startYear;
    private Integer endYear;

    public AddEducationRequest() {
    }

    // Getters and Setters
    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public BigDecimal getCgpa() {
        return cgpa;
    }

    public void setCgpa(BigDecimal cgpa) {
        this.cgpa = cgpa;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }
}
