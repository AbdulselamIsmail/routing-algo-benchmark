package algoritmaProje;

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Stroke;

public class Style {
    // 1. Network Base Colors
    public static final Color CANVAS_BACKGROUND = Color.WHITE;
    public static final Color EDGE_COLOR = new Color(220, 220, 220); // Light Gray
    public static final Color NODE_COLOR = Color.GRAY; // Unvisited nodes

    // 2. Algorithm States
    // "Visited" nodes (The ones popped from the queue)
    // We use 'alpha' (150) to make it see-through/transparent
    public static final Color VISITED_COLOR = new Color(255, 200, 0, 150); // Gold/Yellow

    // The Final Path found by the algorithm
    public static final Color PATH_COLOR = new Color(0, 102, 204); // Strong Blue

    // 3. Landmarks
    public static final Color START_NODE_COLOR = new Color(50, 205, 50); // Lime Green
    public static final Color END_NODE_COLOR = new Color(220, 20, 60);   // Crimson Red

    // 4. Line Thickness
    public static final Stroke THIN_LINE = new BasicStroke(1);
    public static final Stroke THICK_LINE = new BasicStroke(3);
}