package org.example.benchmark;

import org.example.core.BSTree;
import org.example.core.RBTree;

import java.util.Random;
import java.util.SortedMap;

public class BenchmarkRunner {
    private static final int WARMUP_RUNS = 3;
    private static final int MEASURE_RUNS = 5;
    private static final int TOTAL_RUNS = WARMUP_RUNS + MEASURE_RUNS;
    private static final QuickSort quickSort = new QuickSort();
    public static void main(String[] args){
        System.out.println("---Starting Benchmarking!---");
        BSTree.VALIDATE = false;
        int n = 100000;
        System.out.println("\nGenerating Data...");
        int[] randomData = InputGenerator.generatFullyRandom(n);
        int[] nearlySorted1 = InputGenerator.generateNearlySorted(n, 1);
        int[] nearlySorted5 = InputGenerator.generateNearlySorted(n, 5);
        int[] nearlySorted10 = InputGenerator.generateNearlySorted(n, 10);

        System.out.println("---Running: Fully Random Data...");
        runSuiteForDistribution(randomData, "Fully Random");

        System.out.println("---Running: Disorder 1% Random Data...");
        runSuiteForDistribution(nearlySorted1, "1% Disorder");

        System.out.println("---Running: Disorder 5% Random Data...");
        runSuiteForDistribution(nearlySorted5, "5% Disorder");

        System.out.println("---Running: Disorder 10% Random Data...");
        runSuiteForDistribution(nearlySorted10, "10% Disorder");

    }
    public static void runSuiteForDistribution(int[] data, String distName){
        long[] bstInsertTime = new long[MEASURE_RUNS];
        long[] rbInsertTime = new long[MEASURE_RUNS];

        long[] bsDeleteTime = new long[MEASURE_RUNS];
        long[] rbDeleteTime = new long[MEASURE_RUNS];

        long[] bsSearchTime = new long[MEASURE_RUNS];
        long[] rbSearchTime = new long[MEASURE_RUNS];

        long[] bsSortTime = new long[MEASURE_RUNS];
        long[] rbSortTime = new long[MEASURE_RUNS];
        long[] quisckSortTime = new long[MEASURE_RUNS];
        int bsHeight = 0;
        int rbHeight = 0;
        int bsHeight1 = 0;
        int rbHeight1 = 0;
        int n = data.length;
        int[] searchData = new int[n];
        int dummy = 0;
        System.arraycopy(data, 0, searchData, 0, n/2);
        //fake data
        for (int i = n/2; i < n; i++) {
            searchData[i] = (10 * n) + i;
        }

        int deleteCount = (int) (n * 0.2);
        int[] deletData = new int[deleteCount];
        Random rand = new Random(42);
        for (int i = 0; i < deleteCount; i++) {
            deletData[i] = data[rand.nextInt(n)];
        }
        for (int i = 0; i < TOTAL_RUNS; i++) {
            System.gc();
            if (i < WARMUP_RUNS){
                System.out.println("Warmup" + (i + 1) + "...");
            }
            BSTree bsTree = new BSTree();
            RBTree rbTree = new RBTree();
            long bsInsertS = System.nanoTime();
            for (int val : data){
                bsTree.insert(val);
            }
            long bsInsertE = System.nanoTime();
            bsTree.inOrder();
            long bsSortE = System.nanoTime();

            long rbInsertS = System.nanoTime();
            for (int val : data){
                rbTree.insert(val);
            }
            long rbInsertE = System.nanoTime();
            rbTree.inOrder();
            long rbSortE = System.nanoTime();
            long quickSortTime = quickSort.sort(data);
            if (i == TOTAL_RUNS - 1){
                bsHeight = bsTree.height();
                rbHeight = rbTree.height();
            }
            long bsSearchS = System.nanoTime();
            for (int val : searchData){
                if (bsTree.contains(val)){
                    dummy++;
                }
            }
            long bsSearchE = System.nanoTime();

            long rbSearchS = System.nanoTime();
            for (int val : searchData){
                if (rbTree.contains(val)){
                    dummy++;
                }
            }
            long rbSearchE = System.nanoTime();

            long bsDeleteS = System.nanoTime();
            for (int val : deletData){
                bsTree.delete(val);
            }
            long bsDeleteE = System.nanoTime();

            long rbDeleteS = System.nanoTime();
            for (int val : deletData){
                rbTree.delete(val);
            }
            long rbDeleteE = System.nanoTime();
            if (i == TOTAL_RUNS - 1){
                bsHeight1 = bsTree.height();
                rbHeight1 = rbTree.height();
            }
            if (i >= WARMUP_RUNS){
                int idx = i - WARMUP_RUNS;
                bstInsertTime[idx] = bsInsertE - bsInsertS;
                rbInsertTime[idx]= rbInsertE - rbInsertS;
                bsSortTime[idx] = bsSortE - bsInsertS;
                rbSortTime[idx] = rbSortE - rbInsertS;
                quisckSortTime[idx] = quickSortTime;
                bsSearchTime[idx] = bsSearchE - bsSearchS;
                rbSearchTime[idx] = rbSearchE - rbSearchS;
                bsDeleteTime[idx] = bsDeleteE - bsDeleteS;
                rbDeleteTime[idx] = rbDeleteE - rbDeleteS;
            }
        }
        System.out.println("\n" + distName + " Results: ");
        BenchmarkMetrics bsMetrics = new BenchmarkMetrics(bstInsertTime);
        BenchmarkMetrics rbMetrics = new BenchmarkMetrics(rbInsertTime);
        bsMetrics.printReport("Insert 100k", "BST");
        rbMetrics.printReport("Insert 100k", "RBT");
        double insertSpeedUp = bsMetrics.getMean() / rbMetrics.getMean();
        System.out.printf("Insert SpeedUp: %.2fx\n", insertSpeedUp);
        System.out.println("BST Height: " + bsHeight);
        System.out.println("RBT Height: " + rbHeight);


        bsMetrics = new BenchmarkMetrics(bsSortTime);
        rbMetrics = new BenchmarkMetrics(rbSortTime);
        BenchmarkMetrics qsMetrics = new BenchmarkMetrics(quisckSortTime);
        bsMetrics.printReport("Sort", "BST");
        rbMetrics.printReport("Sort", "RBT");
        qsMetrics.printReport("Sort", "QuickSort");
        double sortSpeedUp = bsMetrics.getMean() / rbMetrics.getMean();
        System.out.printf("sort SpeedUp: %.2fx\n", sortSpeedUp);

        double qsVsBst = bsMetrics.getMean() / qsMetrics.getMean();
        double qsVsRbt = rbMetrics.getMean() / qsMetrics.getMean();
        System.out.printf("QuickSort SpeedUp over BST: %.2fx\n", qsVsBst);
        System.out.printf("QuickSort SpeedUp over RBT: %.2fx\n", qsVsRbt);
        System.out.println();

        bsMetrics = new BenchmarkMetrics(bsSearchTime);
        rbMetrics = new BenchmarkMetrics(rbSearchTime);
        bsMetrics.printReport("Search", "BST");
        rbMetrics.printReport("Search", "RBT");
        double searchSpeedUp = bsMetrics.getMean() / rbMetrics.getMean();
        System.out.printf("Search SpeedUp: %.2fx\n", searchSpeedUp);
        bsMetrics = new BenchmarkMetrics(bsDeleteTime);
        rbMetrics = new BenchmarkMetrics(rbDeleteTime);
        bsMetrics.printReport("Delete", "BST");
        rbMetrics.printReport("Delete", "RBT");
        double deleteSpeedUp = bsMetrics.getMean() / rbMetrics.getMean();
        System.out.printf("Delete SpeedUp: %.2fx\n", deleteSpeedUp);
        System.out.println("BST Height: " + bsHeight1);
        System.out.println("RBT Height: " + rbHeight1);
        System.out.println("\n----------------------------------------\n");
    }
}
