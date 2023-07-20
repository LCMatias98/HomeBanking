package com.mindhub.homebanking.utils;

import java.util.Random;

public final class Utilities {


    public static short getCvv(Random randomCVV) {
        return (short) randomCVV.nextInt(899);
    }

    public static String getRandomCardNumber(Random randomNumber) {
        return randomNumber.nextInt(9999) + "-" + randomNumber.nextInt(9999) + "-" + randomNumber.nextInt(9999) + "-" + randomNumber.nextInt(9999);
    }

    public static String getNumberRandom(Random random) {
        return "VIN-" + random.nextInt(99999999);
    }
}
