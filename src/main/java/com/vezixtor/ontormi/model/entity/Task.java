package com.vezixtor.ontormi.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Task extends GenericEntity {
    private String name;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean done;
    @ManyToOne
    private TaskType type;
    @OneToMany
    private List<Reopen> reopen = new ArrayList<>();
    @ManyToMany
    private List<Task> subtask = new ArrayList<>();
    @ManyToMany
    private List<Skill> skill = new ArrayList<>();

    public Task() {}

}
