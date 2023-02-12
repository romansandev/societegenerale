package com.example.exercise.infrastructure.db.springdata.dtos;

import com.example.exercise.infrastructure.db.springdata.model.OperationType;

import java.time.LocalDateTime;

public class BankTransactionDto {
    private Long id;

    private Long money;

    private LocalDateTime transactionTime;

    private OperationType operationType;

    public BankTransactionDto(Long money, LocalDateTime transactionTime, OperationType operationType) {
        this.money = money;
        this.transactionTime = transactionTime;
        this.operationType = operationType;
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

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
}
