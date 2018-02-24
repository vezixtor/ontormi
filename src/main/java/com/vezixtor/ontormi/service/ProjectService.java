package com.vezixtor.ontormi.service;

import com.vezixtor.ontormi.model.dto.ProjectDTO;
import com.vezixtor.ontormi.model.entity.Project;
import com.vezixtor.ontormi.model.entity.User;
import com.vezixtor.ontormi.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService extends BaseService {
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    protected String getNotFoundMessage() {
        return "Project not found";
    }

    public ProjectDTO postCreate(ProjectDTO projectDTO) {
        Project project = projectRepository.save(new Project(projectDTO, getUserFromToken()));
        return project.toDTO();
    }

    public ProjectDTO getOne(Long id) {
        return getOrThrow(projectRepository.findOne(id)).toDTO();
    }

    public ProjectDTO putUpdate(ProjectDTO projectDTO) {
        Project project = getProjectFromUser(projectDTO.getId());
        return projectRepository.save(project.update(projectDTO)).toDTO();
    }

    public void delete(Long id) {
        projectRepository.delete(getProjectFromUser(id));
    }

    private Project getProjectFromUser(Long idProject) {
        return getProjectFromUser(idProject, getUserFromToken());
    }

    private Project getProjectFromUser(Long idProject, User user) {
        return getOrThrow(projectRepository.findByIdAndUser_Id(idProject, user.getId()));
    }
}
