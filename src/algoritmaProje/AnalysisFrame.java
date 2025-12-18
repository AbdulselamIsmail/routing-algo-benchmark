package algoritmaProje;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class AnalysisFrame extends JFrame {

    private ChartPanel chartPanel;
    private StatsEngine engine;
    private String csvFile = "./benchmark_results.csv";

    // Buttons
    private JButton btnEfficiency;
    private JButton btnTime;

    public AnalysisFrame() {
        setTitle("Performance Analytics");
        setSize(900, 650);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center on screen

        engine = new StatsEngine();
        chartPanel = new ChartPanel();

        // --- TOP TOOLBAR ---
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        toolbar.setBackground(new Color(240, 240, 240));

        btnEfficiency = createToggleButton("Nodes Visited (Efficiency)", true);
        btnTime = createToggleButton("Execution Time (Speed)", false);

        toolbar.add(btnEfficiency);
        toolbar.add(btnTime);
        add(toolbar, BorderLayout.NORTH);

        // --- CENTER CHART ---
        add(chartPanel, BorderLayout.CENTER);

        // --- BUTTON ACTIONS ---
        btnEfficiency.addActionListener(e -> loadData("VISITED"));
        btnTime.addActionListener(e -> loadData("TIME"));

        // Default Load
        loadData("VISITED");
    }

    private void loadData(String metric) {
        // Toggle Button Styles
        boolean isVisited = metric.equals("VISITED");
        styleButton(btnEfficiency, isVisited);
        styleButton(btnTime, !isVisited);

        // Fetch Data
        Map<Integer, Map<String, Double>> data = engine.getStats(csvFile, metric);

        // Update Chart
        String label = isVisited ? "Average Nodes Visited (Lower is Better)" : "Average Time in ms (Lower is Better)";
        chartPanel.setChartData(data, label);
    }

    private JButton createToggleButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        styleButton(btn, active);
        return btn;
    }

    private void styleButton(JButton btn, boolean active) {
        // 1. FORCE Java Styling (Ignores Windows/Mac override that blocks colors)
        // This ensures setBackground() actually works!
        btn.setUI(new javax.swing.plaf.metal.MetalButtonUI());

        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        if (active) {
            // Active: Blue Background, White Text
            btn.setBackground(new Color(0, 122, 204));
            btn.setForeground(Color.WHITE);
            // On Metal UI, we need this to make the color solid:
            btn.setOpaque(true);
        } else {
            // Inactive: White Background, Black Text
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setOpaque(true);
        }
    }
}