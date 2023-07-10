package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAccounts();
    }

    @RequestMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication){
        Client client = clientService.findByEmail(authentication.getName());
        // cuando busca en la base de datos guarda sino null
        Account account = accountService.findById(id);

        if (account == null){
            return new ResponseEntity<>("Account not Exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("Account is not yours", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new AccountDTO(account),HttpStatus.ACCEPTED);
    }


    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        Set<Account> accountsAuth = client.getAccounts();
        String numberRandom;

        do{
            Random random = new Random();
            numberRandom = "VIN-" + random.nextInt(9999999);
        }while (accountService.getAccountByNumber(numberRandom) !=null);
        if (accountsAuth.size() == 3 ) {
            return new ResponseEntity<>("Limit Accounts", HttpStatus.FORBIDDEN);
        }else {
           Account account = new Account(numberRandom, LocalDate.now(),0.0);
           client.addAccount(account);
            accountService.saveAccount(account);
           return new ResponseEntity<>("Account Created",HttpStatus.CREATED);
        }
    }

}
