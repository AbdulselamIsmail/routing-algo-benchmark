package algoritmaProje;

public class SimulationResult {
    public String algorithmName; // "Dijkstra" or "A*"
    public int graphSize;        // e.g., 1000 nodes
    public long durationNano;    // Execution time
    public int nodesVisited;     // Efficiency metric
    public int pathLength;       // To verify correctness
}