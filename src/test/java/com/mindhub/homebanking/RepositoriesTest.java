package com.mindhub.homebanking;

import com.mindhub.homebanking.controllers.ClientController;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;


import java.util.List;
import java.util.Set;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans,is(not(empty())));

    }

    @Test
    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

    }

    @Test
    public void existAccount() {
        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts, is(not(empty())));
    }

//    .stream().map(account -> new Account(account)).collect(Collectors.toList());
    @Test
    public void checkAccountLimits() {
        List<Client> clients = clientRepository.findAll();

        for (Client client : clients) {
            Set<Account> accounts = client.getAccounts();
            if (accounts.size() > 3) {
                // Si el Cliente tiene mas de 3 cuentas me trae su nombre y la cantidad de cuentas
                assertThat("El cliente " + client.getFirstName()+ client.getLastName() + " tiene más de 3 cuentas.", accounts.size(), lessThanOrEqualTo(3));
            }
        }
    }

    @Test
    public void existCard() {
        List<Card> cards = cardRepository.findAll();

        assertThat(cards, is(not(empty())));
    }

    @Test
    public void checkCardsLimits() {
        List<Client> clients = clientRepository.findAll();

        for (Client client : clients) {
            Set<Card> cards = client.getCards();
            if (cards.size() > 6) {
                // Si el Cliente tiene mas de 3 cuentas me trae su nombre y la cantidad de cuentas
                assertThat("El cliente " + client.getFirstName()+ client.getLastName() + " tiene más de 6 Tarjetas.", cards.size(), lessThanOrEqualTo(6));
            }
        }
    }


    @Test
    public void existClient() {
        List<Client> clients = clientRepository.findAll();

        assertThat(clients, is(not(empty())));
    }

//        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));



    @Test
    public void verifyClientData() {
        List<Client> clients = clientRepository.findAll();

        for (Client client : clients) {
            assertThat(client.getFirstName(), is(not(emptyString())));
            assertThat(client.getLastName(), is(not(emptyString())));
            assertThat(client.getEmail(), is(not(emptyString())));
            assertThat(client.getPassword(), is(not(emptyString())));
        }
    }


    @Test
    public void existTransaction() {
        List<Transaction> transactions = transactionRepository.findAll();

        assertThat(transactions, is(not(empty())));
    }


    @Test
    public void verifyClientTransferAmountValid() {
        List<Client> clients = clientRepository.findAll();

        for (Client client : clients) {
            Set<Account> accounts = client.getAccounts();
            for (Account account : accounts){
                Set<Transaction> transactions = account.getTransaction();
                for (Transaction transaction : transactions){
                    assertThat(Double.valueOf(transaction.getAmount()), is(notNullValue()));
                }
            }
        }
    }




}