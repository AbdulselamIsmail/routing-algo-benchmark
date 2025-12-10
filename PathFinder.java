package algoritmaProje;

/**
 * The Contract.
 * Both the Dijkstra and A* classes must implement this interface.
 */
public interface PathFinder {

    /**
     * Executes the pathfinding algorithm and captures performance metrics.
     *
     * @param graph The map containing all nodes and edges.
     * @param start The starting Node.
     * @param end   The destination Node.
     * @return SimulationResult containing time taken, nodes visited, and the final path.
     */
    SimulationResult runSimulation(Graph graph, Node start, Node end);
}