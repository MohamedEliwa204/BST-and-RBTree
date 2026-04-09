package org.example.benchmark;

import java.util.concurrent.ThreadLocalRandom;

public class QuickSort {


    public String getName() {
        return "Quick Sort";
    }


    public long sort(int[] arr) {
        int[] array = arr.clone();

        long startTime = System.nanoTime();
        quickSort(array, 0, array.length - 1);
        long endTime = System.nanoTime();

        return endTime - startTime;
    }

    private void quickSort(int[] arr, int start, int end){
        if (end <= start){
            return;
        }
        int pi = partition(arr, start, end);

        quickSort(arr, start, pi - 1);
        quickSort(arr, pi + 1, end);

    }

    private int partition(int[] array, int start, int end){
        int randomIndex = ThreadLocalRandom.current().nextInt(start, end + 1);
        swap(array, randomIndex, end);
        int pivot = array[end];
        int i = start - 1;
        for (int j = start; j < end; j++) {

            if (array[j] < pivot) {
                i++;
                swap(array, i, j);

            }
        }
        swap(array, i + 1, end);
        return i + 1;
    }

    private void swap(int[] array, int i, int j){
        if (i == j) return;

        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
