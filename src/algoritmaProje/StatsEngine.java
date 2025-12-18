package algoritmaProje;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class StatsEngine {

    // Holds data like: 1000 -> { "Dijkstra": 550.0, "A*": 120.0 }
    public Map<Integer, Map<String, Double>> calculateAverages(String csvFile) {
        Map<Integer, Map<String, List<Integer>>> accumulator = new TreeMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line = br.readLine(); // Skip Header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // Expected: Algorithm, Size, Time, Visited, Cost
                if (parts.length < 4) continue;

                String algo = parts[0];
                int size = Integer.parseInt(parts[1]);
                int visited = Integer.parseInt(parts[3]); // We chart "Nodes Visited" (Efficiency)

                // Initialize maps if empty
                accumulator.putIfAbsent(size, new HashMap<>());
                accumulator.get(size).putIfAbsent(algo, new ArrayList<>());

                // Add value to list
                accumulator.get(size).get(algo).add(visited);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // Calculate Averages
        Map<Integer, Map<String, Double>> averages = new TreeMap<>();
        for (int size : accumulator.keySet()) {
            averages.put(size, new HashMap<>());
            for (String algo : accumulator.get(size).keySet()) {
                List<Integer> values = accumulator.get(size).get(algo);
                double sum = 0;
                for (int v : values) sum += v;
                averages.get(size).put(algo, sum / values.size());
            }
        }
        return averages;
    }
}