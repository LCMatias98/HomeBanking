package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import com.mindhub.homebanking.models.Enums.AccountType;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.Utilities.getNumberRandom;


@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;

//    @GetMapping(“”)
//@PostMapping(“”)
    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAccounts();
    }

    @GetMapping("/accounts/{id}")
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


    @PostMapping(path = "/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication, @RequestParam AccountType accountType) {
        Client client = clientService.findByEmail(authentication.getName());
        Set<Account> accountsAuth = client.getAccounts();
        String numberRandom;

        do{
            Random random = new Random();
            numberRandom = getNumberRandom(random);
        }while (accountService.getAccountByNumber(numberRandom) !=null);
        if (accountsAuth.size() == 3 ) {
            return new ResponseEntity<>("Limit Accounts", HttpStatus.FORBIDDEN);
        }else {
           Account account = new Account(numberRandom, LocalDate.now(),0.0,false,accountType);
           client.addAccount(account);
            accountService.saveAccount(account);
           return new ResponseEntity<>("Account Created",HttpStatus.CREATED);
        }
    }

    @PatchMapping(path = "/clients/current/accounts")
    public ResponseEntity<Object> hideAccount(Authentication authentication, @RequestParam long id){
        Client client = clientService.findByEmail(authentication.getName());

        Account accountToDisable = accountService.findById(id);
        Set<Transaction> transactionToDisable = accountToDisable.getTransaction();

        if (!client.getAccounts().contains(accountToDisable)){
            return new ResponseEntity<>("Account is not yours", HttpStatus.FORBIDDEN);
        }
//        if (!accountToDisable.getTransaction().contains(transactionToDisable)){
//            return new ResponseEntity<>("Transactions is not yours", HttpStatus.FORBIDDEN);
//        }
        if (accountToDisable.getBalance() > 0){
            return new ResponseEntity<>("You can't disable account with money", HttpStatus.FORBIDDEN);
        }
        transactionToDisable.forEach(tran -> tran.setHidden(true));
        accountToDisable.setHidden(true);

        List<Transaction> transactionList = transactionToDisable.stream().map(transaction -> {transaction.setHidden(true);return transaction;}).collect(Collectors.toList());

        transactionRepository.saveAll(transactionList);
        accountService.saveAccount(accountToDisable);
        return new ResponseEntity<>("The Account has been disabled", HttpStatus.OK);
    }


}
