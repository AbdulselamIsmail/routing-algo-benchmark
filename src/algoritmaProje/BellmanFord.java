package algoritmaProje;

import java.util.*;

public class BellmanFord implements PathFinder {

    @Override
    public SimulationResult run(Graph graph, Node start, Node end) {
        long startTime = System.nanoTime();

        // 1. Initialize
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> parentMap = new HashMap<>();
        for (Node n : graph.getNodes()) {
            distances.put(n, Double.MAX_VALUE);
        }
        distances.put(start, 0.0);

        // 2. Relax edges (V-1) times
        int nodeCount = graph.getNodes().size();

        // We track changes to optimize; if no changes, stop early.
        for (int i = 0; i < nodeCount - 1; i++) {
            boolean changed = false;

            for (Node u : graph.getNodes()) {
                if (distances.get(u) == Double.MAX_VALUE) continue;

                for (Edge edge : graph.getNeighbors(u)) {
                    Node v = edge.target;
                    double newDist = distances.get(u) + edge.weight;

                    if (newDist < distances.get(v)) {
                        distances.put(v, newDist);
                        parentMap.put(v, u);
                        changed = true;
                    }
                }
            }
            if (!changed) break;
        }

        // 3. Calculate Path Cost
        double cost = distances.getOrDefault(end, 0.0);
        if (distances.get(end) == Double.MAX_VALUE) cost = 0; // No path found

        long endTime = System.nanoTime();

        // Count "Visited" as nodes reached/updated at least once
        int visitedCount = parentMap.size();

        // 4. RETURN SINGLE RESULT OBJECT
        return new SimulationResult("Bellman-Ford", graph.getNodes().size(), (endTime - startTime), visitedCount, cost);
    }

    @Override
    public void runVisualSimulation(Graph graph, Node start, Node end, VisualizerListener listener, int delayMS) {
        // (This visual part remains the same as provided before)
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> parentMap = new HashMap<>();

        for (Node n : graph.getNodes()) distances.put(n, Double.MAX_VALUE);
        distances.put(start, 0.0);

        int nodeCount = graph.getNodes().size();

        for (int i = 0; i < nodeCount - 1; i++) {
            boolean changed = false;
            for (Node u : graph.getNodes()) {
                if (distances.get(u) == Double.MAX_VALUE) continue;

                for (Edge edge : graph.getNeighbors(u)) {
                    Node v = edge.target;
                    double newDist = distances.get(u) + edge.weight;

                    if (newDist < distances.get(v)) {
                        distances.put(v, newDist);
                        parentMap.put(v, u);
                        changed = true;

                        if (listener != null) {
                            listener.onNodeVisited(v, u);
                            try { Thread.sleep(Math.max(1, delayMS / 5)); } catch (Exception e) {}
                        }
                    }
                }
            }
            if (!changed) break;
        }

        if (listener != null) {
            List<Node> path = new ArrayList<>();
            Node current = end;
            if (distances.get(end) != Double.MAX_VALUE) {
                while (current != null) {
                    path.add(0, current);
                    current = parentMap.get(current);
                }
            }
            listener.onPathFound(path);
        }
    }
}