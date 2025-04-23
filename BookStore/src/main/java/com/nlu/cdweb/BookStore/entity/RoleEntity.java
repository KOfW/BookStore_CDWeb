package com.nlu.cdweb.BookStore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

//    @ManyToMany(mappedBy = "roles")
//    private List<UserEntity> users = new ArrayList<>();

    public RoleEntity(String name) {
        this.name = name;
    }

    public RoleEntity() {
    }

//    public RoleEntity(String name, List<UserEntity> users) {
//        this.name = name;
//        this.users = users;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public List<UserEntity> getUsers() {
//        return users;
//    }
//
//    public void setUsers(List<UserEntity> users) {
//        this.users = users;
//    }
}
