package com.vezixtor.ontormi.model.dto;

import com.vezixtor.ontormi.model.entity.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectsResponse extends RestPaging<ProjectsResponse> {
    private List<?> projects = new ArrayList<>();

    public ProjectsResponse(List<Project> projects) {
        if (projects.size() > 0) {
            this.projects = projects
                    .stream().map(project -> {
                        ProjectDTO dto = ProjectDTO.of(project);
                        dto.setUser(null);
                        return dto;
                    })
                    .collect(Collectors.toList());
        }
    }

    @Override
    protected List<?> getSubclassData() {
        return projects;
    }

    @Override
    protected String getPrimaryKeyDeclaredField() {
        return "id";
    }

    public static ProjectsResponse of(List<Project> data) {
        return new ProjectsResponse(data);
    }
}
