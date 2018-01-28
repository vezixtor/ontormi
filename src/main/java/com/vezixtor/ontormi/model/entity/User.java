package com.vezixtor.ontormi.model.entity;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "ontormi_user")
public class User extends GenericEntity {

    private String fullname;
    private String email;
    private String password;
    private boolean enabled;

    public User() {}

    @PrePersist
    protected void prePersistEnabled() {
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

}
