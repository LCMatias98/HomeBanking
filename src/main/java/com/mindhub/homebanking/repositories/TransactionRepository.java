package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

        List<Transaction> findById(long Id);
        Transaction findByAccount(Account account);
        Transaction findByDate(LocalDateTime date);

        List<Transaction> findByDateBetween(LocalDateTime dateStart, LocalDateTime dateEnd);
}