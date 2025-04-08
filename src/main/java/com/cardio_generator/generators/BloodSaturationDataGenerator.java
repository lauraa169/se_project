package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
     * The {@code BloodSaturationDataGenerator} class implements the {@link PatientDataGenerator}
     * interface to generate simulated blood saturation data for patients. It uses a random number generator
     * to create small fluctuations in blood saturation values, simulating realistic changes over time.
     */

public class BloodSaturationDataGenerator implements PatientDataGenerator {
    private static final Random random = new Random();
    private int[] lastSaturationValues;

    /**
     *  * Constructor for BloodSaturationDataGenerator.
     * Initializes the generator with a specified number of patients and sets baseline saturation values.
     * @param patientCount
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }

    /**
     *  * Generates blood saturation data for a specific patient.
     * This method simulates small fluctuations in blood saturation values and outputs the data using the provided output strategy.
     * @param patientId The ID of the patient for whom to generate data.
     * @param outputStrategy The strategy to use for outputting the generated data.
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}
