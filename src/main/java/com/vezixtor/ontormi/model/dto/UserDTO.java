package com.vezixtor.ontormi.model.dto;

import com.vezixtor.ontormi.model.entity.User;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class UserDTO {
    private Long id;
    @NotBlank
    private String fullname;
    @Email
    private String email;
    private String password;

    public UserDTO() {}

    public UserDTO(User user) {
        id = user.getId();
        fullname = user.getFullname();
        email = user.getEmail();
    }

    public Long getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static UserDTO of(User user) {
        return new UserDTO(user);
    }
}
