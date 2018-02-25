package com.vezixtor.ontormi.repository;

import com.vezixtor.ontormi.model.entity.Project;
import com.vezixtor.ontormi.model.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProjectRepository extends PagingAndSortingRepository<Project, Long> {

    Project findByIdAndUser_Id(Long id, Long idUser);

    List<Project> findAllByUser(User userFromToken, Pageable pageable);

    List<Project> findByUser_IdAndIdLessThanOrderByIdDesc(Long id, Long previous, Pageable pageable);

    List<Project> findByUser_IdAndIdGreaterThan(Long id, Long next, Pageable pageable);
}
