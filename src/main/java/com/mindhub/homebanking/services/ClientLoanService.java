package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;

public interface ClientLoanService {

    ClientLoan findById(long  id);

    void saveClientLoan(ClientLoan clientLoan);
}
