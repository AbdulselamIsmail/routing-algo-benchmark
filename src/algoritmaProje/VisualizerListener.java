package algoritmaProje;

public interface VisualizerListener {
    // Called whenever the algorithm pops a node from the PriorityQueue
    void onNodeVisited(Node node);

    // Called when the path is found (to draw the final blue line)
    void onPathFound(java.util.List<Node> path);
}