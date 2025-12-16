package algoritmaProje;

public class Node {
    public String id;
    public int x;
    public int y;

    public Node(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    // toString helps with printing later
    @Override
    public String toString() {
        return id;
    }
}