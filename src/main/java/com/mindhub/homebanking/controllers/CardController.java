package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Enums.CardColor;
import com.mindhub.homebanking.models.Enums.CardType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType cardType, @RequestParam CardColor cardColor) {
        Client client = clientRepository.findByEmail(authentication.getName());

        Random randomCVV = new Random();
        short CVV = (short) randomCVV.nextInt(999);

        String randomCardNumber;
        do {
            Random randomNumber = new Random();
            randomCardNumber = randomNumber.nextInt(9999) + "-" + randomNumber.nextInt(9999) + "-" + randomNumber.nextInt(9999) + "-" + randomNumber.nextInt(9999);
        } while (cardRepository.findByClientAndColorAndType(client, cardColor, cardType) != null);

        if (client.getCards().size() == 6) {
            return new ResponseEntity<>("Limit Card", HttpStatus.FORBIDDEN);
        } else {
            Card card = new Card(client.getFirstName() + " " + client.getLastName(), cardType, cardColor, randomCardNumber, CVV, LocalDate.now().plusYears(5), LocalDate.now());
            client.addCard(card);
            cardRepository.save(card);
            return new ResponseEntity<>("Card Created", HttpStatus.CREATED);
        }
    }
}