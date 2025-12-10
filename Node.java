package algoritmaProje;

import java.util.Objects;

public class Node {
    public String id;
    public int x;
    public int y;

    // REMOVED: List<Edge> edges 
    // Reason: The Graph class holds the edges in the 'adjList'.

    public Node(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    // --- REQUIRED FOR ALGORITHMS TO WORK ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(id, node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id;
    }
}