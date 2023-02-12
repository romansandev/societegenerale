package com.example.exercise.infrastructure.db.springdata.services;

import com.example.exercise.domain.exceptions.BrokeAccountException;
import com.example.exercise.infrastructure.db.springdata.model.Account;
import com.example.exercise.infrastructure.db.springdata.model.BankTransaction;
import com.example.exercise.infrastructure.db.springdata.model.OperationType;
import com.example.exercise.infrastructure.db.springdata.model.Client;
import com.example.exercise.infrastructure.db.springdata.repositories.AccountRepository;
import com.example.exercise.infrastructure.db.springdata.repositories.BankTransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankTransactionService implements IBankTransactionService{
    @Autowired
    BankTransactionRepository bankTransactionRepository;
    @Autowired
    AccountRepository accountRepository;

    @Override
    public void deleteByAccount(Account account) {
        List<BankTransaction> bankTransactionList = bankTransactionRepository.findBankTransactionsByAccount(account);
        bankTransactionList.forEach(bankTransaction -> bankTransactionRepository.deleteById(bankTransaction.getId()));
    }

    @Override
    @Transactional
    public void insert(BankTransaction bankTransaction) throws BrokeAccountException {
        Account account = bankTransaction.getAccount();
        Long accountFunds = account.getMoney();
        if (OperationType.WITHDRAWAL.equals(bankTransaction.getOperationType())) {
            if (bankTransaction.getMoney() > accountFunds) {
                throw new BrokeAccountException("trying to withdraw " + bankTransaction.getMoney() + " from account with lesser funds (" + accountFunds + ")");
            } else {
                account.setMoney(accountFunds - bankTransaction.getMoney());
            }
        } else {
            account.setMoney(accountFunds + bankTransaction.getMoney());
        }
        account.addBankTransaction(bankTransaction);
        accountRepository.save(account);
        bankTransactionRepository.save(bankTransaction);
    }

    @Override
    public List<BankTransaction> findBankTransactionsByAccount(Account account) {
        return bankTransactionRepository.findBankTransactionsByAccount(account);
    }

    @Override
    public List<BankTransaction> findBankTransactionsByClient(Client client) {
        return accountRepository.findAccountsByClient(client).stream()
                .map(account -> bankTransactionRepository.findBankTransactionsByAccount(account))
                .flatMap(List::stream)
                .collect(Collectors.toList());


    }
}
