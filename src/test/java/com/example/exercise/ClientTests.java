package com.example.exercise;

import com.example.exercise.configuration.SocieteGeneraleConfig;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ExerciseApplication.class, SocieteGeneraleConfig.class})
@Sql(scripts = {"classpath:clientdata.sql"})
class ClientTests {

    @Autowired
    ClientRepository clientRepo;
    @Autowired
    AccountRepository accountRepository;

    @After
    public void afterTests(){
        clientRepo.deleteAll();
    }

    @Test
    void InsertClients() {
        Client client = new Client("Jean-Christophe", "abc");
        clientRepo.save(client);
        assertThat(clientRepo.findClientsByClientName("Jean-Christophe").toString()).contains(client.toString());
    }

    @Test
    void DeleteClients(){
        Optional<Client> optclient = clientRepo.findClientsByClientName("Roberto").stream().findFirst();

        assertThat(optclient.isPresent()).isTrue();
        Client client = optclient.get();

        assertThat(clientRepo.findClientsByClientName("Roberto").toString()).contains(client.toString());
        clientRepo.deleteById(1L);
        assertThat(clientRepo.findClientsByClientName("Roberto")).isEmpty();
    }

}
