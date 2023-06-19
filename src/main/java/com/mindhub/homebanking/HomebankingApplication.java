package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.models.Enums.TransactionType;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository) {
		return (args) -> {
			LocalDate today =  LocalDate.now();
			LocalDate tomorrow = today.plusDays(1);
			LocalDateTime todayTime = LocalDateTime.now();
			LocalDateTime tomorrowTime = todayTime.plusDays(1);

			Client client1 = new Client("Lucas","Correa","correalucas@hotmail.com.ar");
			Client client2 = new Client("Mauri","Echaniz","mauriechaniz@gmail.com");
			clientRepository.save(client1);
			clientRepository.save(client2);

			Account account1 = new Account("VIN001",today,5000.0);
			Account account2 = new Account("VIN002",tomorrow,7500.0);
			Account account3 = new Account("VIN003",today,7000.0);
			Account account4 = new Account("VIN004",tomorrow,9000.0);
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client2.addAccount(account4);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 5000.00,"Buyed",todayTime);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -15000.00,"Buyed",tomorrowTime);

			account1.addTransaction(transaction1);
			account2.addTransaction(transaction2);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);

			List<Integer> list1 = List.of(12,24,36,48,60);
			Loan loan1 = new Loan("Hipotecario",500000.00, list1);
			loanRepository.save(loan1);

			List<Integer> list2 = List.of(6,12,24);
			Loan loan2 = new Loan("Personal",100000.00, list2);
			loanRepository.save(loan2);

			List<Integer> list3 = List.of(6,12,24,36);
			Loan loan3 = new Loan("Automotriz",300000.00, list3);
			loanRepository.save(loan3);


			ClientLoan clientLoan1 = new ClientLoan(400000.00,60);
			client1.addClientLoan(clientLoan1);
			loan1.addClientLoan(clientLoan1);
			clientLoanRepository.save(clientLoan1);
		};
	}

}
