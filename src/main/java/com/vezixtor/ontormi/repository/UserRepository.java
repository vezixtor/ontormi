package com.vezixtor.ontormi.repository;

import com.vezixtor.ontormi.domain.OntormiUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<OntormiUser, Long> {

    List<OntormiUser> findByName(String name);

}
