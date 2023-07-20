package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Enums.CardColor;
import com.mindhub.homebanking.models.Enums.CardType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImplement implements CardService {


    @Autowired
    private CardRepository cardRepository;
    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card findByClientAndColorAndType(Client client, CardColor color, CardType type) {
        return cardRepository.findByClientAndColorAndType(client, color, type);
    }

    @Override
    public Card findById(long id) {
        return cardRepository.findById(id).orElse(null);
    }


}
