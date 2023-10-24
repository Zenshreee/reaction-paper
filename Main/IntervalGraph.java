package Main;

import java.util.*;

public class IntervalGraph {
  Map<Interval, List<Interval>> graph = new HashMap<>();

  /**
   * Adds an interval to the graph.
   * 
   * @param interval the interval to add
   */
  public void addInterval(Interval interval) {
    graph.putIfAbsent(interval, new ArrayList<>());
  }

  /**
   * Adds an edge between two intervals.
   * 
   * @param i1 the first interval
   * @param i2 the second interval
   */
  public void addEdge(Interval i1, Interval i2) {
    graph.get(i1).add(i2);
    graph.get(i2).add(i1);
  }

  /**
   * Checks if two intervals overlap.
   * 
   * @param a the first interval
   * @param b the second interval
   * @return true if the intervals overlap, false otherwise
   */
  public boolean isOverlapping(Interval a, Interval b) {
    return !(a.end < b.start || b.end < a.start);
  }

  /**
   * Builds the graph from a list of intervals.
   * 
   * @param intervals the list of intervals
   */
  public void buildGraph(List<Interval> intervals) {
    for (Interval interval : intervals) {
      addInterval(interval);
    }

    for (int i = 0; i < intervals.size(); i++) {
      for (int j = i + 1; j < intervals.size(); j++) {
        if (isOverlapping(intervals.get(i), intervals.get(j))) {
          addEdge(intervals.get(i), intervals.get(j));
        }
      }
    }
  }

  /**
   * Prints the graph.
   */
  public void printGraph() {
    for (Interval interval : graph.keySet()) {
      System.out.print("[" + interval.start + ", " + interval.end + "]: ");
      System.out.print("[");
      for (Interval neighbor : graph.get(interval)) {
        System.out.print("[" + neighbor.start + ", " + neighbor.end + "], ");
      }
      // parse out the last comma + space
      if (graph.get(interval).size() > 0) {
        System.out.print("\b\b");
      }
      System.out.print("]");
      System.out.println();
    }
  }

}
