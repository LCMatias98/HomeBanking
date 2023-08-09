package com.mindhub.homebanking.dtos;

import java.util.List;

public class LoanApplicationDTO {

    private long id;

    private String name;
    private Double amount;
    private Integer payment; // List<Integer>
    private String destinationAccount;



    //Recibimos el Objeto
    public LoanApplicationDTO() {
    }

//    public LoanApplicationDTO(long id, Double amount, Integer payment, String destinationAccount) {
//        this.id = id;
//        this.amount = amount;
//        this.payment = payment;
//        this.destinationAccount = destinationAccount;
//    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayment() {
        return payment;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }
}
