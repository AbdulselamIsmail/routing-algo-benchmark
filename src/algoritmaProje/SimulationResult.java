package algoritmaProje;

public class SimulationResult {
    public String algorithmName; // "Dijkstra" or "A*"
    public int graphSize;        // e.g., 100 nodes
    public long timeNano;        // How fast it was
    public int nodesVisited;     // How efficient it was
    public double pathCost;      // The total distance

    public SimulationResult(String name, int size, long time, int visited, double cost) {
        this.algorithmName = name;
        this.graphSize = size;
        this.timeNano = time;
        this.nodesVisited = visited;
        this.pathCost = cost;
    }

    // Converts this result into a comma-separated line for Excel
    public String toCSV() {
        return algorithmName + "," + graphSize + "," + timeNano + "," + nodesVisited + "," + pathCost;
    }
}