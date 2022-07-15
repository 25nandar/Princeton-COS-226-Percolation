/*
 We model a percolation system using an n-by-n grid of sites. Each site is
 either open or blocked. A full site is an open site that can be connected to
 an open site in the top row via a chain of neighboring (left, right, up, down)
 open sites. We say the system percolates if there is a full site in the bottom
 row. In other words, a system percolates if we fill all open sites connected
 to the top row and that process fills some open site on the bottom row.
 (For the insulating/metallic materials example, the open sites correspond to
 metallic materials, so that a system that percolates has a metallic path from
 top to bottom, with full sites conducting. For the porous substance example,
 the open sites correspond to empty space through which water might flow, so
 that a system that percolates lets water fill open sites, flowing from top to
 bottom.)
*/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // instance variables
    private boolean[][] open;
    private int counter;
    private WeightedQuickUnionUF weighted;
    private WeightedQuickUnionUF backwash;
    private int number;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {

        if (n <= 0) {
            throw new IllegalArgumentException("Input out of range");
        }

        weighted = new WeightedQuickUnionUF((int) Math.pow(n, 2) + 2);
        backwash = new WeightedQuickUnionUF((int) Math.pow(n, 2) + 2);

        open = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                open[i][j] = false;
            }
        }

        number = n;
        counter = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {

        if (open[row][col]) {
            return;
        }

        if (row < 0) {
            throw new IllegalArgumentException("Input out of range");
        }
        if (row > number - 1) {
            throw new IllegalArgumentException("Input out of range");
        }
        if (col < 0) {
            throw new IllegalArgumentException("Input out of range");
        }
        if (col > number - 1) {
            throw new IllegalArgumentException("Input out of range");
        }

        open[row][col] = true;
        counter = counter + 1;

        if (row == 0) {
            if (open[row][col]) {
                weighted.union(row * number + col, (int) Math.pow(number, 2));
                backwash.union(row * number + col, (int) Math.pow(number, 2));
            }
        }

        if (row == number - 1) {
            if (open[row][col]) {
                weighted.union(row * number + col, (int) Math.pow(number, 2) + 1);
            }
        }

        if (row < number - 1) {
            if (open[row + 1][col]) {
                weighted.union(row * number + col, (row + 1) * number + col);
                if (row != number) {
                    backwash.union(row * number + col, (row + 1) * number + col);
                }
            }
        }

        if (row > 0) {
            if (open[row - 1][col]) {
                weighted.union(row * number + col, (row - 1) * number + col);
                backwash.union(row * number + col, (row - 1) * number + col);
            }
        }

        if (col < number - 1) {
            if (open[row][col + 1]) {
                weighted.union(row * number + col, row * number + col + 1);
                backwash.union(row * number + col, row * number + col + 1);
            }
        }
        if (col > 0) {
            if (open[row][col - 1]) {
                weighted.union(row * number + col, row * number + col - 1);
                backwash.union(row * number + col, row * number + col - 1);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return open[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (open[row][col]) {
            if (backwash.find(row * number + col) == backwash.find((int) Math.pow(number, 2))) {
                return true;
            }
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return counter;
    }

    // does the system percolate?
    public boolean percolates() {
        if (weighted.find((int) Math.pow(number, 2)) == weighted
                .find((int) Math.pow(number, 2) + 1)) {
            return true;
        }
        return false;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Percolation per = new Percolation(2);
        per.open(0, 0);
        per.open(0, 1);
        per.open(1, 1);
        StdOut.println("(0, 0) open = " + per.isOpen(0, 0));
        StdOut.println("(0, 0) full = " + per.isFull(0, 0));
        StdOut.println("sites open = " + per.numberOfOpenSites());
        StdOut.println("percolates = " + per.percolates());
    }
}
