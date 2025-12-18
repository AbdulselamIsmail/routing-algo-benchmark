package algoritmaProje;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class StatsEngine {

    /**
     * Reads the CSV and calculates average for a specific metric.
     * metricType = "VISITED" (Index 3) or "TIME" (Index 2)
     */
    public Map<Integer, Map<String, Double>> getStats(String csvFile, String metricType) {
        Map<Integer, Map<String, List<Double>>> accumulator = new TreeMap<>();

        // CSV Indices: Algorithm(0), Size(1), Time(2), Visited(3), Cost(4)
        int targetIndex = metricType.equals("TIME") ? 2 : 3;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line = br.readLine(); // Skip Header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                String algo = parts[0];
                int size = Integer.parseInt(parts[1]);
                double value = Double.parseDouble(parts[targetIndex]);

                // CONVERSION: If looking at Time (ns), convert to Milliseconds (ms)
                if (metricType.equals("TIME")) {
                    value = value / 1_000_000.0;
                }

                accumulator.putIfAbsent(size, new HashMap<>());
                accumulator.get(size).putIfAbsent(algo, new ArrayList<>());
                accumulator.get(size).get(algo).add(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }

        // Calculate Averages
        Map<Integer, Map<String, Double>> averages = new TreeMap<>();
        for (int size : accumulator.keySet()) {
            averages.put(size, new HashMap<>());
            for (String algo : accumulator.get(size).keySet()) {
                List<Double> values = accumulator.get(size).get(algo);
                double sum = 0;
                for (double v : values) sum += v;
                averages.get(size).put(algo, sum / values.size());
            }
        }
        return averages;
    }
}