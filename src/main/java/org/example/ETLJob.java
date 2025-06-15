package org.example;

import java.util.concurrent.ThreadLocalRandom;

public class ETLJob {
    private ETLJob() {
        // Private constructor to prevent instantiation
    }

    public static void run() {
        System.out.println("Starting ETL Job...");

        String data = extractData();
        System.out.println("Data extracted: " + data);

        String transformedData = transformData(data);
        System.out.println("Data transformed: " + transformedData);

        loadData(transformedData);
        System.out.println("Data loaded successfully.");

        System.out.println("ETL Job completed.");
    }

    private static int getRandomInt(int max) {
        if (max < 1) {
            throw new IllegalArgumentException("Max must be greater than or equal to 1");
        }
        return ThreadLocalRandom.current().nextInt(max - 1) + 1;
    }

    private static void sleepRandomSeconds(int max) {
        try {
            Thread.sleep(getRandomInt(max) *  1000L);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }

    }

    private static String extractData() {
        sleepRandomSeconds(3);
        return "Sample Data";
    }

    private static String transformData(String data) {
        sleepRandomSeconds(2);
        return data.toUpperCase();
    }

    private static void loadData(String data) {
        sleepRandomSeconds(3);
        System.out.println("Loading data: " + data);
    }
}
