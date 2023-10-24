package Main;

import java.util.*;

class Main {
  public static void main(String[] args) {

    /*
     * Take in and parse the input, which consists of:
     * - n, the number of rows and columns in the grid
     * - p, the probability threshold that a node is a customer
     * - m, the number of avaiable UAVs
     */
    Scanner scanner = new Scanner(System.in);
    int n = scanner.nextInt();
    Random random = new Random();

    GridGraph grid = new GridGraph(n);

    double p = 0.5; // probability threshold that node is a customer
    
    // generate random customers
    List<int[]> customers = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        double r = random.nextDouble();
        if (r < p) {
          int[] pair = { i, j };
          customers.add(pair);
        }
      }
    }
    
    // generate random intervals
    List<Interval> intervals = new ArrayList<Interval>();
    for (int[] c : customers) {
      int row = c[0];
      int col = c[1];
      int numPrevTruckVisits = col % 2 == 1 ? col * n + row + 1 : col * n + n - row - 1;
      int i = (int) (random.nextDouble() * numPrevTruckVisits);
      int j = col % 2 == 1 ? col * n + n - 1 - row : row;
      int k = (int) (random.nextDouble() * (n * n - 1 - numPrevTruckVisits)) + numPrevTruckVisits + 1;
      intervals.add(new Interval(i, j, k));
    }

    // construct the interval graph
    IntervalGraph intervalGraph = new IntervalGraph();
    for (Interval interval : intervals) {
      intervalGraph.addInterval(interval);
    }

    scanner.close();
  }
}