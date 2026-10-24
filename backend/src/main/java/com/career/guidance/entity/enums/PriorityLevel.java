package com.career.guidance.entity.enums;

public enum PriorityLevel {
    PRIORITY_1("High Priority"),
    PRIORITY_2("Medium Priority"),
    PRIORITY_3("Low Priority");

    private final String label;

    PriorityLevel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
