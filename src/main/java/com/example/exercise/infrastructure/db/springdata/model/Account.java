package com.example.exercise.infrastructure.db.springdata.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private Long money;
    private LocalDateTime creationTime;

    @ManyToOne(optional = false)
    private Client client;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private Set<BankTransaction> bankTransactions;

    public Account() {
    }

    public Account(Long money, LocalDateTime creationTime, Client client) {
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<BankTransaction> getBankTransactions() {
        return bankTransactions;
    }

    public void setBankTransactions(Set<BankTransaction> bankTransactions){
        this.bankTransactions = bankTransactions;
    }

    public void addBankTransaction(BankTransaction bankTransaction) {
        if (this.bankTransactions == null) {
            this.bankTransactions = new HashSet();
        }
        this.bankTransactions.add(bankTransaction);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", money=" + money +
                ", creationTime=" + creationTime +
                ", client=" + client +
                '}';
    }

}
