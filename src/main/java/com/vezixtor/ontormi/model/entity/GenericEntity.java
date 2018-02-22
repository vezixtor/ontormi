package com.vezixtor.ontormi.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class GenericEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    @PrePersist
    private void onCreateEntity() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    private void onUpdateEntity() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
