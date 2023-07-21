package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

        List<Transaction> findById(long Id);
        Transaction findByAccount(Account account);
        Transaction findByDate(LocalDateTime date);

//        @Query("SELECT t FROM Transaction t WHERE t.date BETWEEN :dateStart AND :dateEnd AND t.account = :account")
        List<Transaction> findByDateBetweenAndAccount(LocalDateTime dateStart, LocalDateTime dateEnd, Account account);

}