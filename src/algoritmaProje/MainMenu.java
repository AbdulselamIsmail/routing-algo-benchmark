package algoritmaProje;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Project Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setLayout(new GridLayout(1, 2));

        // --- LEFT SIDE: VISUALIZER ---
        JPanel leftPanel = createPanel(new Color(45, 45, 48), "Visual Simulation");
        JButton btnVisual = createButton("Launch Visualizer");
        leftPanel.add(btnVisual);

        JLabel desc1 = new JLabel("<html><center>Real-time animation<br>on small graphs.</center></html>");
        desc1.setForeground(Color.LIGHT_GRAY);
        leftPanel.add(desc1);

        // --- RIGHT SIDE: BENCHMARK ---
        JPanel rightPanel = createPanel(new Color(60, 60, 65), "Data Analysis");
        JButton btnData = createButton("Run Benchmark & Chart");
        rightPanel.add(btnData);

        JLabel desc2 = new JLabel("<html><center>Compare performance<br>on 1,000+ nodes.</center></html>");
        desc2.setForeground(Color.LIGHT_GRAY);
        rightPanel.add(desc2);

        // --- ACTIONS ---
        btnVisual.addActionListener(e -> {
            new VisualizerFrame().setVisible(true);
            // Optional: dispose(); // Close menu if you want
        });

        btnData.addActionListener(e -> runBenchmarkAndShowChart());

        add(leftPanel);
        add(rightPanel);
    }

    private void runBenchmarkAndShowChart() {
        // Show a loading dialog because benchmark takes 2-3 seconds
        JDialog loading = new JDialog(this, "Processing...", true);
        loading.setSize(200, 100);
        loading.setLocationRelativeTo(this);
        loading.add(new JLabel("Running Simulations...", SwingConstants.CENTER));

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                // 1. Run the Logic (We reuse your BenchmarkRunner logic here)
                // Note: We are calling the main method logic directly or you can create an instance
                BenchmarkRunner.main(new String[]{});
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();
                showChartWindow();
            }
        };

        worker.execute();
        loading.setVisible(true); // Blocks until worker is done
    }

    private void showChartWindow() {
        StatsEngine engine = new StatsEngine();
        Map<Integer, Map<String, Double>> data = engine.calculateAverages("benchmark_results.csv"); // Must match filename in CsvWriter

        JFrame chartFrame = new JFrame("Performance Analysis (Lower is Better)");
        chartFrame.setSize(800, 600);
        chartFrame.add(new ChartPanel(data));
        chartFrame.setLocationRelativeTo(null);
        chartFrame.setVisible(true);
    }

    // --- GUI HELPERS ---
    private JPanel createPanel(Color bg, String title) {
        JPanel p = new JPanel();
        p.setBackground(bg);
        p.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 50));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.LIGHT_GRAY);
        p.add(lblTitle);
        return p;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(200, 50));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(0, 122, 204));
        btn.setForeground(Color.BLACK);
        return btn;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        new MainMenu().setVisible(true);
    }
}