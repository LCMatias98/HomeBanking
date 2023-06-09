package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Enums.TransactionType;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository repository, AccountRepository repository2, TransactionRepository transactionRepository) {
		return (args) -> {
			LocalDate today =  LocalDate.now();
			LocalDate tomorrow = today.plusDays(1);

			Client client1 = new Client("Lucas","Correa","correalucas@hotmail.com.ar");
			Client client2 = new Client("Mauri","Echaniz","mauriechaniz@gmail.com");

			Account account1 = new Account("VIN001",today,5000.0,client1);
			Account account2 = new Account("VIN002",tomorrow,7500.0,client1);
			Account account3 = new Account("VIN003",today,7000.0,client2);
			Account account4 = new Account("VIN004",tomorrow,9000.0,client2);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 5000.00,"Buyed",today,account1);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -15000.00,"Buyed",today,account1);

			repository.save(client1);
			repository.save(client2);
			repository2.save(account1);
			repository2.save(account2);
			repository2.save(account3);
			repository2.save(account4);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
		};
	}

}
