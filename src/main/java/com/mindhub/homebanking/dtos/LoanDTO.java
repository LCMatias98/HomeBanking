package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanDTO {
    private long id;

    private String name;
    private Double amount;
    private List<Integer> payment;

    private Double interest;

//    public LoanDTO() {}


    //Enviamos el Objeto ?
    public LoanDTO(Loan loan){
        this.id = loan.getId();
        this.name = loan.getName();
        this.amount = loan.getMaxAmount();
        this.payment = loan.getPayments();
        this.interest = loan.getInterest();
    }


    public Double getInterest() {
        return interest;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }


    public List<Integer> getPayment() {
        return payment;
    }

}
