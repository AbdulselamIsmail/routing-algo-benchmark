package algoritmaProje;

import java.util.*;

public class AStarOptimized implements PathFinder {

    @Override
    public SimulationResult run(Graph graph, Node start, Node end) {
        long startTime = System.nanoTime();
        Map<Node, Double> gScore = new HashMap<>();
        PriorityQueue<QueueNode> pq = new PriorityQueue<>();

        gScore.put(start, 0.0);
        pq.add(new QueueNode(start, 0.0, calculateHeuristic(start, end)));

        int nodesVisited = 0;
        double finalCost = -1;

        while (!pq.isEmpty()) {
            QueueNode current = pq.poll();
            nodesVisited++;

            if (current.node.equals(end)) {
                finalCost = current.gCost;
                break;
            }

            if (current.gCost > gScore.getOrDefault(current.node, Double.MAX_VALUE)) continue;

            for (Edge edge : graph.getNeighbors(current.node)) {
                double tentativeG = current.gCost + edge.weight;
                if (tentativeG < gScore.getOrDefault(edge.target, Double.MAX_VALUE)) {
                    gScore.put(edge.target, tentativeG);
                    double fCost = tentativeG + calculateHeuristic(edge.target, end);
                    pq.add(new QueueNode(edge.target, tentativeG, fCost));
                }
            }
        }
        long duration = System.nanoTime() - startTime;
        return new SimulationResult("A*", graph.getNodes().size(), duration, nodesVisited, (int) finalCost);
    }

    // --- VISUALIZER METHOD ---
    @Override
    public void runVisualSimulation(Graph graph, Node start, Node end, VisualizerListener listener, int delayMS) {
        Map<Node, Double> gScore = new HashMap<>();
        Map<Node, Node> parentMap = new HashMap<>(); // To retrace steps
        PriorityQueue<QueueNode> pq = new PriorityQueue<>();

        gScore.put(start, 0.0);
        pq.add(new QueueNode(start, 0.0, calculateHeuristic(start, end)));

        while (!pq.isEmpty()) {
            QueueNode current = pq.poll();

            // 1. NOTIFY UI
            if (listener != null) {
                // Get the parent of the current node to draw the line
                Node parent = parentMap.get(current.node);
                listener.onNodeVisited(current.node, parent);

                try { Thread.sleep(delayMS); } catch (InterruptedException e) {}
            }

            if (current.node.equals(end)) {
                // 2. FOUND PATH
                if (listener != null) {
                    List<Node> path = reconstructPath(parentMap, end);
                    listener.onPathFound(path);
                }
                return;
            }

            if (current.gCost > gScore.getOrDefault(current.node, Double.MAX_VALUE)) continue;

            for (Edge edge : graph.getNeighbors(current.node)) {
                double tentativeG = current.gCost + edge.weight;
                if (tentativeG < gScore.getOrDefault(edge.target, Double.MAX_VALUE)) {
                    gScore.put(edge.target, tentativeG);
                    parentMap.put(edge.target, current.node); // Record parent

                    double fCost = tentativeG + calculateHeuristic(edge.target, end);
                    pq.add(new QueueNode(edge.target, tentativeG, fCost));
                }
            }
        }
    }

    // --- HELPERS ---

    private double calculateHeuristic(Node a, Node b) {
        // Euclidean distance
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    private List<Node> reconstructPath(Map<Node, Node> parentMap, Node current) {
        List<Node> path = new ArrayList<>();
        path.add(current);
        while (parentMap.containsKey(current)) {
            current = parentMap.get(current);
            path.add(current);
        }
        Collections.reverse(path);
        return path;
    }

    private static class QueueNode implements Comparable<QueueNode> {
        Node node;
        double gCost;
        double fCost;

        public QueueNode(Node node, double gCost, double fCost) {
            this.node = node;
            this.gCost = gCost;
            this.fCost = fCost;
        }

        @Override
        public int compareTo(QueueNode other) {
            return Double.compare(this.fCost, other.fCost);
        }
    }
}