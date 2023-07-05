package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransferDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Enums.TransactionType;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository  transactionRepository;

    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> sendTransactions(Authentication authentication, @RequestBody TransferDTO transferDTO){

        Client client = clientRepository.findByEmail(authentication.getName());
        Account originAccount = accountRepository.findByNumber(transferDTO.getAccountOrigin());
        Account destinationAccount = accountRepository.findByNumber(transferDTO.getAccountDestination());
        Double amount = transferDTO.getAmount();
        String description = transferDTO.getDescription();

        if (transferDTO.getAccountOrigin().isBlank()){
            return  new ResponseEntity<>("Please complete the Origin Account", HttpStatus.FORBIDDEN);
        }
        if (transferDTO.getAccountDestination().isBlank()){
            return  new ResponseEntity<>("Please complete the Destination Account,", HttpStatus.FORBIDDEN);
        }
        if (transferDTO.getAmount().isNaN()){
            return  new ResponseEntity<>("Please complete the Amount,", HttpStatus.FORBIDDEN);
        }
        if (transferDTO.getDescription().isBlank()){
            return  new ResponseEntity<>("Please complete the Description,", HttpStatus.FORBIDDEN);
        }
        if (transferDTO.getAccountDestination().equals(transferDTO.getAccountOrigin())){
            return  new ResponseEntity<>("The accounts are the same", HttpStatus.FORBIDDEN);
        }
        if(originAccount == null){
            return new ResponseEntity<>("Origin account does not exist",HttpStatus.FORBIDDEN);
        }
        if(destinationAccount == null){
            return new ResponseEntity<>("Destination account does not exist",HttpStatus.FORBIDDEN);
        }

        if(client.getAccounts().stream().filter(item -> item.getNumber().equals(transferDTO.getAccountOrigin())).collect(Collectors.toSet()).isEmpty()){
            return new ResponseEntity<>("The account does not belong to you",HttpStatus.FORBIDDEN);
        }
        if(originAccount.getBalance() < transferDTO.getAmount() ){
            return new ResponseEntity<>("You don't have enough funds",HttpStatus.FORBIDDEN);
        }
        Transaction transaction = new Transaction(TransactionType.CREDIT,amount,transferDTO.getAccountOrigin() +": " + description, LocalDateTime.now());
        Transaction transaction1 = new Transaction(TransactionType.DEBIT,Double.parseDouble("-" + amount),transferDTO.getAccountDestination() +": " +description, LocalDateTime.now());


        originAccount.setBalance(originAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        destinationAccount.addTransaction(transaction);
        originAccount.addTransaction(transaction1);

        transactionRepository.save(transaction);
        transactionRepository.save(transaction1);

        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);

        return new ResponseEntity<>("Transactions succes",HttpStatus.CREATED);
    }
}
