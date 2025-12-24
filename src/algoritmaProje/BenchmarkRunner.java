package algoritmaProje;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BenchmarkRunner {
    public static void main(String[] args) {
        System.out.println("--- STARTING NETWORK SIMULATION ---");

        // 1. Setup Tools
        GraphGenerator generator = new GraphGenerator();
        CsvWriter writer = new CsvWriter();
        PathFinder dijkstra = new DijkstraOptimized();
        PathFinder aStar = new AStarOptimized();
        PathFinder bellman = new BellmanFord();
        List<SimulationResult> results = new ArrayList<>();
        Random rand = new Random();

        // 2. Define Test Sizes (The "Big Data" part)
        int[] sizes = {100, 500, 1000, 2000};

        // 3. The Simulation Loop
        for (int size : sizes) {
            System.out.print("Simulating Graph Size: " + size + " ... ");

            // Create fresh graph
            Graph graph = generator.generateGraph(size);
            List<Node> nodes = new ArrayList<>(graph.getNodes());

            // Run 100 races per size to get a good average
            for (int i = 0; i < 100; i++) {
                Node start = nodes.get(rand.nextInt(nodes.size()));
                Node end = nodes.get(rand.nextInt(nodes.size()));

                // Run both algorithms on the exact same start/end points
                results.add(dijkstra.run(graph, start, end));
                results.add(aStar.run(graph, start, end));
                results.add(bellman.run(graph, start, end));
            }
            System.out.println("Done.");
        }

        // 4. Save to File
        writer.writeToCSV("benchmark_results.csv", results);
        System.out.println("--- COMPLETE. Check ProjectResults.csv ---");
    }
}