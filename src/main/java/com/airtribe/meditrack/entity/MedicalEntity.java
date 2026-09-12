package com.airtribe.meditrack.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class MedicalEntity {
    private final int id;
    private final LocalDateTime createdAt;

    protected MedicalEntity(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Entity id must be positive.");
        }
        this.id = id;
        this.createdAt = LocalDateTime.now();
    }

    public final int getId() {
        return id;
    }

    public final LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public abstract String getDisplayName();
}
