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
    private JLabel statusLabel;

    // --- NEW: Slider Tools ---
    private JSlider sizeSlider;
    private JLabel sizeValueLabel; // Shows the number "500" next to slider

    // Tools
    private GraphGenerator generator = new GraphGenerator();
    private PathFinder dijkstra = new DijkstraOptimized();
    private PathFinder aStar = new AStarOptimized();

    // UI COLORS
    private final Color PRIMARY_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = Color.BLACK;

    public VisualizerFrame() {
        setTitle("Algoritma Proje - Pathfinding Visualizer");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- 1. TOP HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Network Routing Simulation");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        statusLabel = new JLabel("Ready.");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        statusLabel.setForeground(Color.GRAY);
        headerPanel.add(statusLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- 2. CENTER GRAPH ---
        graphPanel = new GraphPanel();
        add(graphPanel, BorderLayout.CENTER);

        // Mouse Listener
        graphPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Node clickedNode = graphPanel.getNodeAt(e.getX(), e.getY());
                if (clickedNode != null) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        startNode = clickedNode;
                        statusLabel.setText("Start Point Set.");
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        endNode = clickedNode;
                        statusLabel.setText("Destination Set.");
                    }
                    graphPanel.setStartEnd(startNode, endNode);
                }
            }
        });

        // --- 3. BOTTOM CONTROLS ---
        JPanel controlsPanel = new JPanel();
        controlsPanel.setBackground(PRIMARY_COLOR);
        controlsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        controlsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));

        // --- NEW: SLIDER SETUP ---
        // Range: 50 to 1500 nodes. Default: 200.
        sizeSlider = new JSlider(JSlider.HORIZONTAL, 50, 1500, 200);
        sizeSlider.setBackground(PRIMARY_COLOR);
        sizeSlider.setFocusable(false);
        sizeSlider.setPreferredSize(new Dimension(200, 45));

        // Add Ticks to make it look professional
        sizeSlider.setMajorTickSpacing(500);
        sizeSlider.setMinorTickSpacing(100);
        sizeSlider.setPaintTicks(true);

        // Label that updates when you drag
        sizeValueLabel = new JLabel("Size: 200");
        sizeValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sizeValueLabel.setPreferredSize(new Dimension(80, 30)); // Fixed width prevents jitter

        // Listener: Update label immediately while dragging
        sizeSlider.addChangeListener(e -> {
            sizeValueLabel.setText("Size: " + sizeSlider.getValue());
        });

        // Add Components
        controlsPanel.add(sizeValueLabel);
        controlsPanel.add(sizeSlider);

        // Buttons
        JButton btnGenerate = createStyledButton("Generate");
        JButton btnDijkstra = createStyledButton("Run Dijkstra");
        JButton btnAStar = createStyledButton("Run A*");

        controlsPanel.add(Box.createHorizontalStrut(20)); // Spacer
        controlsPanel.add(btnGenerate);
        controlsPanel.add(btnDijkstra);
        controlsPanel.add(btnAStar);

        add(controlsPanel, BorderLayout.SOUTH);

        // --- 4. BUTTON ACTIONS ---
        btnGenerate.addActionListener(e -> generateNewMap());
        btnDijkstra.addActionListener(e -> runAlgorithm(dijkstra, "Dijkstra"));
        btnAStar.addActionListener(e -> runAlgorithm(aStar, "A*"));

        // Initialize
        generateNewMap();
    }

    private void generateNewMap() {
        // --- NEW: Read exact value from slider ---
        int size = sizeSlider.getValue();

        statusLabel.setText("Generating " + size + " nodes...");

        new Thread(() -> {
            currentGraph = generator.generateGraph(size);

            SwingUtilities.invokeLater(() -> {
                List<Node> nodes = new java.util.ArrayList<>(currentGraph.getNodes());
                startNode = nodes.get((int)(Math.random() * nodes.size()));
                endNode = nodes.get((int)(Math.random() * nodes.size()));

                graphPanel.setGraph(currentGraph);
                graphPanel.setStartEnd(startNode, endNode);
                statusLabel.setText("Generated " + size + " Nodes.");
            });
        }).start();
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(TEXT_COLOR);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(230, 240, 255)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(Color.WHITE); }
        });
        return btn;
    }

    private void runAlgorithm(PathFinder algorithm, String name) {
        if (currentGraph == null) return;
        statusLabel.setText("Running " + name + "...");
        graphPanel.reset();

        // --- NEW: COLOR LOGIC ---
        if (name.equals("Dijkstra")) {
            // Dijkstra = Cyan (Like water flooding everywhere)
            // Alpha 150 means slightly transparent
            graphPanel.setVisitedColor(new Color(0, 255, 255, 150));
        } else {
            // A* = Orange (Like a focused beam)
            graphPanel.setVisitedColor(new Color(255, 165, 0, 150));
        }
        // ------------------------

        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            VisualizerListener listener = new VisualizerListener() {
                @Override
                public void onNodeVisited(Node node) { graphPanel.addVisitedNode(node); }
                @Override
                public void onPathFound(List<Node> path) {
                    graphPanel.setPath(path);
                    long duration = System.currentTimeMillis() - startTime;
                    SwingUtilities.invokeLater(() ->
                            statusLabel.setText(name + " Finished. Path: " + path.size() + " hops (" + duration + "ms)")
                    );
                }
            };

            // Smart Speed Calculation
            int currentSize = currentGraph.getNodes().size();
            int delay = 15;
            if (currentSize > 400) delay = 5;
            if (currentSize > 800) delay = 1;

            algorithm.runVisualSimulation(currentGraph, startNode, endNode, listener, delay);
        }).start();
    }
}