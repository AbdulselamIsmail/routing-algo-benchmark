package algoritmaProje;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        GraphGenerator gen = new GraphGenerator();
        Graph myGraph = gen.generateGraph(10); // Now returns a Graph object

        List<Node> allNodes = myGraph.getNodes();

        System.out.println("Graph generated with " + allNodes.size() + " nodes.");

        // Pick the first node
        Node first = allNodes.get(0);

        // Get its neighbors from the Central Manager (Graph class)
        List<Edge> connections = myGraph.getNeighbors(first);

        System.out.println("Checking Node: " + first.id);
        for (Edge e : connections) {
            System.out.println(" -> Connects to " + e.target.id + " (Cost: " + e.weight + ")");
        }
    }
}