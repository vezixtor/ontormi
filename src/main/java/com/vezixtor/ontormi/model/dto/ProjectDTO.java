package com.vezixtor.ontormi.model.dto;

import com.vezixtor.ontormi.model.entity.Project;
import com.vezixtor.ontormi.model.entity.Task;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private UserDTO user;
    private List<Task> tasks = new ArrayList<>();

    public ProjectDTO() {}

    public ProjectDTO(Project project) {
        id = project.getId();
        name = project.getName();
        description = project.getDescription();
        user = UserDTO.of(project.getUser());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public UserDTO getUser() {
        return user;
    }

    public static ProjectDTO of(Project project) {
        return new ProjectDTO(project);
    }
}
