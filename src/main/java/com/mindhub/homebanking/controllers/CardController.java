package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.paymentWhitCardDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Enums.CardColor;
import com.mindhub.homebanking.models.Enums.CardType;

import com.mindhub.homebanking.models.Enums.TransactionType;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;

import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import javax.lang.model.type.NullType;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;

import static com.mindhub.homebanking.utils.Utilities.getCvv;
import static com.mindhub.homebanking.utils.Utilities.getRandomCardNumber;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private CardService cardService;

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    //    @GetMapping(“”)
//@PostMapping(“”)


    @PostMapping(path = "/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType cardType, @RequestParam CardColor cardColor) {
        Client client = clientService.findByEmail(authentication.getName());

        if (client == null){
            return new ResponseEntity<>("The Client does not exist", HttpStatus.FORBIDDEN);
        }

        if(cardType == null){
            return new ResponseEntity<>("Please enter Card Type", HttpStatus.FORBIDDEN);
        }

        if(cardColor == null){
            return new ResponseEntity<>("Please enter Card Color", HttpStatus.FORBIDDEN);
        }

        Random randomCVV = new Random();
        short CVV = getCvv(randomCVV);

        String randomCardNumber;
        do {
            Random randomNumber = new Random();
            randomCardNumber = getRandomCardNumber(randomNumber);
        } while (cardService.findByClientAndColorAndType(client, cardColor, cardType) != null);

        if (client.getCards().size() == 6) {
            return new ResponseEntity<>("Limit Card", HttpStatus.FORBIDDEN);
        } else {
            Card card = new Card(client.getFirstName() + " " + client.getLastName(), cardType, cardColor, randomCardNumber, CVV, LocalDate.now().plusYears(5), LocalDate.now(),false);
            client.addCard(card);
            cardService.saveCard(card);
            return new ResponseEntity<>("Card Created", HttpStatus.CREATED);
        }
    }

    @PatchMapping(path = "/clients/current/cards")
    public ResponseEntity<Object> hideCard(Authentication authentication, @RequestParam long id){
        Client client = clientService.findByEmail(authentication.getName());

        Card cardToDisable = cardService.findById(id);

        if (!client.getCards().contains(cardToDisable)){
            return new ResponseEntity<>("Card is not yours", HttpStatus.FORBIDDEN);
        }

        cardToDisable.setHidden(true);
        cardService.saveCard(cardToDisable);
        return new ResponseEntity<>("The Card has been disabled", HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @Transactional
    @PostMapping(path = "/clients/current/card/payment")
    public ResponseEntity<Object> payWithCard( @RequestBody paymentWhitCardDTO pay) {

        Card card = cardService.findByNumber(pay.getNumber());
        Client client = card.getClient();
        Set<Account> accounts = client.getAccounts();
        Account firstAccount = accounts.iterator().next();

        Double amountToPay = pay.getAmount();
        String description = pay.getDescription();

//        Set<Transaction> transactions = firstAccount.getTransaction();


        if (firstAccount.getHidden()){
            return new ResponseEntity<>("Account disabled", HttpStatus.FORBIDDEN);
        }
        if (!pay.getCardHolder().equals(card.getCardHolder()) ){
            return new ResponseEntity<>("Verify Card Holder", HttpStatus.FORBIDDEN);
        }
        if (!pay.getNumber().equals(card.getNumber())){
            return new ResponseEntity<>("Verify Card Number", HttpStatus.FORBIDDEN);
        }
        if (!pay.getCcv().equals(card.getCcv())){
            return new ResponseEntity<>("Verify Card CVV", HttpStatus.FORBIDDEN);
        }
        if (!pay.getThruDate().equals(card.getThruDate())){
            return new ResponseEntity<>("Verify Thru Date", HttpStatus.FORBIDDEN);
        }
        if (pay.getAmount() > firstAccount.getBalance()){
            return new ResponseEntity<>("Insufficient Funds", HttpStatus.FORBIDDEN);
        }
//        if (pay.getThruDate() > card.getThruDate()){
//            return new ResponseEntity<>("Expired Card", HttpStatus.FORBIDDEN);
//        }
        firstAccount.setBalance(firstAccount.getBalance() - pay.getAmount());
        Transaction transaction = new Transaction(TransactionType.DEBIT,amountToPay,pay.getDescription(), LocalDateTime.now(),false);
        firstAccount.addTransaction(transaction);
        accountService.saveAccount(firstAccount);
        transactionService.saveTransaction(transaction);

        return new ResponseEntity<>("Payment Accepted", HttpStatus.OK);
}
}