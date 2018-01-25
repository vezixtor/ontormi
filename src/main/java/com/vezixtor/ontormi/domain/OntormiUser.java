package com.vezixtor.ontormi.domain;

import javax.persistence.*;

@Entity
public class OntormiUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String firstname;

    public OntormiUser() {}

    public OntormiUser(String name, String firstname) {
        this.name = name;
        this.firstname = firstname;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

}
