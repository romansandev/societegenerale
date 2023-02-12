package com.example.exercise;

import com.example.exercise.configuration.SocieteGeneraleConfig;
import com.example.exercise.infrastructure.db.springdata.model.Account;
import com.example.exercise.infrastructure.db.springdata.model.Client;
import com.example.exercise.infrastructure.db.springdata.repositories.AccountRepository;
import com.example.exercise.infrastructure.db.springdata.repositories.ClientRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ExerciseApplication.class, SocieteGeneraleConfig.class})
@Sql(scripts = {"classpath:clientdata.sql"})
public class AccountTests {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;

    @After
    public void afterTests(){
        accountRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void insertAccount(){

        Client client = clientRepository.findById(1L).get();
        Account account = new Account((long) 2987.26, LocalDateTime.now(), client);

        assertThat(accountRepository.findAll()).isEmpty();

        accountRepository.save(account);

        assertThat(accountRepository.findAll()).isNotEmpty();

    }

}
