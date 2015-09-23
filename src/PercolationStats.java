import java.util.Arrays;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int numberIterations;
    private double[] values;

    public PercolationStats(int N, int T) {

        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException(
                    "Incorrect size for percolation grid" + N);

        this.numberIterations = T;

        values = new double[T];
        Arrays.fill(values, 0);

    }

    public double mean() {
        return StdStats.mean(values);
    }

    public double stddev() {
        return StdStats.stddev(values);
    }

    public double confidenceLo() {
        return (mean() - (1.96 * stddev() / java.lang.Math
                .sqrt(numberIterations)));
    }

    public double confidenceHi() {
        return (mean() + (1.96 * stddev() / java.lang.Math
                .sqrt(numberIterations)));
    }

    public static void main(String[] args) {

        if (args.length != 2)
            throw new IllegalArgumentException();

        int N = StdIn.readInt();
        int T = StdIn.readInt();

        PercolationStats pstats = new PercolationStats(N, T);

        for (int i = 0; i < T; i++) {
            Percolation percolation = new Percolation(N);
            int iter = 0;

            while (!percolation.percolates()) {
                int randi = StdRandom.uniform(1, N+1);
                int randj = StdRandom.uniform(1, N+1);

                if (percolation.isOpen(randi, randj))
                    continue;

                percolation.open(randi, randj);
                iter++;
            }

            pstats.values[i] = iter * 1.0 / (N * N);

        }
        System.out.println("mean\t= " + pstats.mean());
        System.out.println("stddev\t= " + pstats.stddev());
        System.out.println("95% confidence interval\t= "
                + (pstats.confidenceHi() + ", " + pstats.confidenceLo()));

    }

}
