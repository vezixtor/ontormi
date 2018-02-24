package com.vezixtor.ontormi.repository;

import com.vezixtor.ontormi.model.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findByIdAndUser_Id(Long id, Long idUser);
}
