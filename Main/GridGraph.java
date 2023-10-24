package Main;

import java.util.ArrayList;
import java.util.List;

public class GridGraph {
    private final int n;
    private final Node[][] grid;

    public GridGraph(int n) {
        this.n = n;
        this.grid = new Node[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = new Node(i, j);
            }
        }

        // Initialize nodes and set up edges
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (x < n - 1) {
                    grid[x][y].addNeighbor(grid[x + 1][y]); // Right neighbor
                    grid[x + 1][y].addNeighbor(grid[x][y]);
                }
                if (y < n - 1) {
                    grid[x][y].addNeighbor(grid[x][y + 1]); // Lower neighbor
                    grid[x][y + 1].addNeighbor(grid[x][y]);
                }
            }
        }
    }

    /**
     * Prints the grid graph.
     */
    public void printGraph() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print("(" + grid[i][j].x + " " + grid[i][j].y + ") ");
            }
            System.out.println();
        }
    }

    /**
     * Prints the edges of the grid graph.
     */
    public void printEdges() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print("(" + i + ", " + j + "): [");
                for (Node neighbor : grid[i][j].getNeighbors()) {
                    System.out.print("(" + neighbor.x + ", " + neighbor.y + "), ");
                }
                // parse out the last space and comma
                if (grid[i][j].getNeighbors().size() > 0) {
                    System.out.print("\b\b");
                }
                System.out.println("]");
            }
        }
    }

    public static class Node {
        public final int x;
        public final int y;
        private final List<Node> neighbors;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.neighbors = new ArrayList<>();
        }

        public void addNeighbor(Node neighbor) {
            this.neighbors.add(neighbor);
        }

        public List<Node> getNeighbors() {
            return neighbors;
        }
    }

    public Node getNode(int x, int y) {
        if (x < 0 || x >= n || y < 0 || y >= n) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        return grid[x][y];
    }
}
