import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import static java.lang.System.exit;

/**
 * Created by mrasquinha on 9/20/15.
 */
public class Percolation {

    WeightedQuickUnionUF wuf;
    private int site_length;
    private int virt_top;
    private int virt_bottom;
    private boolean[][] sites;

    /** create N-by-N grid, with all sites blocked
     * @param N: dimension of the percolation grid
     * @throws IllegalArgumentException if N<=0
     * @throws OutOfMemoryError if NxN array cannot be allocated
     */
    public Percolation(int N) {
        try {
            if (N <= 0) throw new java.lang.IllegalArgumentException();
            this.site_length = N;
            this.virt_top = 0;
            this.virt_bottom = N*N+1;

            //Adding 2 for the virtual nodes
            wuf = new WeightedQuickUnionUF(this.site_length*this.site_length+2);
            print_wuf();

            sites = new boolean[site_length][site_length];

            for (int i=0; i<site_length; i++)
                for (int j=0; j<site_length; j++)
                    sites[i][j] = false;
        }
        catch (java.lang.IllegalArgumentException e) {
            System.out.println("Incorrect size for percolation grid"+N);
            System.out.println(e);
        }
        catch (java.lang.OutOfMemoryError e) {
            System.out.println("Grid of size "+ N +"cannot be allocated");
            System.out.println(e);
        }
    }

    private void print_wuf() {
        /*
        System.out.println("virt= "+wuf.find(0)+"/"+wuf.find(site_length*site_length+1));
        for (int i=0; i<site_length; i++) {
            for (int j = 0; j < site_length; j++)
                System.out.print("\t" + wuf.find(i * site_length + j + 1));
            System.out.println("");
        }
        */
    }
    /** open site (row i, column j) if it is not open already
     * @param i open site at ith row in grid
     * @param j open site at jth column in grid
     * @throws IndexOutOfBoundsException
     */
    public void open(int i, int j) {
        try {
            if (i < 0 || i >= site_length || j < 0 || j >= site_length) throw new IndexOutOfBoundsException();

            int element = (i * site_length) + j + 1;

            if (sites[i][j])
                return;  //element already open

            if (i == 0) {
                //connect the top row to its virtual node
                wuf.union(virt_top, element);
            } else if (i == (site_length - 1)) {
                //connect the bottom row to its virtual node
                wuf.union(element, virt_bottom);
            }

            sites[i][j] = true;
            // connect neighbours
            if (i != 0 && sites[i-1][j]) {
                wuf.union(element, element - site_length);
                //sites[i-1][j] = true;
            }
            if (i != site_length - 1 && sites[i+1][j]) {
                wuf.union(element, element + site_length);
                //sites[i+1][j] = true;
            }
            if ((j != 0) && sites[i][j-1]) {
                wuf.union(element, element - 1);
                //sites[i][j-1] = true;
            }
            if (j != site_length - 1 && sites[i][j+1]) {
                wuf.union(element, element + 1);
                //sites[i][j+1] = true;
            }

        } catch (IndexOutOfBoundsException e) {
            System.out.println(e);
            exit(1);
        }

    }

    /** is site (row i, column j) open?
     * @param i row number in grid
     * @param j column number in grid
     * @return
     * @throws IndexOutOfBoundsException
     */
    public boolean isOpen(int i, int j) {
        if (isFull(i,j))
            return false;
        else
            return true;
    }

    /** is site (row i, column j) full?
     *
     * @param i row number in grid
     * @param j column number in grid
     * @return
     * @throws IndexOutOfBoundsException
     */
    public boolean isFull(int i, int j) {
        if (i < 0 || i >= site_length || j < 0 || j >= site_length) throw new IndexOutOfBoundsException();

        if (!sites[i][j])
            return true;
        else
            return false;
    }

    /** does the system percolate
     *
     * @return
     */
    public boolean percolates() {
        if(wuf.connected(virt_top,virt_bottom))
            return true;
        else
            return false;
    }

    /** test client
     *
     * @param args: Size of the percolation grid
     */
    public static void main(String[] args) {
        int N = 200; //StdIn.readInt();
        Percolation p = new Percolation(N);
        int iter = 0;
        while(!p.percolates()) {
            int randi = StdRandom.uniform(N);
            int randj = StdRandom.uniform(N);
            if(p.isOpen(randi,randj)) continue;
            System.out.println("open i=" + randi +
                    " j=" + randj+ " ");
            p.open(randi, randj);
            p.print_wuf();
            iter++;
        }
        System.out.println("For iter=" + iter + " prob: " + iter*1.0/(N*N));
    }
}
