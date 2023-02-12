package com.example.exercise.infrastructure.db.springdata.services;

import com.example.exercise.infrastructure.db.springdata.model.Account;
import com.example.exercise.infrastructure.db.springdata.model.Client;
import com.example.exercise.infrastructure.db.springdata.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService implements  IAccountService{

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BankTransactionService bankTransactionService;


    @Override
    public void deleteByClient(Client client) {

        List<Account> accountList = accountRepository.findAccountsByClient(client);
        accountList.forEach(account -> {
            bankTransactionService.deleteByAccount(account);
            accountRepository.deleteById(account.getId());
        });
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> result = new ArrayList<Account>();
        accountRepository.findAll().forEach(result::add);
        return result;
    }

    @Override
    public List<Account> findAccountsByClient(Client client) {
        return accountRepository.findAccountsByClient(client);
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }

}
