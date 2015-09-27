import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Created by mrasquinha on 9/20/15.
 */
public class Percolation {

    private WeightedQuickUnionUF wuf;
    private WeightedQuickUnionUF wufFull;
    private int siteDepth;

    // create virtual nodes that connect the top row; similarly bottom row
    private int virtTop;
    private int virtBottom;

    // mask for each site in the grid
    private boolean[][] sites;

    // prevent backwash
    private boolean hasPercolated;

    /**
     * create N-by-N grid, with all sites blocked
     * @param gridSize      dimension of the percolation grid
     * @throws              IllegalArgumentException if N<=0
     */
    public Percolation(int gridSize) {

        if (gridSize <= 0)
            throw new java.lang.IllegalArgumentException(
                    "Incorrect size for percolation grid" + gridSize);

        this.siteDepth = gridSize;
        this.virtTop = 0;
        this.virtBottom = gridSize * gridSize + 1;
        this.hasPercolated = false;

        // Adding 2 for the virtual nodes
        wuf = new WeightedQuickUnionUF(this.siteDepth * this.siteDepth + 2);
        // Adding 1 for the top virtual node
        wufFull = new WeightedQuickUnionUF(this.siteDepth * this.siteDepth + 1);

        sites = new boolean[siteDepth+1][siteDepth+1];

        for (int i = 0; i <= siteDepth; i++)
            for (int j = 0; j <= siteDepth; j++)
                sites[i][j] = false;
    }

    /**
     * open site (row i, column j) if it is not open already
     * @param i     open site at ith row in grid
     * @param j     open site at jth column in grid
     * @throws      IndexOutOfBoundsException
     */
    public void open(int i, int j) {
        if (isOpen(i, j))
            return;

        int element = ((i-1)*siteDepth) + (j-1) + 1;

        if (i == 1) {
            // connect the top row to its virtual node
            wuf.union(virtTop, element);
            wufFull.union(virtTop, element);
        }

        if (i == siteDepth) {
            // connect the bottom row to its virtual node
            wuf.union(element, virtBottom);
        }

        // connect neighbors if they are open
        if ((i != 1) && isOpen(i - 1, j)) {
            wuf.union(element, element - siteDepth);
            wufFull.union(element, element - siteDepth);
        }

        if ((i != siteDepth) && isOpen(i + 1, j)) {
            wuf.union(element, element + siteDepth);
            wufFull.union(element, element + siteDepth);
        }
       
        if ((j != 1) && isOpen(i, j - 1)) {
            wuf.union(element, element - 1);
            wufFull.union(element, element - 1);
        }

        if ((j != siteDepth) && isOpen(i, j + 1)) {
            wuf.union(element, element + 1);
            wufFull.union(element, element + 1);
        }

        // Open the current site
        sites[i][j] = true;

        if (!hasPercolated && wuf.find(element) == wuf.find(virtTop))
            wufFull.union(virtTop, element);

    }

    /**
     * is site (row i, column j) open?
     * @param i     row number in grid
     * @param j     column number in grid
     * @return      true if site is open or full; false otherwise
     * @throws      IndexOutOfBoundsException
     */
    public boolean isOpen(int i, int j) {
        if (i <= 0 || i > siteDepth || j <= 0 || j > siteDepth)
            throw new IndexOutOfBoundsException();

        if (sites[i][j])
            return true;
        
        return false;
    }

    /**
     * is site (row i, column j) full?
     * @param i     row number in grid
     * @param j     column number in grid
     * @return      true if site is full; false otherwise
     * @throws      IndexOutOfBoundsException
     */
    public boolean isFull(int i, int j) {
        if (i <= 0 || i > siteDepth || j <= 0 || j > siteDepth)
            throw new IndexOutOfBoundsException();

        int element = ((i-1)*siteDepth) + (j-1) + 1;

        if (wufFull.connected(element, virtTop))
            return true;

        return false;
    }

    /**
     * does the system percolate ?
     * @return true is there is a path from row0 to rowN-1
     */
    public boolean percolates() {
        if (wuf.connected(virtTop, virtBottom)) {
            hasPercolated = true;
            return true;
        }
        
        return false;
    }

    /**
     * test client
     */
    public static void main(String[] args) {
        
        boolean useInputTxt = true; // using this with the input test files provided

        int numberElem = StdIn.readInt();
        Percolation p = new Percolation(numberElem);
        int iteration = 0;

        while (!p.percolates() || (useInputTxt && !StdIn.isEmpty())) {
            //array indices are from 1 to grid_size inclusive
            int randi = StdRandom.uniform(1, numberElem+1);
            int randj = StdRandom.uniform(1, numberElem+1);

            if (useInputTxt && StdIn.isEmpty())
                break;

            if (useInputTxt) {
                randi = StdIn.readInt();
                randj = StdIn.readInt();
            }

            p.open(randi, randj);
            iteration++;
        }

        if (p.percolates())
            System.out.println("For iteration=" + iteration + " prob: " +
                    iteration * 1.0 / (numberElem * numberElem));
        else
            System.out.println("Does not percolate");
    }

}
