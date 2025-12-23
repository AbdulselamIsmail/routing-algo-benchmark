package algoritmaProje;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public class GraphPanel extends JPanel {

    private Graph graph;
    private Node startNode;
    private Node endNode;

    // Track Nodes and Edges for animation
    private Set<Node> visitedNodes = new HashSet<>();
    private List<Node[]> visitedEdges = new ArrayList<>(); // Stores pairs: [Parent, Child]
    private List<Node> finalPath = new ArrayList<>();

    private Color currentVisitedColor = new Color(255, 200, 0, 150);
    private double scaleX = 1.0;
    private double scaleY = 1.0;

    public GraphPanel() {
        this.setBackground(Style.CANVAS_BACKGROUND);
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
        reset();
    }

    public void setStartEnd(Node start, Node end) {
        this.startNode = start;
        this.endNode = end;
        repaint();
    }

    public void setVisitedColor(Color c) {
        this.currentVisitedColor = c;
    }

    public void reset() {
        visitedNodes.clear();
        visitedEdges.clear(); // Clear lines too
        finalPath.clear();
        repaint();
    }

    // --- UPDATED ANIMATION METHOD ---
    public void addVisitedNode(Node node, Node parent) {
        visitedNodes.add(node);
        // If there is a parent, store the connection so we can draw the line
        if (parent != null) {
            visitedEdges.add(new Node[]{parent, node});
        }
        repaint();
    }

    public void setPath(List<Node> path) {
        this.finalPath = path;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graph == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        scaleX = (double) getWidth() / 1000.0;
        scaleY = (double) getHeight() / 1000.0;

        // 1. Draw Gray Background Edges
        g2.setColor(Style.EDGE_COLOR);
        g2.setStroke(Style.THIN_LINE);
        for (Node node : graph.getNodes()) {
            for (Edge edge : graph.getNeighbors(node)) {
                drawLine(g2, node, edge.target);
            }
        }

        // 2. Draw Faint Gray Nodes
        g2.setColor(new Color(200, 200, 200));
        for (Node n : graph.getNodes()) {
            drawDot(g2, n, 6);
        }

        // --- NEW: Draw Visited Lines (The "Web" Effect) ---
        g2.setColor(currentVisitedColor);
        g2.setStroke(new BasicStroke(2)); // Slightly thicker than background lines
        for (Node[] pair : visitedEdges) {
            drawLine(g2, pair[0], pair[1]);
        }
        // --------------------------------------------------

        // 3. Draw Visited Nodes (The Dots)
        g2.setColor(currentVisitedColor);
        for (Node n : visitedNodes) {
            drawDot(g2, n, 8);
        }

        // 4. Draw Final Path
        if (finalPath != null && finalPath.size() > 1) {
            g2.setColor(Style.PATH_COLOR);
            g2.setStroke(Style.THICK_LINE);
            for (int i = 0; i < finalPath.size() - 1; i++) {
                drawLine(g2, finalPath.get(i), finalPath.get(i+1));
            }
        }

        // 5. Start/End
        if (startNode != null) {
            g2.setColor(Style.START_NODE_COLOR);
            drawDot(g2, startNode, 15);
        }
        if (endNode != null) {
            g2.setColor(Style.END_NODE_COLOR);
            drawDot(g2, endNode, 15);
        }
    }

    private void drawDot(Graphics2D g2, Node n, int size) {
        int x = (int) (n.x * scaleX);
        int y = (int) (n.y * scaleY);
        g2.fillOval(x - size/2, y - size/2, size, size);
    }

    private void drawLine(Graphics2D g2, Node a, Node b) {
        int x1 = (int) (a.x * scaleX);
        int y1 = (int) (a.y * scaleY);
        int x2 = (int) (b.x * scaleX);
        int y2 = (int) (b.y * scaleY);
        g2.drawLine(x1, y1, x2, y2);
    }

    public Node getNodeAt(int mouseX, int mouseY) {
        if (graph == null) return null;
        double graphX = mouseX / scaleX;
        double graphY = mouseY / scaleY;
        Node closest = null;
        double minDistance = Double.MAX_VALUE;
        for (Node n : graph.getNodes()) {
            double dist = Math.sqrt(Math.pow(n.x - graphX, 2) + Math.pow(n.y - graphY, 2));
            if (dist < 50 && dist < minDistance) {
                minDistance = dist;
                closest = n;
            }
        }
        return closest;
    }
}