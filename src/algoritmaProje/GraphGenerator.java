package algoritmaProje;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphGenerator {

    public Graph generateGraph(int size) {
        Graph graph = new Graph();
        List<Node> tempNodeList = new ArrayList<>();
        Random rand = new Random();

        // 1. Create Nodes and put them in the Graph
        for (int i = 0; i < size; i++) {
            Node n = new Node("R" + i, rand.nextInt(1000), rand.nextInt(1000));
            graph.addNode(n);
            tempNodeList.add(n);
        }

        // 2. Connect Backbone (0->1->2...)
        for (int i = 0; i < size - 1; i++) {
            Node current = tempNodeList.get(i);
            Node next = tempNodeList.get(i + 1);
            double weight = getDist(current, next);

            graph.addEdge(current, next, weight);
            graph.addEdge(next, current, weight); // Bi-directional
        }

        // 3. Add Random Chaos
        for (Node source : tempNodeList) {
            int targetIndex = rand.nextInt(size);
            if (!source.id.equals("R" + targetIndex)) { // Avoid self-loop
                Node target = tempNodeList.get(targetIndex);
                double weight = getDist(source, target);
                graph.addEdge(source, target, weight);
            }
        }

        return graph;
    }

    private double getDist(Node a, Node b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }
}