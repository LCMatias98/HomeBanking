package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Enums.CardColor;
import com.mindhub.homebanking.models.Enums.CardType;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class CardDTO {
    private long id;
    private String cardHolder;
    private CardType type;
    private CardColor color;
    private String number;
    private Short ccv;
    private LocalDate thruDate;
    private LocalDate fromDate;
    private Client client;

    public CardDTO(Card card) {

        this.id = card.getId();

        this.cardHolder = card.getCardHolder();

        this.type = card.getType();

        this.color = card.getColor();

        this.number = card.getNumber();

        this.ccv = card.getCcv();

        this.thruDate = card.getThruDate();

        this.fromDate = card.getFromDate();

    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
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

    public LocalDate getFromDate() {
        return fromDate;
    }

    public Client getClient() {
        return client;
    }
}
