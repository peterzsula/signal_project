package com.cardio_generator.outputs;

/**
 * The OutputStrategy interface represents a strategy for outputting data.
 * Implementations of this interface define how data should be outputted.
 */
public interface OutputStrategy {
    /**
     * Outputs the given data.
     *
     * @param patientId the ID of the patient
     * @param timestamp the timestamp of the data
     * @param label the label of the data
     * @param data the data to be outputted
     */
    void output(int patientId, long timestamp, String label, String data);
}
