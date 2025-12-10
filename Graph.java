package algoritmaProje;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {

    // We use a HashMap for O(1) access speed (Crucial for Big Data)
    // Key = The Node
    // Value = List of connections (Edges) going out from that node
    private Map<Node, List<Edge>> adjList = new HashMap<>();

    /**
     * Adds a node to the graph.
     */
    public void addNode(Node node) {
        // Only add if it doesn't exist yet to prevent duplicates
        adjList.putIfAbsent(node, new ArrayList<>());
    }

    /**
     * Adds a directed edge from source to destination.
     * Note: For a 2-way connection, you usually call this twice (A->B and B->A).
     */
    public void addEdge(Node source, Node destination, double weight) {
        // Ensure nodes exist first
        addNode(source);
        addNode(destination);

        // Create the edge and add it to the source's list
        Edge edge = new Edge(destination, weight);
        adjList.get(source).add(edge);
    }

    /**
     * VITAL FOR ROLE B (Algorithms).
     * Returns the list of edges connected to a specific node.
     */
    public List<Edge> getNeighbors(Node node) {
        return adjList.getOrDefault(node, new ArrayList<>());
    }

    /**
     * VITAL FOR ROLE A (Simulation Runner).
     * Returns all nodes so you can pick random Start/End points.
     */
    public Set<Node> getAllNodes() {
        return adjList.keySet();
    }
}