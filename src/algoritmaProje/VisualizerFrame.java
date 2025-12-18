package algoritmaProje;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class VisualizerFrame extends JFrame {

    private GraphPanel graphPanel;
    private Graph currentGraph;
    private Node startNode;
    private Node endNode;

    // Status Label to show what's happening ("Running...", "Done", "Cost: 50")
    private JLabel statusLabel;

    // Tools
    private GraphGenerator generator = new GraphGenerator();
    private PathFinder dijkstra = new DijkstraOptimized();
    private PathFinder aStar = new AStarOptimized();


    // UI COLORS (Modern Dark Theme)
    private final Color PRIMARY_COLOR = new Color(45, 45, 48);   // Dark Gray Background
    private final Color ACCENT_COLOR = new Color(0, 122, 204);   // VS Code Blue
    private final Color TEXT_COLOR = Color.BLACK;

    public VisualizerFrame() {
        setTitle("Algoritma Proje - Pathfinding Visualizer");
        setSize(1000, 720); // Bigger window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- 1. TOP HEADER ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20)); // Padding
        headerPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Network Routing Simulation");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        statusLabel = new JLabel("Ready.");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        statusLabel.setForeground(new Color(200, 200, 200));
        headerPanel.add(statusLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- 2. CENTER GRAPH ---
        graphPanel = new GraphPanel();
        add(graphPanel, BorderLayout.CENTER);

        // --- 3. BOTTOM CONTROLS ---
        JPanel controlsPanel = new JPanel();
        controlsPanel.setBackground(PRIMARY_COLOR);
        controlsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        // Use FlowLayout with gaps
        controlsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        // Create Custom Buttons
        JButton btnGenerate = createStyledButton("⚡ Generate New Map");
        JButton btnDijkstra = createStyledButton("🔵 Run Dijkstra");
        JButton btnAStar = createStyledButton("🟢 Run A* (Heuristic)");

        controlsPanel.add(btnGenerate);
        controlsPanel.add(btnDijkstra);
        controlsPanel.add(btnAStar);

        add(controlsPanel, BorderLayout.SOUTH);
        // ... inside VisualizerFrame constructor ...
        graphPanel = new GraphPanel();
        add(graphPanel, BorderLayout.CENTER);

        // --- NEW: MOUSE LISTENER ---
        graphPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Get the node the user clicked on
                Node clickedNode = graphPanel.getNodeAt(e.getX(), e.getY());

                if (clickedNode != null) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        // Left Click = Set Start
                        startNode = clickedNode;
                        statusLabel.setText("Start Point Moved.");
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        // Right Click = Set End
                        endNode = clickedNode;
                        statusLabel.setText("Destination Moved.");
                    }
                    // Redraw the green/red dots immediately
                    graphPanel.setStartEnd(startNode, endNode);
                }
            }
        });

        // --- 4. BUTTON ACTIONS ---
        btnGenerate.addActionListener(e -> generateNewMap());
        btnDijkstra.addActionListener(e -> runAlgorithm(dijkstra, "Dijkstra"));
        btnAStar.addActionListener(e -> runAlgorithm(aStar, "A*"));

        // Initialize
        generateNewMap();
    }

    /**
     * Helper to make buttons look beautiful and interactive
     */
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(TEXT_COLOR);
        btn.setBackground(new Color(60, 60, 60)); // Slightly lighter gray
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25)); // Padding inside button
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand pointer on hover

        // Hover Effect (Change color when mouse enters/exits)
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(ACCENT_COLOR); // Turn Blue
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(60, 60, 60)); // Back to Gray
            }
        });

        return btn;
    }

    private void generateNewMap() {
        statusLabel.setText("Generating Graph...");
        currentGraph = generator.generateGraph(500);

        List<Node> nodes = new java.util.ArrayList<>(currentGraph.getNodes());
        startNode = nodes.get((int)(Math.random() * nodes.size()));
        endNode = nodes.get((int)(Math.random() * nodes.size()));

        graphPanel.setGraph(currentGraph);
        graphPanel.setStartEnd(startNode, endNode);
        statusLabel.setText("Graph Generated. Nodes: " + nodes.size());
    }

    private void runAlgorithm(PathFinder algorithm, String name) {
        if (currentGraph == null) return;

        statusLabel.setText("Running " + name + "...");
        graphPanel.reset();

        new Thread(() -> {
            long startTime = System.currentTimeMillis();

            VisualizerListener listener = new VisualizerListener() {
                @Override
                public void onNodeVisited(Node node) {
                    graphPanel.addVisitedNode(node);
                }

                @Override
                public void onPathFound(List<Node> path) {
                    graphPanel.setPath(path);
                    long duration = System.currentTimeMillis() - startTime;
                    // Update status on UI thread
                    SwingUtilities.invokeLater(() ->
                            statusLabel.setText(name + " Finished. Path Length: " + path.size() + " (" + duration + "ms)")
                    );
                }
            };

            // Speed set to 15ms for a nice smooth flow
            algorithm.runVisualSimulation(currentGraph, startNode, endNode, listener, 15);

        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VisualizerFrame().setVisible(true);
        });
    }


}