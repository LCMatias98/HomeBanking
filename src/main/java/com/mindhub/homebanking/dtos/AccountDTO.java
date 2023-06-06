package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDate creationDate;
    private double balance;

    private Client client;

    public AccountDTO(){}
    public AccountDTO(Account account) {

        this.id = account.getId();

        this.number = account.getNumber();

        this.creationDate = account.getCreationDate();

        this.balance = account.getBalance();

    }

    public Client getClient() {
        return client;
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
