import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Created by mrasquinha on 9/20/15.
 */
public class Percolation {

    private enum SiteState {
        SB, SO, SF 
    }

    private WeightedQuickUnionUF wuf;
    private int siteDepth;

    // create virtual nodes that connect the top row; similarly bottom row
    private int virtTop;
    private int virtBottom;

    // mask for each site in the grid
    private SiteState[][] sites;

    /**
     * create N-by-N grid, with all sites blocked
     * 
     * @param N
     *            : dimension of the percolation grid
     * @throws IllegalArgumentException
     *             if N<=0
     * @throws OutOfMemoryError
     *             if NxN array cannot be allocated
     */
    public Percolation(int N) {

        if (N <= 0)
            throw new java.lang.IllegalArgumentException(
                    "Incorrect size for percolation grid" + N);

        this.siteDepth = N;
        this.virtTop = 0;
        this.virtBottom = N * N + 1;
        // Adding 2 for the virtual nodes
        wuf = new WeightedQuickUnionUF(this.siteDepth * this.siteDepth + 2);

        sites = new SiteState[siteDepth+1][siteDepth+1];

        for (int i = 0; i <= siteDepth; i++)
            for (int j = 0; j <= siteDepth; j++)
                sites[i][j] = SiteState.SB;
    }

    /**
     * open site (row i, column j) if it is not open already
     * 
     * @param i
     *            open site at ith row in grid
     * @param j
     *            open site at jth column in grid
     * @throws IndexOutOfBoundsException
     */
    public void open(int i, int j) {
        if (isOpen(i, j))
            return;

        int element = ((i-1) * siteDepth) + (j-1) + 1;
        sites[i][j] = SiteState.SO;

        if (i == 1) {
            // connect the top row to its virtual node
            wuf.union(virtTop, element);
            //sites[i][j] = SiteState.SF;
        } 
        if (i == siteDepth) {
            // connect the bottom row to its virtual node
            wuf.union(element, virtBottom);
        }

        // connect neighbors if they are open
        if ((i != 1) && isOpen(i - 1, j))
            wuf.union(element, element - siteDepth);

        if ((i != siteDepth) && isOpen(i + 1, j))
            wuf.union(element, element + siteDepth);
       

        if ((j != 1) && isOpen(i, j - 1))
            wuf.union(element, element - 1);

        if ((j != siteDepth) && isOpen(i, j + 1))
            wuf.union(element, element + 1);

        if (wuf.find(element) == wuf.find(virtTop))
            findFullSites(i, j);
    }

    private void findFullSites(int xdim, int ydim) {
        if (sites[xdim][ydim] != SiteState.SO)
            return;
        int element = ((xdim-1) * siteDepth) + (ydim-1) + 1;
        if (wuf.find(element) == wuf.find(virtTop))
            sites[xdim][ydim] = SiteState.SF;
            
        if (xdim != 1)
            findFullSites(xdim-1, ydim);
        if (xdim < siteDepth)
            findFullSites(xdim+1, ydim);
        if (ydim != 1)
            findFullSites(xdim, ydim-1);
        if (ydim < siteDepth)
            findFullSites(xdim, ydim+1);
        
    }
    /**
     * is site (row i, column j) open?
     * 
     * @param i
     *            row number in grid
     * @param j
     *            column number in grid
     * @return
     * @throws IndexOutOfBoundsException
     */
    public boolean isOpen(int i, int j) {
        if (i <= 0 || i > siteDepth || j <= 0 || j > siteDepth)
            throw new IndexOutOfBoundsException();

        if (sites[i][j] == SiteState.SO || sites[i][j] == SiteState.SF)
            return true;
        
        return false;
    }

    /**
     * is site (row i, column j) full?
     * 
     * @param i
     *            row number in grid
     * @param j
     *            column number in grid
     * @return
     * @throws IndexOutOfBoundsException
     */
    public boolean isFull(int i, int j) {

        if (i <= 0 || i > siteDepth || j <= 0 || j > siteDepth)
            throw new IndexOutOfBoundsException();
        
        if (sites[i][j] == SiteState.SF)
            return true;

        return false;
    }

    /**
     * does the system percolate
     * 
     * @return true is there is a path from row0 to rowN-1
     */
    public boolean percolates() {
        if (wuf.connected(virtTop, virtBottom)) {
            return true;
        }
        
        return false;
    }

    /**
     * test client
     * 
     * @param args
     *            : Size of the percolation grid
     */
    public static void main(String[] args) {
        
        boolean use_input_txt = false; // using this with the input test files
                                       // provided

        int N = StdIn.readInt();
        Percolation p = new Percolation(N);
        int iter = 0;

        while (!p.percolates() || (use_input_txt && !StdIn.isEmpty())) { 
            //array indices are from 1 to grid_size inclusive
            int randi = StdRandom.uniform(1, N+1);
            int randj = StdRandom.uniform(1, N+1);

            if (use_input_txt && StdIn.isEmpty())
                break;

            if (use_input_txt) {
                randi = StdIn.readInt();
                randj = StdIn.readInt();
            }

            if (p.isOpen(randi, randj))
                continue;

            p.open(randi, randj);
            iter++;
        }

        if (p.percolates())
            System.out.println("For iter=" + iter + " prob: " + iter * 1.0
                    / (N * N));
        else
            System.out.println("Does not percolate");
    }

}
