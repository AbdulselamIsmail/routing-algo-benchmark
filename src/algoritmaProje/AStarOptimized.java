package algoritmaProje;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class AStarOptimized implements PathFinder {

    @Override
    public SimulationResult runSimulation(Graph graph, Node start, Node end) {
        long startTime = System.nanoTime();

        // 1. Setup Data Structures
        // gScore: The cost of the cheapest path from start to node currently known.
        Map<Node, Double> gScore = new HashMap<>();
        
        // Open Set: Nodes to be explored, sorted by fCost (g + h)
        PriorityQueue<QueueNode> pq = new PriorityQueue<>();

        // 2. Initialize
        gScore.put(start, 0.0);
        
        // For the start node, g=0, so f is just the heuristic
        double startH = calculateHeuristic(start, end);
        pq.add(new QueueNode(start, 0.0, startH));

        int nodesVisited = 0;
        double finalCost = -1; // -1 means "not found"

        // 3. The Algorithm Loop
        while (!pq.isEmpty()) {
            QueueNode current = pq.poll();
            nodesVisited++;

            // Early Exit: Found target
            if (current.node.equals(end)) {
                finalCost = current.gCost;
                break;
            }

            // Optimization: If this path is worse than what we already found, skip it.
            // (Note: We check gCost here, because that is the 'real' distance traveled)
            if (current.gCost > gScore.getOrDefault(current.node, Double.MAX_VALUE)) {
                continue;
            }

            // Check neighbors
            for (Edge edge : graph.getNeighbors(current.node)) {
                double tentativeG = current.gCost + edge.weight;

                // If this path to neighbor is better than any previous one
                if (tentativeG < gScore.getOrDefault(edge.target, Double.MAX_VALUE)) {
                    gScore.put(edge.target, tentativeG);
                    
                    // The Magic of A*: Add the estimated remaining distance (Heuristic)
                    double fCost = tentativeG + calculateHeuristic(edge.target, end);
                    
                    pq.add(new QueueNode(edge.target, tentativeG, fCost));
                }
            }
        }

        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        return new SimulationResult(
            "A*", 
            graph.getAllNodes().size(), 
            duration, 
            nodesVisited, 
            (int) finalCost
        );
    }

    /**
     * The Heuristic Function.
     */
    private double calculateHeuristic(Node current, Node target) {
        // Option 1: Euclidean Distance (Direct Line) - More Accurate
        return Math.sqrt(Math.pow(current.x - target.x, 2) + Math.pow(current.y - target.y, 2));

        // Option 2: Manhattan Distance (Grid Steps) - Faster calculation
        // Uncomment this if you want to test speed over accuracy:
        // return Math.abs(current.x - target.x) + Math.abs(current.y - target.y);
    }

    /**
     * Helper Class specifically for A*.
     * It stores 'g' for math and 'f' for sorting.
     */
    private static class QueueNode implements Comparable<QueueNode> {
        Node node;
        double gCost; // Actual cost from start
        double fCost; // Estimated total cost (g + h)

        public QueueNode(Node node, double gCost, double fCost) {
            this.node = node;
            this.gCost = gCost;
            this.fCost = fCost;
        }

        // CRITICAL: A* sorts by the Estimated Total Cost (fCost), not just distance so far.
        @Override
        public int compareTo(QueueNode other) {
            return Double.compare(this.fCost, other.fCost);
        }
    }
}