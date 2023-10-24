package Main;

import java.util.*;

class Main {

  public static List<List<Interval>> solveMDSP(int m, int mDelta, int B, List<Interval> intervals,
      IntervalGraph intervalGraph) {
    HashSet<Integer> M = new HashSet<>();
    for (int k = 0; k < mDelta; k++) {
      M.add(k);
    }

    double[][] P = new double[mDelta][2];
    double[] W = new double[mDelta];
    List<List<Interval>> S = new ArrayList<List<Interval>>();
    List<Integer> mPrime = new ArrayList<>(mDelta);

    for (int k = 0; k < mDelta; k++) {
      P[k][0] = k;
    }

    // initialize S for each drone
    for (int k = 0; k < mDelta; k++) {
      S.add(new ArrayList<Interval>());
    }

    // loop through the sorted intervals
    for (int j = 0; j < intervals.size(); j++) {
      if (mPrime.size() < m) {
        boolean foundDrone = false;
        int i = 0;
        // loop through drones to find a drone that can cover this interval
        while (!foundDrone) {
          foundDrone = true;
          for (Interval interval : S.get(i)) {
            if (intervalGraph.isOverlapping(interval, intervals.get(j))) {
              foundDrone = false;
            }
          }
          i += (foundDrone == true) ? 0 : 1;
        }

        W[i] += intervals.get(j).getCost();
        S.get(i).add(intervals.get(j));
        P[i][1] += intervals.get(j).getProfit();
        if (W[i] > B) {
          M.remove(i);
          mPrime.add(i);
        }
      } else {
        break;
      }
    }

    // post-processing
    for (Integer i : mPrime) {
      Interval l = S.get(i).get(S.get(i).size() - 1);
      if (P[i][1] >= 2 * l.getProfit()) {
        W[i] -= l.getCost();
        P[i][1] -= l.getProfit();
        S.get(i).remove(S.get(i).size() - 1);
      } else {
        W[i] = l.getCost();
        P[i][1] = l.getProfit();
        S.get(i).clear();
        S.get(i).add(l);
      }
    }

    // last-line: return the most profitable assignments
    List<List<Interval>> assignments = new ArrayList<List<Interval>>();
    Arrays.sort(P, new java.util.Comparator<double[]>() {
      public int compare(double[] a, double[] b) {
        return Double.compare(b[1], a[1]);
      }
    });

    for (int k = 0; k < m; k++) {
      assignments.add(S.get(k));
    }

    return assignments;
  }

  public static void main(String[] args) {

    /*
     * Take in and parse the input, which consists of:
     * - n, the number of rows and columns in the grid
     */
    Scanner scanner = new Scanner(System.in);
    int n = scanner.nextInt();
    Random random = new Random();

    double p = 0.5; // probability threshold that node is a customer

    // generate random customers
    List<int[]> customers = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        double r = random.nextDouble();
        if (r < p) {
          int[] pair = { i, j };
          // System.out.println(i + " " + j);
          customers.add(pair);
        }
      }
    }

    // generate random intervals
    List<Interval> intervals = new ArrayList<Interval>();
    for (int[] c : customers) {
      int row = c[0];
      int col = c[1];
      int j = col % 2 == 1 ? col * n + n - 1 - row : col * n + row;
      int i = (int) (random.nextDouble() * j);
      int k = (int) (random.nextDouble() * (n * n - j)) + j;

      double profit = random.nextDouble() * 100 + 50;
      int[] start = new int[2];
      int[] end = new int[2];
      start[0] = (i / n) % 2 == 1 ? n - 1 - i % n : i % n;
      start[1] = i / n;
      end[0] = (k / n) % 2 == 1 ? n - 1 - k % n : k % n;
      end[1] = k / n;
      double launchDist = Math.sqrt(Math.pow((start[0] - c[0]), 2) + Math.pow((start[1] - c[1]), 2));
      double rendezvousDist = Math.sqrt(Math.pow(c[0] - end[0], 2) + Math.pow(c[1] - end[1], 2));
      double cost = launchDist + rendezvousDist;

      intervals.add(new Interval(i, j, k, profit, cost));
    }

    // construct the interval graph
    IntervalGraph intervalGraph = new IntervalGraph();
    intervalGraph.buildGraph(intervals);

    // print intervals
    // System.out.println("The intervals are:");
    // for (Interval interval : intervals) {
    //   System.out.println(interval);
    // }

    // sort intervals by profit/cost ratio
    Collections.sort(intervals, new Comparator<Interval>() {
      @Override
      public int compare(Interval i1, Interval i2) {
        return Double.compare(i1.profit / i1.cost, i2.profit / i2.cost);
      }
    });

    // Battery Budget
    int B = n;

    // Drones
    int m = 4;

    int mDelta = m + intervalGraph.maxDegree();

    // Run the GreedyAlgoForMDSP algorithm.
    List<List<Interval>> assignments = solveMDSP(m, mDelta, B, intervals, intervalGraph);

    // Print the results.
    System.out.println("The most profitable assignments are:");
    for (int i = 0; i < assignments.size(); i++) {
      System.out.println("Drone " + i + ":");
      for (Interval interval : assignments.get(i)) {
        System.out.println(interval);
      }
    }

    scanner.close();
  }
}