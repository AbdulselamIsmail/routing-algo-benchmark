package algoritmaProje;

public class Edge {
    public Node target;
    public double weight;

    // THIS is the part you are missing:
    public Edge(Node target, double weight) {
        this.target = target;
        this.weight = weight;
    }
}
