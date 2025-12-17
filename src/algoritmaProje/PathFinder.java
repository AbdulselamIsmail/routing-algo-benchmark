package algoritmaProje;

public interface PathFinder {
    // OLD: SimulationResult run(List<Node> graph, Node start, Node end);

    // NEW: Update to match your new Graph class
    SimulationResult run(Graph graph, Node start, Node end);
    void runVisualSimulation(Graph graph, Node start, Node end, VisualizerListener listener, int delayMS);
}
