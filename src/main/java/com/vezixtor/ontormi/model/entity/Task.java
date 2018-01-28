package com.vezixtor.ontormi.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Task extends GenericEntity {
    private String name;
    private String description;
    private boolean done;
    @ManyToOne
    private TaskType type;
    @OneToMany
    @JoinColumn(name = "task_id")
    private List<TimeSpent> timeSpent = new ArrayList<>();
    @ManyToMany
    private List<Task> subtask = new ArrayList<>();
    @ManyToMany
    private List<Skill> skill = new ArrayList<>();

    public Task() {}

}
