import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int numberIterations;
    private double[] values;

    public PercolationStats(int gridSz, int numIter) {

        if (gridSz <= 0 || numIter <= 0)
            throw new IllegalArgumentException(
                    "Invalid input arguments N=" + gridSz + " T=" + numIter);

        this.numberIterations = numIter;

        values = new double[numIter];

        for (int i = 0; i < numberIterations; i++) {
            int iteration = 0;

            Percolation p = new Percolation(gridSz);
            while (!p.percolates()) {
                int x_idx = StdRandom.uniform(1, gridSz+1);
                int y_idx = StdRandom.uniform(1, gridSz+1);

                p.open(x_idx, y_idx);
                iteration++;
            }
            values[i] = iteration * 1.0 / (gridSz * gridSz);
        }

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

        int numberElem = Integer.parseInt(args[0]);
        int numberIter = Integer.parseInt(args[1]);

        PercolationStats pstats = new PercolationStats(numberElem, numberIter);

        System.out.println("mean                        = " + pstats.mean());
        System.out.println("stddev                      = " + pstats.stddev());
        System.out.println("95% confidence interval     = "
                + (pstats.confidenceHi() + ", " + pstats.confidenceLo()));

    }

}
