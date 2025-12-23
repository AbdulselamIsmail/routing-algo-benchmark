package algoritmaProje;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Map;

public class ChartPanel extends JPanel {
    private Map<Integer, Map<String, Double>> data;
    private String yAxisLabel = "Value";

    // Colors
    private final Color COLOR_DIJKSTRA = new Color(54, 162, 235); // Blue
    private final Color COLOR_ASTAR = new Color(75, 192, 192);    // Green
    private final Color COLOR_BELLMAN = new Color(138, 43, 226);  // Purple (New)
    private final Color TEXT_COLOR = new Color(80, 80, 80);
    private final Color GRID_COLOR = new Color(220, 220, 220);

    public ChartPanel() {
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    public void setChartData(Map<Integer, Map<String, Double>> newData, String label) {
        this.data = newData;
        this.yAxisLabel = label;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (data == null || data.isEmpty()) {
            g.drawString("Loading Data...", 100, 100);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int marginLeft = 80;
        int marginBottom = 90;
        int marginTop = 60;
        int marginRight = 20;

        int chartWidth = width - marginLeft - marginRight;
        int chartHeight = height - marginBottom - marginTop;

        // 1. Determine Max Value
        double maxVal = 0;
        for (Integer size : data.keySet()) {
            for (Double val : data.get(size).values()) {
                if (val > maxVal) maxVal = val;
            }
        }
        maxVal = (maxVal == 0) ? 1 : maxVal * 1.1;

        // 2. Draw Grid
        g2.setColor(GRID_COLOR);
        g2.setStroke(new BasicStroke(1));
        int gridLines = 5;
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        for (int i = 0; i <= gridLines; i++) {
            int y = marginTop + chartHeight - (i * chartHeight / gridLines);
            double value = (maxVal / gridLines) * i;

            g2.setColor(GRID_COLOR);
            g2.drawLine(marginLeft, y, width - marginRight, y);

            g2.setColor(TEXT_COLOR);
            String label = formatValue(value);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, marginLeft - fm.stringWidth(label) - 10, y + 5);
        }

        // --- 3. DRAW BARS (ADJUSTED FOR 3 ALGORITHMS) ---
        int numGroups = data.size();
        int groupWidth = chartWidth / numGroups;

        // Calculate new widths to fit 3 bars + spacing
        // We divide the group space into roughly 4 parts (3 bars + gaps)
        int barWidth = (int) (groupWidth * 0.22);
        int spacing = (int) (groupWidth * 0.05);

        // Calculate starting X to center the cluster of 3 bars
        int totalClusterWidth = (barWidth * 3) + (spacing * 2);
        int x = marginLeft + (groupWidth / 2) - (totalClusterWidth / 2);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 10)); // Smaller font to fit labels

        for (Integer size : data.keySet()) {
            Map<String, Double> algos = data.get(size);
            double dVal = algos.getOrDefault("Dijkstra", 0.0);
            double aVal = algos.getOrDefault("A*", 0.0);
            double bVal = algos.getOrDefault("Bellman-Ford", 0.0); // Get Bellman Data

            int h1 = (int) ((dVal / maxVal) * chartHeight);
            int h2 = (int) ((aVal / maxVal) * chartHeight);
            int h3 = (int) ((bVal / maxVal) * chartHeight);

            int y1 = marginTop + chartHeight - h1;
            int y2 = marginTop + chartHeight - h2;
            int y3 = marginTop + chartHeight - h3;

            // 1. Dijkstra (Blue)
            g2.setColor(COLOR_DIJKSTRA);
            g2.fillRoundRect(x, y1, barWidth, h1, 8, 8);
            g2.setColor(Color.BLACK);
            drawCenteredString(g2, formatValue(dVal), x + barWidth/2, y1 - 5);

            // 2. A* (Green)
            g2.setColor(COLOR_ASTAR);
            g2.fillRoundRect(x + barWidth + spacing, y2, barWidth, h2, 8, 8);
            g2.setColor(Color.BLACK);
            drawCenteredString(g2, formatValue(aVal), x + barWidth + spacing + barWidth/2, y2 - 5);

            // 3. Bellman-Ford (Purple)
            g2.setColor(COLOR_BELLMAN);
            g2.fillRoundRect(x + (barWidth + spacing) * 2, y3, barWidth, h3, 8, 8);
            g2.setColor(Color.BLACK);
            drawCenteredString(g2, formatValue(bVal), x + (barWidth + spacing) * 2 + barWidth/2, y3 - 5);

            // X-Axis Label
            g2.setColor(TEXT_COLOR);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            // Center label under the middle bar (A*)
            drawCenteredString(g2, size + " Nodes", x + barWidth + spacing + (barWidth/2), marginTop + chartHeight + 25);

            x += groupWidth;
        }

        // 4. Titles & Legend
        drawAxisTitles(g2, width, height, marginLeft);
        drawLegend(g2, width);
    }

    // --- HELPER METHODS ---

    private String formatValue(double val) {
        if (yAxisLabel.contains("Time")) {
            return String.format("%.3f ms", val);
        } else {
            return String.format("%.0f", val);
        }
    }

    private void drawAxisTitles(Graphics2D g2, int width, int height, int marginLeft) {
        g2.setColor(TEXT_COLOR);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // X-Axis
        drawCenteredString(g2, "Graph Size (Number of Nodes)", width / 2 + (marginLeft/2), height - 20);

        // Y-Axis (Rotated)
        AffineTransform defaultAt = g2.getTransform();
        AffineTransform at = new AffineTransform();
        at.rotate(-Math.PI / 2);
        g2.setTransform(at);
        g2.drawString(yAxisLabel, -(height / 2) - 50, 20);
        g2.setTransform(defaultAt);
    }

    private void drawLegend(Graphics2D g2, int width) {
        int iconSize = 15;
        int legendY = 30;
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Center the legend items
        int startX = width/2 - 200;

        // Dijkstra
        g2.setColor(COLOR_DIJKSTRA);
        g2.fillRoundRect(startX, legendY, iconSize, iconSize, 5, 5);
        g2.setColor(TEXT_COLOR);
        g2.drawString("Dijkstra", startX + 20, legendY + 12);

        // A*
        g2.setColor(COLOR_ASTAR);
        g2.fillRoundRect(startX + 100, legendY, iconSize, iconSize, 5, 5);
        g2.setColor(TEXT_COLOR);
        g2.drawString("A*", startX + 120, legendY + 12);

        // Bellman-Ford
        g2.setColor(COLOR_BELLMAN);
        g2.fillRoundRect(startX + 180, legendY, iconSize, iconSize, 5, 5);
        g2.setColor(TEXT_COLOR);
        g2.drawString("Bellman-Ford", startX + 200, legendY + 12);
    }

    private void drawCenteredString(Graphics2D g, String text, int x, int y) {
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, x - (textWidth / 2), y);
    }
}