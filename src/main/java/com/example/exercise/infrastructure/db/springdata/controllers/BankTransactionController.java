package com.example.exercise.infrastructure.db.springdata.controllers;

import com.example.exercise.infrastructure.db.springdata.dtos.AccountDto;
import com.example.exercise.infrastructure.db.springdata.dtos.BankTransactionDto;
import com.example.exercise.domain.exceptions.BrokeAccountException;
import com.example.exercise.infrastructure.db.springdata.dtos.ClientDto;
import com.example.exercise.infrastructure.db.springdata.model.Account;
import com.example.exercise.infrastructure.db.springdata.model.BankTransaction;
import com.example.exercise.infrastructure.db.springdata.model.OperationType;
import com.example.exercise.infrastructure.db.springdata.model.Client;
import com.example.exercise.infrastructure.db.springdata.repositories.AccountRepository;
import com.example.exercise.infrastructure.db.springdata.services.BankTransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Controller
public class BankTransactionController {

    @Autowired
    BankTransactionService bankTransactionService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger LOGGER = Logger.getLogger(BankTransactionController.class.getName());


    public void deleteByAccountDto(AccountDto accountDto) {
        bankTransactionService.deleteByAccount(modelMapper.map(accountDto, Account.class));
    }

    @PostMapping("/account/{accountId}/transaction/{money}/{operationType}")
    public ResponseEntity<BankTransaction> insert(@PathVariable long accountId, @PathVariable long money, @PathVariable String operationType) throws BrokeAccountException {

        if(Arrays.stream(OperationType.values()).filter(value -> value.name().equals(operationType)).collect(Collectors.toList()).isEmpty()){
            LOGGER.log(Level.SEVERE, "operationType: " + operationType + " not supported", new Exception());
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        BankTransaction bankTransaction = convertToEntity(new BankTransactionDto(money, LocalDateTime.now(), OperationType.valueOf(operationType)));
        Account account = accountRepository.findById(accountId).orElse(null);
        if(account == null){
            LOGGER.log(Level.SEVERE, "account_id: " + accountId + " not found", new Exception());
            return new ResponseEntity<>(bankTransaction, HttpStatus.NOT_ACCEPTABLE);
        }
        bankTransactionService.findBankTransactionsByAccount(account);
        account.setBankTransactions(bankTransactionService.findBankTransactionsByAccount(account).stream().collect(Collectors.toSet()));
        bankTransaction.setAccount(account);
        bankTransactionService.insert(bankTransaction);
        return new ResponseEntity<>(bankTransaction, HttpStatus.CREATED);
    }

    private BankTransactionDto convertToDto(BankTransaction bankT) {
        BankTransactionDto bankTDto = modelMapper.map(bankT, BankTransactionDto.class);
        return bankTDto;
    }

    private BankTransaction convertToEntity(BankTransactionDto bankTDto) {
        BankTransaction bankTransaction = modelMapper.map(bankTDto, BankTransaction.class);
        return bankTransaction;
    }

    private Account convertToEntity(AccountDto accountDto) {
        Account account = modelMapper.map(accountDto, Account.class);
        return account;
    }

    public List<BankTransaction> getBankTransactionsByAccount(AccountDto account) {
        return bankTransactionService.findBankTransactionsByAccount(modelMapper.map(account, Account.class));
    }

    @GetMapping("/client/transaction/historic")
    public ResponseEntity<List<BankTransaction>> getBankTransactionsByClient(@RequestBody ClientDto client) {
        List<BankTransaction> result = bankTransactionService.findBankTransactionsByClient(modelMapper.map(client, Client.class));
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}
