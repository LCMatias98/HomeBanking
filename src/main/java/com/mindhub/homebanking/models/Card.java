package com.mindhub.homebanking.models;

import com.mindhub.homebanking.models.Enums.CardColor;
import com.mindhub.homebanking.models.Enums.CardType;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") // strategia para generar pk auto
    @GenericGenerator(name = "native", strategy = "native") //strategia por defecto db
    private long id;
    private String cardHolder;
    private CardType type;
    private CardColor color;
    private String number;
    private Short ccv;
    private LocalDate thruDate;
    private LocalDate fromDate;
    private Boolean hidden;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    public Card() {}

    public Card(String cardHolder, CardType type, CardColor color, String number, Short ccv, LocalDate thruDate, LocalDate fromDate, Boolean hidden) {
        this.cardHolder = cardHolder;
        this.type = type;
        this.color = color;
        this.number = number;
        this.ccv = ccv;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
        this.hidden = hidden;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public long getId() {
        return id;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    @JsonIgnore
    public void setClient(Client client) {
        this.client = client;
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

    public short getCcv() {
        return ccv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }
}
