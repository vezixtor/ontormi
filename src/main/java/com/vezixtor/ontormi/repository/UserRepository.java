package com.vezixtor.ontormi.repository;

import com.vezixtor.ontormi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

//    List<User> findByName(String name);

}
