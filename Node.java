
package algoritmaProje;
import java.util.ArrayList;
import java.util.List;

public class Node {
    public String id;
    public int x;
    public int y;
    // We store edges here for easy access
    public List<Edge> edges = new ArrayList<>();

    public Node(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public void addEdge(Node target, double weight) {
        this.edges.add(new Edge(target, weight));
    }
}
