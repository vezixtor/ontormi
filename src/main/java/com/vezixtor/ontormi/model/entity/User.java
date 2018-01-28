package com.vezixtor.ontormi.model.entity;

import com.vezixtor.ontormi.model.dto.UserDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "ontormi_user")
public class User extends GenericEntity {

    private String fullname;
    @Column(unique = true)
    private String email;
    private String password;
    private boolean enabled;

    public User() {}

    public User(UserDTO userDTO) {
        this.fullname = userDTO.getFullname();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
    }

    @PrePersist
    private void prePersistUser() {
        this.enabled = true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void enable() {
        enabled = true;
    }

    public void disable(){
        enabled = false;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public User update(UserDTO userDTO) {
        this.fullname = userDTO.getFullname();
        this.email = userDTO.getEmail();
        return this;
    }

    public User delete() {
        email = email.concat("_datamine");
        enabled = false;
        return this;
    }
}
