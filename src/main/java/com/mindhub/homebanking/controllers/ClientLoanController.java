package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Enums.TransactionType;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientLoanService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class ClientLoanController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientLoanService clientLoanService;

    @PostMapping(path = "/client-loans/payment")
    public ResponseEntity<Object> payLoan(Authentication authentication, @RequestParam String numberAccount, @RequestParam Long id) {
        Client client = clientService.findByEmail(authentication.getName());
        Account accountToPay = accountService.getAccountByNumber(numberAccount);
        ClientLoan clientLoan = clientLoanService.findById(id);
        Double payment = clientLoan.getAmount() / clientLoan.getPayments();

        if (client == null){
            return new ResponseEntity<>("The Client does not exist", HttpStatus.FORBIDDEN);
        }

        if (accountToPay == null){
            return new ResponseEntity<>("The Account does not exist", HttpStatus.FORBIDDEN);
        }

        if (accountToPay.getBalance() < payment){
            return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
        }

        Transaction transaction = new Transaction(TransactionType.DEBIT,payment,"Loan Payment", LocalDateTime.now(),false);

        accountToPay.setBalance(accountToPay.getBalance()-payment);

        clientLoan.setRemainingAmount(clientLoan.getRemainingAmount()-payment);
        clientLoan.setRemainingPayments(clientLoan.getRemainingPayments()- 1);
        accountToPay.addTransaction(transaction);

        accountService.saveAccount(accountToPay);
        clientLoanService.saveClientLoan(clientLoan);
        transactionService.saveTransaction(transaction);

        return new ResponseEntity<>("The Loan has been paid", HttpStatus.OK);
    }


}
