package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {

    void saveAccount(Account account);

    List<AccountDTO> getAccounts();

    Account findById(long id);

    Account getAccountByNumber(String number);
}
