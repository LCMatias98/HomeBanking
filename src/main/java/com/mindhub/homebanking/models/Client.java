package com.mindhub.homebanking.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") // strategia para generar pk auto
    @GenericGenerator(name = "native", strategy = "native") //strategia por defecto db
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @OneToMany(mappedBy="client", fetch=FetchType.LAZY) //traetodo
    private Set<Account> accounts = new HashSet<>();
    @OneToMany(mappedBy="client", fetch=FetchType.LAZY)
    private List<ClientLoan> clientLoans = new ArrayList<>();

    @OneToMany(mappedBy="client", fetch=FetchType.LAZY)
    private Set<Card> cards = new HashSet<>();
    public Client() { }
    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
    @JsonIgnore
    public Set<Card> getCards() {
        return cards;
    }
    public void addCard(Card card) {
        card.setClient(this);
        cards.add(card);
    }
    @JsonIgnore
    public List<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }
    @JsonIgnore
    public Set<Account> getAccounts() {
        return accounts;
    }


    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Client{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return getId() == client.getId() && Objects.equals(getFirstName(), client.getFirstName()) && Objects.equals(getLastName(), client.getLastName()) && Objects.equals(getEmail(), client.getEmail()) && Objects.equals(getAccounts(), client.getAccounts()) && Objects.equals(getClientLoans(), client.getClientLoans()) && Objects.equals(getCards(), client.getCards());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getEmail(), getAccounts(), getClientLoans(), getCards());
    }
}
