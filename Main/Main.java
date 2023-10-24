package Main;

import java.util.*;

class Main {
  public static void main(String[] args) {

    // take in input
    Scanner scanner = new Scanner(System.in);
    int n = scanner.nextInt();
    Random random = new Random();

    GridGraph grid = new GridGraph(n);

    double p = 0.5;

    scanner.close();
  }
}