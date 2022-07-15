/*
To estimate the percolation threshold, consider
the following computational experiment:

Initialize all sites to be blocked.
Repeat the following until the system percolates:
Choose a site uniformly at random among all blocked sites.
Open the site.
The fraction of sites that are opened when the system percolates
provides an estimate of the percolation threshold.
 */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    // instance variables
    private double T;
    private double[] values;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("Input out of range");
        }
        if (trials <= 0) {
            throw new IllegalArgumentException("Input out of range");
        }

        values = new double[trials];
        T = trials;

        for (int i = 0; i < trials; i++) {
            Percolation grid = new Percolation(n);
            while (grid.percolates() == false) {
                int randomOne = StdRandom.uniform(0, n);
                int randomTwo = StdRandom.uniform(0, n);
                grid.open(randomOne, randomTwo);
                // StdOut.println("random = " + one + " " + two);
            }
            values[i] = (grid.numberOfOpenSites()) / (Math.pow(n, 2));
            // StdOut.println("value = " + values[i]);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(values);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(values);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        double average = mean();
        double low = average - ((1.96) * stddev() / (Math.sqrt(T)));
        return low;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        double average = mean();
        double high = average + ((1.96) * stddev() / (Math.sqrt(T)));
        return high;
    }

    // test client (see below)
    public static void main(String[] args) {
        Stopwatch he = new Stopwatch();
        PercolationStats my = new PercolationStats(Integer.parseInt(args[0]),
                                                   Integer.parseInt(args[1]));
        StdOut.println("mean() = " + String.format("%.6f", my.mean()));
        StdOut.println("stddev() = " + String.format("%.6f", my.stddev()));
        StdOut.println("confidenceLow() = " + String.format("%.6f", my.confidenceLow()));
        StdOut.println("confidenceHigh() = " + String.format("%.6f", my.confidenceHigh()));
        StdOut.println("elapsed time = " + String.format("%.3f", he.elapsedTime()));
    }
}
