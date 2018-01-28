package com.vezixtor.ontormi.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    @PrePersist
    private void onCreateLocalDateTime() {
        this.createdAt = LocalDateTime.now();
    }

    public Skill() {}

}
