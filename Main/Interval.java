package Main;

/**
 * Interval class, represented only by start and end times.
 */
public class Interval {
  int i;
  int j;
  int k;
  double profit;
  double cost;
  /**
   * Interval constructor 
  */
  public Interval(int i, int j, int k, double profit, double cost) {
    this.i = i;
    this.j = j;
    this.k = k;
    this.profit = profit;
    this.cost = cost;
  }

  public double getCost() {
    return cost;
  }

  public double getProfit() {
    return profit;
  }

  // toString
  public String toString() {
    return "[" + i + ", " + j + ", " + k + "]";
  }
}
