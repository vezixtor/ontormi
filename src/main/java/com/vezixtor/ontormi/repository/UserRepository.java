package com.vezixtor.ontormi.repository;

import com.vezixtor.ontormi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByName(String name);

}
