package com.data_management;

import java.io.BufferedReader;
import java.io.IOException;

import javax.xml.crypto.Data;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileDataReader implements DataReader {
    private Path filePath;

    public FileDataReader(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        // Implement the logic to read data from the file at filePath
        // and store it in the dataStorage.
        // This is a placeholder implementation.
        System.out.println("Reading data from file: " + filePath);
        // tries to read file, throws exception if not possible
        try{
            Files.walk(filePath).forEach(file -> processFile(file, dataStorage));
        } catch (IOException e) {
            System.err.println("Error reading data directory: " + e.getMessage());
        }
    }

    private void processFile(Path file, DataStorage dataStorage) {
        // Implement the logic to process each file and store the data in dataStorage.
        // This is a placeholder implementation.
        System.out.println("Processing file: " + file.toString());
        // reads file line by line and parses it into PatientRecord objects
        try (BufferedReader br = Files.newBufferedReader(file)) {
            String line;
            while ((line = br.readLine()) != null) {
                PatientRecord record = parseLineToRecord(line);
                // checks if the record is not null and adds it to the data storage
                if (record != null) {
                    dataStorage.addPatientData(record.getPatientId(), record.getMeasurementValue(), record.getRecordType(), record.getTimestamp());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath + ": " + e.getMessage());
        }
    }
    public PatientRecord parseLineToRecord(String line) throws IOException {
        // Implement the logic to parse a line of data into a PatientRecord object.
        // Assuming the line format is: patientId,measurementValue,recordType,timestamp
        // Example: 1,100.0,WhiteBloodCells,1714376789050
        String[] parts = line.split(","); // Split the line by commas
        // checks if the line is in the correct format by checking array length
        if (parts.length != 4) {
            System.err.println("Invalid line format: " + line);
            throw new IOException("Invalid line format: " + line);
        }
        // assigns each array element to a variable
        int patientId = Integer.parseInt(parts[0]);
        double measurementValue = Double.parseDouble(parts[1]);
        String recordType = parts[2];
        long timestamp = Long.parseLong(parts[3]);
        // creates a new PatientRecord object and returns it
        return new PatientRecord(patientId, measurementValue, recordType, timestamp);
    }
    
    public Path getFilePath() {
        return filePath;
    }

}
