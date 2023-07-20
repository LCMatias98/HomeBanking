package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import java.util.List;
import java.util.Set;

public interface TransactionService {

    void saveTransaction(Transaction transaction);

    Transaction findById(long id);

    Transaction findByAccount(Account account);


}
