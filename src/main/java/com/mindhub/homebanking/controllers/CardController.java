package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Enums.CardColor;
import com.mindhub.homebanking.models.Enums.CardType;

import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.Random;

import static com.mindhub.homebanking.utils.Utilities.getCvv;
import static com.mindhub.homebanking.utils.Utilities.getRandomCardNumber;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private CardService cardService;

    //    @GetMapping(“”)
//@PostMapping(“”)

    @PostMapping(path = "/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType cardType, @RequestParam CardColor cardColor) {
        Client client = clientService.findByEmail(authentication.getName());

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

}