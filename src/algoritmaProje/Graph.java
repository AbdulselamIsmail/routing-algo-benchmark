package algoritmaProje;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    // The "Adjacency List" (The Map of Connections)
    // Key = The starting Node
    // Value = List of Edges leaving that node
    public Map<Node, List<Edge>> adjList = new HashMap<>();

    // Add a node to our manager
    public void addNode(Node n) {
        adjList.putIfAbsent(n, new ArrayList<>());
    }

    // Add a connection
    public void addEdge(Node source, Node target, double weight) {
        // Make sure both exist in the map
        addNode(source);
        addNode(target);

        // Add the edge to the source's list
        List<Edge> neighbors = adjList.get(source);
        neighbors.add(new Edge(target, weight));
    }

    // Helper to get all nodes
    public List<Node> getNodes() {
        return new ArrayList<>(adjList.keySet());
    }

    // Helper to get neighbors of a specific node
    public List<Edge> getNeighbors(Node n) {
        return adjList.get(n);
    }
}