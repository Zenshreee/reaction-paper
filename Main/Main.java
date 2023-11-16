package Main;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

class Main {

  /**
   * Solves the MDSP problem.
   * 
   * @param m             the number of drones
   * @param mDelta        the number of drones + the maximum degree of the
   *                      interval
   *                      graph
   * @param B             the budget
   * @param intervals     the list of intervals
   * @param intervalGraph the interval graph
   * @return the list of assignments
   */
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

  /*
   * Runs the simulation with a given set of parameters.
   */
  public static double run(int n, int m, double p, int B) {
    // generate random customers
    Random random = new Random();
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

    // sort intervals by profit/cost ratio
    Collections.sort(intervals, new Comparator<Interval>() {
      @Override
      public int compare(Interval i1, Interval i2) {
        return Double.compare(i1.profit / i1.cost, i2.profit / i2.cost);
      }
    });

    // compute m + delta
    int mDelta = m + intervalGraph.maxDegree();

    // Run the GreedyAlgoForMDSP algorithm.
    List<List<Interval>> assignments = solveMDSP(m, mDelta, B, intervals, intervalGraph);

    // compute profit
    double profit = 0;
    for (int i = 0; i < assignments.size(); i++) {
      for (int j = 0; j < assignments.get(i).size(); j++) {
        profit += assignments.get(i).get(j).getProfit();
      }
    }

    double maxProfit = 0;
    for (int k = 0; k < intervals.size(); k++) {
      maxProfit += intervals.get(k).getProfit();
    }

    return profit / maxProfit;
  }

  /*
   * Main method
   */
  public static void main(String[] args) {

    int n = 150; // grid size is n x n
    int m = n * n ;
    double[] probabilities = { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1 };
    int[] B = { n * n};

    for (int b : B) {
      for (double p : probabilities) {
        double profit = run(n, m, p, b);

        // put results in results.txt Format: p, profit
        try {
          FileWriter fw = new FileWriter("results.txt", true);
          fw.write(p + ", " + profit + ", " + b + "\n");
          fw.close();
        } catch (IOException e) {
          e.printStackTrace();
        }

      }
    }

  }
}