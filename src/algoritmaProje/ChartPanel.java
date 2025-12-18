package algoritmaProje;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Map;

public class ChartPanel extends JPanel {
    private Map<Integer, Map<String, Double>> data;

    // Modern Flat Colors
    private final Color COLOR_DIJKSTRA = new Color(54, 162, 235); // Soft Blue
    private final Color COLOR_ASTAR = new Color(75, 192, 192);    // Teal/Emerald
    private final Color GRID_COLOR = new Color(220, 220, 220);    // Light Gray
    private final Color TEXT_COLOR = new Color(80, 80, 80);       // Dark Gray

    public ChartPanel(Map<Integer, Map<String, Double>> data) {
        this.data = data;
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (data == null || data.isEmpty()) {
            g.drawString("No Data available.", 100, 100);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // --- LAYOUT CALCULATIONS ---
        int width = getWidth();
        int height = getHeight();

        // INCREASED MARGINS HERE:
        int marginLeft = 80;
        int marginBottom = 90; // Changed from 50 to 90 for more room
        int marginTop = 60;
        int marginRight = 20;

        int chartWidth = width - marginLeft - marginRight;
        int chartHeight = height - marginBottom - marginTop;

        // 1. Find Max Value
        double maxVal = 0;
        for (Integer size : data.keySet()) {
            for (Double val : data.get(size).values()) {
                if (val > maxVal) maxVal = val;
            }
        }
        maxVal = maxVal * 1.1; // Headroom

        // --- DRAW BACKGROUND GRID ---
        g2.setColor(GRID_COLOR);
        g2.setStroke(new BasicStroke(1));

        int gridLines = 5;
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        for (int i = 0; i <= gridLines; i++) {
            int y = marginTop + chartHeight - (i * chartHeight / gridLines);
            double value = (maxVal / gridLines) * i;

            // Grid Line
            g2.setColor(GRID_COLOR);
            g2.drawLine(marginLeft, y, width - marginRight, y);

            // Y-Axis Number
            g2.setColor(TEXT_COLOR);
            String label = String.format("%.0f", value);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, marginLeft - fm.stringWidth(label) - 10, y + 5);
        }

        // --- DRAW BARS ---
        int numGroups = data.size();
        int groupWidth = chartWidth / numGroups;
        int barWidth = (int) (groupWidth * 0.3);
        int spacing = (int) (groupWidth * 0.1);

        int x = marginLeft + (groupWidth / 2) - barWidth - (spacing/2);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));

        for (Integer size : data.keySet()) {
            Map<String, Double> algos = data.get(size);
            double dVal = algos.getOrDefault("Dijkstra", 0.0);
            double aVal = algos.getOrDefault("A*", 0.0);

            int h1 = (int) ((dVal / maxVal) * chartHeight);
            int h2 = (int) ((aVal / maxVal) * chartHeight);

            int y1 = marginTop + chartHeight - h1;
            int y2 = marginTop + chartHeight - h2;

            // Draw Dijkstra
            g2.setColor(COLOR_DIJKSTRA);
            g2.fillRoundRect(x, y1, barWidth, h1, 10, 10);
            g2.setColor(Color.BLACK);
            drawCenteredString(g2, String.format("%.0f", dVal), x + barWidth/2, y1 - 5);

            // Draw A*
            g2.setColor(COLOR_ASTAR);
            g2.fillRoundRect(x + barWidth + spacing, y2, barWidth, h2, 10, 10);
            g2.setColor(Color.BLACK);
            drawCenteredString(g2, String.format("%.0f", aVal), x + barWidth + spacing + barWidth/2, y2 - 5);

            // X-Axis Label (e.g., "100 Nodes")
            g2.setColor(TEXT_COLOR);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            // Positioned just below the axis line
            drawCenteredString(g2, size + " Nodes", x + barWidth + (spacing/2), marginTop + chartHeight + 25);

            x += groupWidth;
        }

        // --- DRAW AXIS TITLES ---
        drawAxisTitles(g2, width, height, marginLeft, marginBottom);

        // --- DRAW LEGEND ---
        drawLegend(g2, width);
    }

    private void drawAxisTitles(Graphics2D g2, int width, int height, int marginLeft, int marginBottom) {
        g2.setColor(TEXT_COLOR);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // X-Axis Title (Centered at the very bottom)
        String xTitle = "Graph Size (Number of Nodes)";
        drawCenteredString(g2, xTitle, width / 2 + (marginLeft/2), height - 20); // Moved up slightly

        // Y-Axis Title (Rotated)
        String yTitle = "Efficiency (Nodes Visited)";
        AffineTransform defaultAt = g2.getTransform();
        AffineTransform at = new AffineTransform();
        at.rotate(-Math.PI / 2);
        g2.setTransform(at);
        g2.drawString(yTitle, -(height / 2) - 100, 20);
        g2.setTransform(defaultAt);
    }

    private void drawLegend(Graphics2D g2, int width) {
        int iconSize = 15;
        int legendY = 30;

        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Dijkstra
        g2.setColor(COLOR_DIJKSTRA);
        g2.fillRoundRect(width/2 - 150, legendY, iconSize, iconSize, 5, 5);
        g2.setColor(TEXT_COLOR);
        g2.drawString("Dijkstra", width/2 - 130, legendY + 12);

        // A*
        g2.setColor(COLOR_ASTAR);
        g2.fillRoundRect(width/2 - 30, legendY, iconSize, iconSize, 5, 5);
        g2.setColor(TEXT_COLOR);
        g2.drawString("A* (Heuristic)", width/2 - 10, legendY + 12);
    }

    private void drawCenteredString(Graphics2D g, String text, int x, int y) {
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        g.drawString(text, x - (textWidth / 2), y);
    }
}