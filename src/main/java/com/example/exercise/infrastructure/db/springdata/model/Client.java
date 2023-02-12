package com.example.exercise.infrastructure.db.springdata.model;

import jakarta.persistence.*;

@Entity
public class Client {

    @Id
    @GeneratedValue
    private Long id;
    private String clientName;
    private String password;

    public Client() {
    }

    public Client(String clientName, String password) {
        this.clientName = clientName;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
