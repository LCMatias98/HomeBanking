package com.mindhub.homebanking.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class ClientDTO{

    private long id;
    private String firstName;
    private String lastName;
    private String email;

    private Set<AccountDTO> accounts;

    private List<ClientLoanDTO> loans;


    private Set<CardDTO> cards;

    public ClientDTO(Client client) {

        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEmail();

        this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());

        this.loans = client.getClientLoans().stream().map(ClientLoanDTO::new).collect(Collectors.toList());

        this.cards = client.getCards().stream().map(CardDTO::new).collect(Collectors.toSet());
    }

    public Set<CardDTO> getCards() {
        return cards;
    }

    public ClientDTO(){}

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }


    public ClientDTO(Account account) {
    }

    public long getId() {
        return id;
    }

    public List<ClientLoanDTO> getLoans() {
        return loans;
    }

    public String getFirstName() {
        return firstName;
    }

   

    public String getLastName() {
        return lastName;
    }

    

    public String getEmail() {
        return email;
    }


}
