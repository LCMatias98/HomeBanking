package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Enums.CardColor;
import com.mindhub.homebanking.models.Enums.CardType;

import java.time.LocalDate;

public class paymentWhitCardDTO {

        private String cardHolder;
        private String number;
        private Short ccv;
        private LocalDate thruDate;
        private Double amount;
        private String description;

//        public paymentWhitCardDTO(Card card) {
//
//            this.cardHolder = card.getCardHolder();
//
//            this.number = card.getNumber();
//
//            this.ccv = card.getCcv();
//
//            this.thruDate = card.getThruDate();
//
//
//        }

    public paymentWhitCardDTO() {
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public Short getCcv() {
        return ccv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }
}
