package com.example.exercise.infrastructure.db.springdata.services;

import com.example.exercise.infrastructure.db.springdata.model.Account;
import com.example.exercise.infrastructure.db.springdata.model.Client;

import java.util.List;

public interface IAccountService {

    void deleteByClient(Client client);

    List<Account> getAllAccounts();

    List<Account> findAccountsByClient(Client client);

    void save(Account account);

}
