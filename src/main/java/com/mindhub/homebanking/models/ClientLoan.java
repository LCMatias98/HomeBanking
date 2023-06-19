package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Double amount;
    private Integer payments;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    public ClientLoan() {}

    public ClientLoan(Double amount, Integer payments) {
        this.amount = amount;
        this.payments = payments;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public void setClient(Client clients) {
        this.client = clients;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public long getId() {
        return id;
    }
    public Double getAmount() {
        return amount;
    }

    public Client getClient() {
        return client;
    }

    public Integer getPayments() {
        return payments;
    }

    public Client getClients() {
        return client;
    }
    public Loan getLoan() {
        return loan;
    }
}
