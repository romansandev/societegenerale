package com.example.exercise.infrastructure.db.springdata.controllers;

import com.example.exercise.infrastructure.db.springdata.dtos.AccountDto;
import com.example.exercise.infrastructure.db.springdata.dtos.ClientDto;
import com.example.exercise.infrastructure.db.springdata.model.Account;
import com.example.exercise.infrastructure.db.springdata.model.Client;
import com.example.exercise.infrastructure.db.springdata.services.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientController clientController;
    @Autowired
    private ModelMapper modelMapper;


    public List<AccountDto> getAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return accounts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<AccountDto> getAccountsByClient(ClientDto clientDto) {
        return accountService.findAccountsByClient(modelMapper.map(clientDto, Client.class))
                .stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<AccountDto> getAccountsByClientWithTransactions(ClientDto clientDto) {
        return accountService.findAccountsByClient(modelMapper.map(clientDto, Client.class))
                .stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public void deleteByClientDto(ClientDto clientDto) {
        accountService.deleteByClient(modelMapper.map(clientDto, Client.class));
    }

    private AccountDto convertToDto(Account account) {
        AccountDto accountDto = modelMapper.map(account, AccountDto.class);
        return accountDto;
    }

    private Account convertToEntity(AccountDto accountDto) {
        Account account = modelMapper.map(accountDto, Account.class);
        return account;
    }

    public void save(AccountDto account) {
        accountService.save(convertToEntity(account));
    }
}
