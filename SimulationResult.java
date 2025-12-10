package algoritmaProje;

public class SimulationResult {
    public String algorithmName; // "Dijkstra" or "A*"
    public int graphSize;        // e.g., 1000 nodes
    public long durationNano;    // Execution time
    public int nodesVisited;     // Efficiency metric
    public int pathLength;       // To verify correctness

    // Add this Constructor so you can create the object in one line
    public SimulationResult(String name, int size, long time, int visited, int length) {
        this.algorithmName = name;
        this.graphSize = size;
        this.durationNano = time;
        this.nodesVisited = visited;
        this.pathLength = length;
    }
}