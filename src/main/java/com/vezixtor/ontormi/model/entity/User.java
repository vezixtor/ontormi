package com.vezixtor.ontormi.model.entity;

import com.vezixtor.ontormi.model.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.util.Collection;

@Entity
@Table(name = "ontormi_user")
public class User extends GenericEntity implements UserDetails {

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

    public void setPassword(String password) {
        this.password = password;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
}
