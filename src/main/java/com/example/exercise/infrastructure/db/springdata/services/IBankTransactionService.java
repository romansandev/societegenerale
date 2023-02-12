package com.example.exercise.infrastructure.db.springdata.services;

import com.example.exercise.infrastructure.db.springdata.model.Account;
import com.example.exercise.infrastructure.db.springdata.model.BankTransaction;
import com.example.exercise.domain.exceptions.BrokeAccountException;
import com.example.exercise.infrastructure.db.springdata.model.Client;

import java.util.List;

public interface IBankTransactionService {

    void deleteByAccount(Account account);

    void insert(BankTransaction bankTransaction) throws BrokeAccountException;

    List<BankTransaction> findBankTransactionsByAccount(Account account);

    List<BankTransaction> findBankTransactionsByClient(Client client);

}
