package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Enums.AccountType;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDate creationDate;
    private double balance;
    private Boolean hidden;
    private Set<TransactionDTO> transaction;

    private AccountType accountType;

    public AccountDTO(){}

    public AccountDTO(Account account) {

        this.id = account.getId();

        this.number = account.getNumber();

        this.creationDate = account.getCreationDate();

        this.balance = account.getBalance();

        this.transaction = account.getTransaction().stream().map(TransactionDTO::new).collect(Collectors.toSet());;

        this.hidden = account.getHidden();

        this.accountType = account.getType();
    }
//distinct


    public AccountType getAccountType() {
        return accountType;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public Set<TransactionDTO> getTransaction() {
        return transaction;
    }

    public long getId() {
        return id;
    }


    public String getNumber() {
        return number;
    }


    public LocalDate getCreationDate() {
        return creationDate;
    }



    public double getBalance() {
        return balance;
    }


}
