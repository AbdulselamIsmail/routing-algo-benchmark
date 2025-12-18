package algoritmaProje;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Map;

public class ChartPanel extends JPanel {
    private Map<Integer, Map<String, Double>> data;
    private String yAxisLabel = "Value";

    // Colors
    private final Color COLOR_DIJKSTRA = new Color(54, 162, 235);
    private final Color COLOR_ASTAR = new Color(75, 192, 192);
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
        // Avoid 0 division and add headroom
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

            // Y-Axis Number Formatting
            g2.setColor(TEXT_COLOR);
            String label = formatValue(value); // Uses helper method
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, marginLeft - fm.stringWidth(label) - 10, y + 5);
        }

        // 3. Draw Bars
        int numGroups = data.size();
        int groupWidth = chartWidth / numGroups;
        int barWidth = (int) (groupWidth * 0.3);
        int spacing = (int) (groupWidth * 0.1);
        int x = marginLeft + (groupWidth / 2) - barWidth - (spacing/2);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 11)); // Slightly smaller font for precision text

        for (Integer size : data.keySet()) {
            Map<String, Double> algos = data.get(size);
            double dVal = algos.getOrDefault("Dijkstra", 0.0);
            double aVal = algos.getOrDefault("A*", 0.0);

            int h1 = (int) ((dVal / maxVal) * chartHeight);
            int h2 = (int) ((aVal / maxVal) * chartHeight);

            // Dijkstra
            g2.setColor(COLOR_DIJKSTRA);
            g2.fillRoundRect(x, marginTop + chartHeight - h1, barWidth, h1, 10, 10);
            g2.setColor(Color.BLACK);
            drawCenteredString(g2, formatValue(dVal), x + barWidth/2, marginTop + chartHeight - h1 - 5);

            // A*
            g2.setColor(COLOR_ASTAR);
            g2.fillRoundRect(x + barWidth + spacing, marginTop + chartHeight - h2, barWidth, h2, 10, 10);
            g2.setColor(Color.BLACK);
            drawCenteredString(g2, formatValue(aVal), x + barWidth + spacing + barWidth/2, marginTop + chartHeight - h2 - 5);

            // X-Axis Label
            g2.setColor(TEXT_COLOR);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            drawCenteredString(g2, size + " Nodes", x + barWidth + (spacing/2), marginTop + chartHeight + 25);

            x += groupWidth;
        }

        // 4. Titles & Legend
        drawAxisTitles(g2, width, height, marginLeft);
        drawLegend(g2, width);
    }

    // --- NEW: SMART FORMATTER ---
    private String formatValue(double val) {
        if (yAxisLabel.contains("Time")) {
            // If it's Time, show 3 decimal places + "ms"
            // Example: "0.452 ms"
            return String.format("%.3f ms", val);
        } else {
            // If it's Nodes, show Integer
            // Example: "550"
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

        g2.setColor(COLOR_DIJKSTRA);
        g2.fillRoundRect(width/2 - 150, legendY, iconSize, iconSize, 5, 5);
        g2.setColor(TEXT_COLOR);
        g2.drawString("Dijkstra", width/2 - 130, legendY + 12);

        g2.setColor(COLOR_ASTAR);
        g2.fillRoundRect(width/2 - 30, legendY, iconSize, iconSize, 5, 5);
        g2.setColor(TEXT_COLOR);
        g2.drawString("A*", width/2 - 10, legendY + 12);
    }

    private void drawCenteredString(Graphics2D g, String text, int x, int y) {
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, x - (textWidth / 2), y);
    }
}