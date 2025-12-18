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

    // Dynamic lists to track animation state
    private Set<Node> visitedNodes = new HashSet<>();
    private List<Node> finalPath = new ArrayList<>();

    // SCALING: The Graph is 1000x1000, but the window size might change.
    // We scale the dots to fit the window.
    private double scaleX = 1.0;
    private double scaleY = 1.0;

    public GraphPanel() {
        this.setBackground(Style.CANVAS_BACKGROUND);
    }

    // --- SETUP METHODS ---

    public void setGraph(Graph graph) {
        this.graph = graph;
        reset(); // Clear old drawings
    }

    public void setStartEnd(Node start, Node end) {
        this.startNode = start;
        this.endNode = end;
        repaint(); // Redraw immediately
    }

    public void reset() {
        visitedNodes.clear();
        finalPath.clear();
        repaint();
    }

    // --- ANIMATION METHODS (Called by the Listener) ---

    public void addVisitedNode(Node node) {
        visitedNodes.add(node);
        repaint(); // Trigger a screen refresh
    }

    public void setPath(List<Node> path) {
        this.finalPath = path;
        repaint();
    }

    // --- THE DRAWING ENGINE ---

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graph == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Update Scale
        scaleX = (double) getWidth() / 1000.0;
        scaleY = (double) getHeight() / 1000.0;

        // 1. Draw Edges (Background Lines)
        g2.setColor(Style.EDGE_COLOR);
        g2.setStroke(Style.THIN_LINE);
        for (Node node : graph.getNodes()) { // changed to getNodes() to match your fix
            for (Edge edge : graph.getNeighbors(node)) {
                drawLine(g2, node, edge.target);
            }
        }

        // --- NEW: Draw "Clickable" Nodes (Subtle Gray Dots) ---
        // This makes them visible so you know where to click!
        g2.setColor(new Color(200, 200, 200)); // Light faint gray
        for (Node n : graph.getNodes()) {
            drawDot(g2, n, 6); // Size 6 (Small but visible)
        }
        // -----------------------------------------------------

        // 2. Draw Visited Nodes (Animation - Yellow)
        g2.setColor(Style.VISITED_COLOR);
        for (Node n : visitedNodes) {
            drawDot(g2, n, 8); // Slightly larger than base nodes
        }

        // 3. Draw The Final Path (Blue)
        if (finalPath != null && finalPath.size() > 1) {
            g2.setColor(Style.PATH_COLOR);
            g2.setStroke(Style.THICK_LINE);
            for (int i = 0; i < finalPath.size() - 1; i++) {
                drawLine(g2, finalPath.get(i), finalPath.get(i+1));
            }
        }

        // 4. Draw Start/End Points (Big and Bright)
        if (startNode != null) {
            g2.setColor(Style.START_NODE_COLOR);
            drawDot(g2, startNode, 15);
        }
        if (endNode != null) {
            g2.setColor(Style.END_NODE_COLOR);
            drawDot(g2, endNode, 15);
        }
    }

    // --- HELPER METHODS TO HANDLE SCALING ---

    private void drawDot(Graphics2D g2, Node n, int size) {
        int x = (int) (n.x * scaleX);
        int y = (int) (n.y * scaleY);
        // Draw oval centered on the coordinate
        g2.fillOval(x - size/2, y - size/2, size, size);
    }

    private void drawLine(Graphics2D g2, Node a, Node b) {
        int x1 = (int) (a.x * scaleX);
        int y1 = (int) (a.y * scaleY);
        int x2 = (int) (b.x * scaleX);
        int y2 = (int) (b.y * scaleY);
        g2.drawLine(x1, y1, x2, y2);
    }
    /**
     * Finds the node closest to where the user clicked.
     * We have to reverse the scaling math (Screen X -> Graph X).
     */
    public Node getNodeAt(int mouseX, int mouseY) {
        if (graph == null) return null;

        // Convert screen coordinates back to graph coordinates (0-1000)
        double graphX = mouseX / scaleX;
        double graphY = mouseY / scaleY;

        Node closest = null;
        double minDistance = Double.MAX_VALUE;

        // Find the node closest to this click
        for (Node n : graph.getNodes()) {
            double dist = Math.sqrt(Math.pow(n.x - graphX, 2) + Math.pow(n.y - graphY, 2));
            // Only 'grab' the node if the click is somewhat close (within 50 units)
            if (dist < 50 && dist < minDistance) {
                minDistance = dist;
                closest = n;
            }
        }
        return closest;
    }
}