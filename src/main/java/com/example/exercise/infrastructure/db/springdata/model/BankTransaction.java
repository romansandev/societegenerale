package com.example.exercise.infrastructure.db.springdata.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class BankTransaction {

    @Id
    @GeneratedValue
    private Long id;

    private Long money;
    private LocalDateTime transactionTime;
    private OperationType operationType;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public BankTransaction() {
    }

    public BankTransaction(Long money, LocalDateTime transactionTime, OperationType operationType, Account account) {
        this.money = money;
        this.transactionTime = transactionTime;
        this.operationType = operationType;
        this.account = account;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "BankTransaction{" +
                "id=" + id +
                ", money=" + money +
                ", transactionTime=" + transactionTime +
                ", operationType=" + operationType +
                ", account=" + account +
                '}';
    }
}
