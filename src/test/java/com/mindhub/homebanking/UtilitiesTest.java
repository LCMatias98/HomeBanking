package com.mindhub.homebanking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static com.mindhub.homebanking.utils.Utilities.getCvv;

@SpringBootTest
public class UtilitiesTest {

    @Test
    public void checkRandomCVV(){
        Random random = new Random();
        int randomNumber = random.nextInt(899) + 100;

        assertThat(String.valueOf(randomNumber).length(), is(3));
    }


    @Test
    public void checkRandomCardNumber(){
        Random random = new Random();
        int randomNumber = random.nextInt(8999)+1000;

        String numberCard = randomNumber +"-"+randomNumber +"-"+randomNumber +"-"+randomNumber;

        assertThat(String.valueOf(numberCard).length(), is(19));
    }


    @Test
    public void getAccountNumberRandom(){
        Random random = new Random();
        int randomAccount = random.nextInt(89999999)+10000000;
        String randomAccountNumber = "VIN-" + randomAccount;
        assertThat(String.valueOf(randomAccountNumber).length(), is(12));
    }
}
