package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;
/**
 * BloodSaturationDataGenerator is an implementation of the PatientDataGenerator interface that 
 generates blood saturation data for patients.
 * It simulates blood saturation values for each patient and outputs the data using the specified 
 output strategy.
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {
    private static final Random random = new Random();
    private int[] lastSaturationValues;

    /**
     * Creates a new BloodSaturationDataGenerator with the specified number of patients.
     * Initializes the lastSaturationValues array with baseline saturation values for each patient.
     *
     * @param patientCount the number of patients 
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }

    /**
     * Generates blood saturation data for the specified patient ID and outputs it using the 
     specified output strategy.
     * The data is written in the format: "Patient ID: [patientId], Timestamp: [timestamp], Label: 
     [label], Data: [data]"
     * If an error occurs while generating the data, it prints an error message.
     *
     * @param patientId      the ID of the patient
     * @param outputStrategy the output strategy to use for generating the data
     * @throws Exception prints the stack trace to help identify where the error occurred
     * @return void
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
