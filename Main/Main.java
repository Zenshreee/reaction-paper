package Main;

import java.util.*;

class Main {
  public static void main(String[] args) {
    List<Interval> intervals = new ArrayList<>();
    intervals.add(new Interval(1, 4));
    intervals.add(new Interval(2, 6));
    intervals.add(new Interval(5, 6));
    intervals.add(new Interval(7, 8));

    IntervalGraph graph = new IntervalGraph();
    graph.buildGraph(intervals);
    graph.printGraph();
    
  }
}