package com.career.guidance.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "learning_resources")
public class LearningResource {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 50)
    private String type; // COURSE, TUTORIAL, DOCUMENTATION, BOOK, VIDEO

    @Column(nullable = false, length = 500)
    private String url;

    @Column(length = 100)
    private String provider;

    @Column(name = "is_free", nullable = false)
    private boolean free = true;

    public LearningResource() {
    }

    public LearningResource(Skill skill, String title, String type, String url, String provider, boolean free) {
        this.skill = skill;
        this.title = title;
        this.type = type;
        this.url = url;
        this.provider = provider;
        this.free = free;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}
