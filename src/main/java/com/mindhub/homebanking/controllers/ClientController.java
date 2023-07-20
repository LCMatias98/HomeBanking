package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Enums.AccountType;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.mindhub.homebanking.utils.Utilities.getNumberRandom;


@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountService accountService;

    //    @GetMapping(“”)
//@PostMapping(“”)
    @GetMapping("/clients")
    public List<ClientDTO> getClient(){
        return clientService.getClientsDTO();
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientService.getClientDTO(id);
    }

    @PostMapping(path = "/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        if (firstName.isBlank()) {
            return new ResponseEntity<>("Please enter First Name", HttpStatus.FORBIDDEN);
        }
        if (lastName.isBlank()) {
            return new ResponseEntity<>("Please enter Last Name", HttpStatus.FORBIDDEN);
        }
        if (email.isBlank()) {
            return new ResponseEntity<>("Please enter Email", HttpStatus.FORBIDDEN);
        }
        if (password.isBlank()) {
            return new ResponseEntity<>("Please enter Password", HttpStatus.FORBIDDEN);
        }



        if (clientService.findByEmail(email)  != null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(client);

        // Crear una nueva cuenta para el cliente
        String numberRandom;
        do {
            Random random = new Random();
            numberRandom = getNumberRandom(random);
        } while (accountService.getAccountByNumber(numberRandom) != null);

        Account account = new Account(numberRandom, LocalDate.now(), 0.0,false, AccountType.CURRENT);
        client.addAccount(account);
        accountService.saveAccount(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }



//   @RequestMapping(path = "/clients", method = RequestMethod.POST)
//    public ResponseEntity<Object> register(
//            @RequestParam String firstName, @RequestParam String lastName,
//            @RequestParam String email, @RequestParam String password){
//        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
//            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
//        }
//        if (clientRepository.findByEmail(email) != null){
//            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
//        }
//        clientRepository.save(new Client(firstName,lastName,email,passwordEncoder.encode(password)));
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }
    @GetMapping("/clients/current")
    public ClientDTO getAuthClient(Authentication authentication){
        return new ClientDTO(clientService.findByEmail(authentication.getName()));
    }



}