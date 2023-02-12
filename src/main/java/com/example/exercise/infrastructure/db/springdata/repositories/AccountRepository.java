package com.example.exercise.infrastructure.db.springdata.repositories;

import com.example.exercise.infrastructure.db.springdata.model.Account;
import com.example.exercise.infrastructure.db.springdata.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAccountsByClient(Client client);

}
