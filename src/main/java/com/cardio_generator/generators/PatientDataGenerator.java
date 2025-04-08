package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;
/**
 * The {@code PatientDataGenerator} interface defines a contract for generating
 * health data for patients. Implementing classes must provide a method to
 * generate data for a specific patient and output it using a specified output
 */
public interface PatientDataGenerator {
    void generate(int patientId, OutputStrategy outputStrategy);
}
