package algoritmaProje;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CsvWriter {
    public void writeToCSV(String filename, List<SimulationResult> results) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write the Header Row
            writer.println("Algorithm,GraphSize,Time(ns),NodesVisited,PathCost");

            // Write the Data Rows
            for (SimulationResult res : results) {
                writer.println(res.toCSV());
            }
            System.out.println("Data saved to: " + filename);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}