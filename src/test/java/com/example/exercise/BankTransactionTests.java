package com.example.exercise;

import com.example.exercise.configuration.SocieteGeneraleConfig;
import com.example.exercise.domain.exceptions.BrokeAccountException;
import com.example.exercise.infrastructure.db.springdata.controllers.AccountController;
import com.example.exercise.infrastructure.db.springdata.controllers.BankTransactionController;
import com.example.exercise.infrastructure.db.springdata.controllers.ClientController;
import com.example.exercise.infrastructure.db.springdata.dtos.AccountDto;
import com.example.exercise.infrastructure.db.springdata.dtos.ClientDto;
import com.example.exercise.infrastructure.db.springdata.model.Account;
import com.example.exercise.infrastructure.db.springdata.model.BankTransaction;
import com.example.exercise.infrastructure.db.springdata.model.OperationType;
import com.example.exercise.infrastructure.db.springdata.model.Client;
import com.example.exercise.infrastructure.db.springdata.repositories.AccountRepository;
import com.example.exercise.infrastructure.db.springdata.repositories.BankTransactionRepository;
import com.example.exercise.infrastructure.db.springdata.repositories.ClientRepository;
import com.example.exercise.infrastructure.db.springdata.services.BankTransactionService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ExerciseApplication.class, SocieteGeneraleConfig.class})
@Sql(scripts = {"classpath:clientdata.sql"})
public class BankTransactionTests {

    @Autowired
    BankTransactionRepository bankTransactionRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    BankTransactionService bankTransactionService;

    @Autowired
    ClientController clientController;
    @Autowired
    AccountController accountController;
    @Autowired
    BankTransactionController bankTransactionController;

    @After
    public void afterTests() {
        bankTransactionRepository.deleteAll();
        accountRepository.deleteAll();
        clientRepository.deleteAll();
    }


    @Before
    public void beforeTests() {

        ClientDto client1 = clientController.getById(1L);
        AccountDto account11 = new AccountDto((long) 2987.26, LocalDateTime.now(), client1);
        accountController.save(account11);

        ClientDto client2 = clientController.getById(2L);
        AccountDto account12 = new AccountDto((long) 12500.00, LocalDateTime.now(), client2);
        AccountDto account22 = new AccountDto((long) 987.26, LocalDateTime.now(), client2);
        accountController.save(account12);
        accountController.save(account22);

    }

    @Test
    public void insertBankWithdrawalCorrect() throws BrokeAccountException {
        ClientDto client = clientController.getById(1L);
        AccountDto account = accountController.getAccountsByClient(client).get(0);
        Long funds = account.getMoney();

        assertThat(bankTransactionRepository.findAll()).isEmpty();

        bankTransactionController.insert(account.getId(), (long) 285.02, OperationType.WITHDRAWAL.name());

        account = accountController.getAccountsByClient(client).get(0);
        assertThat(bankTransactionRepository.findAll()).isNotEmpty();
        assertThat(funds > account.getMoney()).isTrue();

    }

    @Test
    public void insertBankWithdrawalException() throws BrokeAccountException {
        Client client = clientRepository.findById(1L).get();
        Account account = accountRepository.findAccountsByClient(client).get(0);
        BankTransaction bankTransaction = new BankTransaction((long) 20285.02, LocalDateTime.now(), OperationType.WITHDRAWAL, account);

        assertThat(bankTransactionRepository.findAll()).isEmpty();

        assertThatExceptionOfType(BrokeAccountException.class).isThrownBy(() ->
                bankTransactionService.insert(bankTransaction)
        );

    }

    @Test
    public void insertBankDeposit() throws BrokeAccountException {
        ClientDto client = clientController.getById(1L);
        AccountDto account = accountController.getAccountsByClient(client).get(0);
        Long funds = account.getMoney();
        assertThat(bankTransactionRepository.findAll()).isEmpty();

        bankTransactionController.insert(account.getId(), (long) 285.02, OperationType.DEPOSIT.name());

//        bankTransactionService.insert(bankTransaction);

        account = accountController.getAccountsByClient(client).get(0);
        assertThat(bankTransactionRepository.findAll()).isNotEmpty();
        assertThat(funds < account.getMoney()).isTrue();
    }

    @Test
    public void HistoricTransactionsByAccount() throws BrokeAccountException {

        ClientDto client = clientController.getById(1L);

        AccountDto account = accountController.getAccountsByClient(client).get(0);
        Long funds = account.getMoney();

        assertThat(bankTransactionController.getBankTransactionsByAccount(account)).isEmpty();

        bankTransactionController.insert(account.getId(), (long) 285.02, OperationType.WITHDRAWAL.name());
        bankTransactionController.insert(account.getId(), 350, OperationType.DEPOSIT.name());
        bankTransactionController.insert(account.getId(), 500, OperationType.WITHDRAWAL.name());
        bankTransactionController.insert(account.getId(), 586, OperationType.WITHDRAWAL.name());

        assertThat(bankTransactionController.getBankTransactionsByAccount(account)).hasSize(4);

    }

    @Test
    public void HistoricTransactionsByclient() throws BrokeAccountException {

        ClientDto client = clientController.getById(2L);

        AccountDto account1 = accountController.getAccountsByClient(client).get(0);
        Long funds1 = account1.getMoney();
        AccountDto account2 = accountController.getAccountsByClient(client).get(1);
        Long funds2 = account2.getMoney();

        assertThat(bankTransactionController.getBankTransactionsByClient(client).getBody()).isEmpty();

        bankTransactionController.insert(account1.getId(), (long) 285.02, OperationType.WITHDRAWAL.name());
        bankTransactionController.insert(account1.getId(), 350, OperationType.DEPOSIT.name());
        bankTransactionController.insert(account1.getId(), 500, OperationType.WITHDRAWAL.name());
        bankTransactionController.insert(account2.getId(), 586, OperationType.WITHDRAWAL.name());

        assertThat(bankTransactionController.getBankTransactionsByClient(client).getBody()).hasSize(4);
        assertThat(funds1 > accountController.getAccountsByClient(client).get(0).getMoney());
        assertThat(funds2 > accountController.getAccountsByClient(client).get(1).getMoney());
    }
}