package com.example.exercise.infrastructure.db.springdata.dtos;

import java.time.LocalDateTime;

public class AccountDto {

    private Long id;

    private Long money;

    private LocalDateTime creationTime;

    private ClientDto client;

    public AccountDto() {
    }

    public AccountDto(Long money, LocalDateTime creationTime, ClientDto client) {
        this.money = money;
        this.creationTime = creationTime;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public ClientDto getClient() {
        return client;
    }

    public void setClient(ClientDto client) {
        this.client = client;
    }

}
