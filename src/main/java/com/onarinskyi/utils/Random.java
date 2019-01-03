package com.onarinskyi.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

public class Random {

    private static final String AUTO = "AUTO";

    public static String name() {
        return AUTO + RandomStringUtils.randomAlphanumeric(6);
    }

    public static String name(int length) {
        return AUTO + RandomStringUtils.randomAlphanumeric(length);
    }

    public static String alphanumericString() {
        return RandomStringUtils.randomAlphanumeric(6);
    }

    public static String alphanumericString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static Integer integer() {
        return ThreadLocalRandom.current().nextInt();
    }

    public static Integer integer(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    public static Double roundedDouble() {
        Double result = ThreadLocalRandom.current().nextDouble(1, 999_999d);
        return new BigDecimal(result.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
