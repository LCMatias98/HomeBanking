package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Enums.CardColor;
import com.mindhub.homebanking.models.Enums.CardType;

public interface CardService {

    void saveCard(Card card);
    Card findByClientAndColorAndType(Client client, CardColor color, CardType type);
}
