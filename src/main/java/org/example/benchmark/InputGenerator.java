package org.example.benchmark;

import java.util.Random;

public class InputGenerator {
    private static final long SEED = 42L;
    private static final Random random = new Random(SEED);

    public static int[] generatFullyRandom(int n) {
        int[] arr = new int[n];
        int maxRange = 10 * n;
        for (int i = 0; i < n; i++) {
            arr[i] = random.nextInt(maxRange + 1);
        }
        return arr;
    }


    public static int[] generateNearlySorted(int n, int x) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }

        int numberOfSwaps = (int) (n * (x / 100.0));
        for (int i = 0; i < numberOfSwaps; i++) {
            int idx1 = random.nextInt(n);
            int idx2 = random.nextInt(n);

            int temp = arr[idx1];
            arr[idx1] = arr[idx2];
            arr[idx2] = temp;
        }
        return arr;
    }
}
