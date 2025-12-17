package algoritmaProje;

import java.util.*;

public class DijkstraOptimized implements PathFinder {

    // --- ORIGINAL METHOD (For Benchmark) ---
    @Override
    public SimulationResult run(Graph graph, Node start, Node end) {
        long startTime = System.nanoTime();
        Map<Node, Double> distances = new HashMap<>();
        PriorityQueue<QueueNode> pq = new PriorityQueue<>();

        distances.put(start, 0.0);
        pq.add(new QueueNode(start, 0.0));

        int nodesVisited = 0;
        double finalCost = -1;

        while (!pq.isEmpty()) {
            QueueNode current = pq.poll();
            nodesVisited++;

            if (current.node.equals(end)) {
                finalCost = current.cost;
                break;
            }

            if (current.cost > distances.getOrDefault(current.node, Double.MAX_VALUE)) continue;

            for (Edge edge : graph.getNeighbors(current.node)) {
                double newDist = current.cost + edge.weight;
                if (newDist < distances.getOrDefault(edge.target, Double.MAX_VALUE)) {
                    distances.put(edge.target, newDist);
                    pq.add(new QueueNode(edge.target, newDist));
                }
            }
        }
        long duration = System.nanoTime() - startTime;
        return new SimulationResult("Dijkstra", graph.getNodes().size(), duration, nodesVisited, (int) finalCost);
    }

    // --- NEW METHOD (For Visualizer) ---
    @Override
    public void runVisualSimulation(Graph graph, Node start, Node end, VisualizerListener listener, int delayMS) {
        Map<Node, Double> distances = new HashMap<>();
        // NEW: We need to remember the path to draw it later
        Map<Node, Node> parentMap = new HashMap<>();
        PriorityQueue<QueueNode> pq = new PriorityQueue<>();

        distances.put(start, 0.0);
        pq.add(new QueueNode(start, 0.0));

        while (!pq.isEmpty()) {
            QueueNode current = pq.poll();

            // 1. NOTIFY UI: "I am visiting this node!"
            if (listener != null) {
                listener.onNodeVisited(current.node);
                try {
                    Thread.sleep(delayMS); // The "Slow Motion" Pause
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (current.node.equals(end)) {
                // 2. NOTIFY UI: "I found the path!"
                if (listener != null) {
                    List<Node> path = reconstructPath(parentMap, end);
                    listener.onPathFound(path);
                }
                return;
            }

            if (current.cost > distances.getOrDefault(current.node, Double.MAX_VALUE)) continue;

            for (Edge edge : graph.getNeighbors(current.node)) {
                double newDist = current.cost + edge.weight;
                if (newDist < distances.getOrDefault(edge.target, Double.MAX_VALUE)) {
                    distances.put(edge.target, newDist);
                    parentMap.put(edge.target, current.node); // Record the path
                    pq.add(new QueueNode(edge.target, newDist));
                }
            }
        }
    }

    // Helper to build the final Blue Line
    private List<Node> reconstructPath(Map<Node, Node> parentMap, Node current) {
        List<Node> path = new ArrayList<>();
        path.add(current);
        while (parentMap.containsKey(current)) {
            current = parentMap.get(current);
            path.add(current);
        }
        Collections.reverse(path); // Turn End->Start into Start->End
        return path;
    }

    // Helper Class
    private static class QueueNode implements Comparable<QueueNode> {
        Node node;
        double cost;
        public QueueNode(Node node, double cost) {
            this.node = node;
            this.cost = cost;
        }
        @Override
        public int compareTo(QueueNode other) {
            return Double.compare(this.cost, other.cost);
        }
    }
}