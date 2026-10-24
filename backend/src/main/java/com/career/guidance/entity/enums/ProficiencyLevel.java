package com.career.guidance.entity.enums;

public enum ProficiencyLevel {
    BEGINNER(1),
    INTERMEDIATE(2),
    ADVANCED(3),
    EXPERT(4);

    private final int levelWeight;

    ProficiencyLevel(int levelWeight) {
        this.levelWeight = levelWeight;
    }

    public int getLevelWeight() {
        return levelWeight;
    }
}
