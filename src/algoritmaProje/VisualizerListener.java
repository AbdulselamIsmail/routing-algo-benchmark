package algoritmaProje;

import java.util.List;

public interface VisualizerListener {
    // UPDATED: Now accepts 'parent' so we can draw the line connecting them
    void onNodeVisited(Node node, Node parent);

    void onPathFound(List<Node> path);
}