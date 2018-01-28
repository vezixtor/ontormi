package com.vezixtor.ontormi.model.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Project extends GenericEntity {
    private String name;
    private String description;
    @OneToOne
    private User user;
    @ManyToMany
    private List<Project> tasks = new ArrayList<>();

    public Project() {}

}
