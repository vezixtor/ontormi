package com.vezixtor.ontormi.model.entity;

import com.vezixtor.ontormi.model.dto.ProjectDTO;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Project extends GenericEntity {
    private String name;
    private String description;
    @OneToOne
    private User user;

    @ManyToMany
    private List<Task> tasks = new ArrayList<>();

    public Project() {}

    public Project(ProjectDTO projectDTO, User user) {
        this.user = user;
        setWithDTO(projectDTO);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public User getUser() {
        return user;
    }

    public Project update(ProjectDTO projectDTO) {
        setWithDTO(projectDTO);
        return this;
    }

    private void setWithDTO(ProjectDTO projectDTO) {
        name = projectDTO.getName();
        description = projectDTO.getDescription();
    }

    public ProjectDTO toDTO() {
        return new ProjectDTO(this);
    }
}
