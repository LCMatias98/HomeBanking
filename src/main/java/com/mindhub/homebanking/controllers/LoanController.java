package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.dtos.TransferDTO;
import com.mindhub.homebanking.models.*;

import com.mindhub.homebanking.models.Enums.TransactionType;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private LoanService loanService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @RequestMapping(path = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> claimLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){
        Client client = clientService.findByEmail(authentication.getName());
        Set<Account> accountsAuth = client.getAccounts();

        if (loanApplicationDTO.getAmount() == null || loanApplicationDTO.getPayment() == null || loanApplicationDTO.getDestinationAccount() == null) {
            return new ResponseEntity<>("Please provide all the required information", HttpStatus.FORBIDDEN);
        }



        if (loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayment() <= 0) {
            return new ResponseEntity<>("Please enter valid loan amount and payment", HttpStatus.FORBIDDEN);
        }

        // Verificar que el préstamo exista POR NAME
        Loan loan = loanService.findByName(loanApplicationDTO.getName());
        if (loan == null) {
            return new ResponseEntity<>("Loan not found", HttpStatus.FORBIDDEN);
        }


        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("Loan amount exceeds maximum limit", HttpStatus.FORBIDDEN);
        }

        // Verificar que la cantidad de cuotas se encuentre entre las disponibles del préstamo
        List<Integer> availablePayments = loan.getPayments();
        if (!availablePayments.contains(loanApplicationDTO.getPayment())) {
            return new ResponseEntity<>("Invalid number of payments", HttpStatus.FORBIDDEN);
        }

        // Verificar que la cuenta de destino exista
        Account destinationAccount = accountService.getAccountByNumber(loanApplicationDTO.getDestinationAccount());
        if (destinationAccount == null) {
            return new ResponseEntity<>("Destination account not found", HttpStatus.FORBIDDEN);
        }

        // Verificar que la cuenta de destino pertenezca al cliente autenticado
        if (!accountsAuth.contains(destinationAccount)) {
            return new ResponseEntity<>("Destination account does not belong to the authenticated client", HttpStatus.FORBIDDEN);
        }

        //Se debe crear una solicitud de préstamo con el monto solicitado sumando el 20% del mismo
        double loanAmount = loanApplicationDTO.getAmount() * 1.2;
//        Loan newLoan = new Loan(loanAmount, loanApplicationDTO.getPayment());
//        loanRepository.save(newLoan);
        ClientLoan newLoan = new ClientLoan(loanAmount, loanApplicationDTO.getPayment());



        //Se debe crear una transacción “CREDIT” asociada a la cuenta de destino (el monto debe quedar positivo) con la descripción concatenando el nombre del préstamo y la frase “loan approved”
        // public Transaction(TransactionType type, double amount, String description, LocalDateTime date)
        String description = loan.getName() + " loan approved";
        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), description, LocalDateTime.now());
        destinationAccount.addTransaction(transaction);
        // Actualizar la cuenta de destino sumando el monto solicitado
        destinationAccount.setBalance(destinationAccount.getBalance() + loanApplicationDTO.getAmount());
        destinationAccount.addTransaction(transaction);
        client.addClientLoan(newLoan);
        loan.addClientLoan(newLoan);
        loanService.saveLoan(loan);
        clientService.saveClient(client);
        transactionService.saveTransaction(transaction);
        clientLoanRepository.save(newLoan);
        accountService.saveAccount(destinationAccount);

        return new ResponseEntity<>("Loan application successful", HttpStatus.CREATED);
    }

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getLoans();
    }

}

//    @RequestMapping(path = "/loans", method = RequestMethod.GET)
//    public ResponseEntity<Object> claimLoan(Authentication authentication, @RequestBody LoanDTO loanDTO){
//        Client client = clientRepository.findByEmail(authentication.getName());
//        Set<Account> accountsAuth = client.getAccounts();
//
//    }

