package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

public class FileOutputStrategy implements OutputStrategy { // class names in UpperCamelCase

    private String baseDirectory; // field names in lowerCamelCase

    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();
    /**
     * * Constructor for FileOutputStrategy.
     * Initializes the base directory for file output.
     * @param baseDirectory
     */
    public FileOutputStrategy(String baseDirectory) {

        this.baseDirectory = baseDirectory; // field names in lowerCamelCase
    }
    /**
     * * Outputs health data to a file.
     * * @param patientId The ID of the patient.
     * * @param timestamp The timestamp of the data.
     * * @param label The label of the data (e.g., "ECG", "Blood Pressure").
     * * @param data The actual data to be written to the file.
     * * This method creates a directory for the specified label if it doesn't exist,
     * * and appends the data to a file named after the label. The file is created
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable 
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());
        // 

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}