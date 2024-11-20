package io.giovannymassuia.disecomm.ordermanagement.utils;

import io.giovannymassuia.disecomm.ordermanagement.grpc.ProductAvailabilityGrpcService;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChaosUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChaosUtils.class);
    private static final Random RANDOM = new Random();

    /**
     * Adds a random delay between 100 and 2000 milliseconds.
     */
    public static void addRandomDelay() {
        try {
            long delay = 100 + (long) (RANDOM.nextDouble() * 1900);
            System.out.println("Adding random delay: " + delay + " ms");
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    /**
     * Performs a random Fibonacci calculation between 1 and 50.
     */
    public static void stressCPUWithFibonacci() {
        int n = 1 + RANDOM.nextInt(50); // Random Fibonacci input between 1 and 50
        LOGGER.info("Calculating Fibonacci for n = {}", n);
        long result = fibonacci(n);
        LOGGER.info("Fibonacci result for n = {} is {}", n, result);
    }

    /**
     * Stresses memory by allocating a random-sized array and holding it temporarily.
     */
    public static void stressMemory() {
        int randomLargeArraySize = 1 + RANDOM.nextInt(1_000_000); // Array size between 1 and 1,000,000
        LOGGER.info("Allocating memory with an array of size: {}", randomLargeArraySize);
        int[] largeArray = new int[randomLargeArraySize];
        for (int i = 0; i < randomLargeArraySize; i++) {
            largeArray[i] = i; // Populate the array to ensure memory is used
        }
        LOGGER.info("Memory stress complete");
    }

    /**
     * Runs all chaos simulations: delay, CPU stress, and memory stress.
     */
    public static void applyChaos(boolean randomize) {
        // Randomly proceed with chaos simulation
        if (randomize && RANDOM.nextBoolean()) {
            System.out.println("Skipping chaos simulation...");
            return;
        }

        System.out.println("Applying chaos...");
        addRandomDelay();
        stressCPUWithFibonacci();
        stressMemory();
        System.out.println("Chaos applied!");
    }

    /**
     * Helper method to calculate Fibonacci recursively.
     */
    private static long fibonacci(int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
}
