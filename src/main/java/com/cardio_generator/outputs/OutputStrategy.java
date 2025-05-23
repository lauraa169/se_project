package com.cardio_generator.outputs;
/**
 * The {@code OutputStrategy} interface defines a contract for outputting health
 * data generated by the {@link HealthDataSimulator}. Implementing classes must
 * provide a method to output data in various formats (console, file, WebSocket,
 * or TCP socket).
 */

public interface OutputStrategy {
    void output(int patientId, long timestamp, String label, String data);
}
