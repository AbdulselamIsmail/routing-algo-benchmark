package algoritmaProje;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class DijkstraOptimized implements PathFinder {

    @Override
    public SimulationResult runSimulation(Graph graph, Node start, Node end) {
        long startTime = System.nanoTime();
        
        // 1. Setup Data Structures
        // Stores the shortest known distance to every node
        Map<Node, Double> distances = new HashMap<>();
        
        // The Open Set: Nodes we need to process, sorted by lowest cost
        PriorityQueue<QueueNode> pq = new PriorityQueue<>();

        // 2. Initialize
        distances.put(start, 0.0);
        pq.add(new QueueNode(start, 0.0));
        
        int nodesVisited = 0;
        double finalCost = -1; // -1 means "not found"

        // 3. The Algorithm Loop
        while (!pq.isEmpty()) {
            // Get the node with the lowest cost
            QueueNode current = pq.poll();
            nodesVisited++;

            // Early Exit: Did we find the target?
            if (current.node.equals(end)) {
                finalCost = current.cost;
                break; 
            }

            // Optimization: If we found a shorter way to this node already, skip this stale entry
            if (current.cost > distances.getOrDefault(current.node, Double.MAX_VALUE)) {
                continue;
            }

            // Check neighbors
            for (Edge edge : graph.getNeighbors(current.node)) {
                double newDist = current.cost + edge.weight;

                // If we found a faster way to the neighbor, update it
                if (newDist < distances.getOrDefault(edge.target, Double.MAX_VALUE)) {
                    distances.put(edge.target, newDist);
                    pq.add(new QueueNode(edge.target, newDist));
                }
            }
        }

        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        // Return the metrics
        return new SimulationResult(
            "Dijkstra", 
            graph.getAllNodes().size(), 
            duration, 
            nodesVisited, 
            (int) finalCost // Casting double cost to int for the result object
        );
    }

    /**
     * Helper Class specifically for the PriorityQueue.
     * It couples a Node with "Cost to reach it so far".
     */
    private static class QueueNode implements Comparable<QueueNode> {
        Node node;
        double cost;

        public QueueNode(Node node, double cost) {
            this.node = node;
            this.cost = cost;
        }

        // This tells the PriorityQueue to sort by COST (smallest first)
        @Override
        public int compareTo(QueueNode other) {
            return Double.compare(this.cost, other.cost);
        }
    }
}