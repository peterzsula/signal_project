package com.cardio_generator.generators;

import java.util.Random; //deleted an empty line
import com.cardio_generator.outputs.OutputStrategy;

/**
 * The AlertGenerator class is responsible for generating alerts for patients.
 * It implements the PatientDataGenerator interface.
 */
public class AlertGenerator implements PatientDataGenerator {

    public static final Random randomGenerator = new Random();
    private boolean[] alertStates; // false = resolved, true = pressed 
    //renamed AlertStates to alertStates to fit the camelCase naming convention

    /**
     * Creates a new AlertGenerator with the specified number of patients.
     * Initializes the alertStates array with the specified number of patients plus one.
     *
     * @param patientCount the number of patients
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Generates alerts for the specified patient ID and outputs them using the specified output strategy.
     * The data is written in the format: "Patient ID: [patientId], Timestamp: [timestamp], Label: [label], Data: [data]"
     * If an error occurs while generating the data, it prints an error message.
     *
     * @param patientId      the ID of the patient
     * @param outputStrategy the output strategy to use for generating the data
     * @throws Exception prints an error message and patient ID to help identify where the error occurred
     * @return void
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), //line wrapping
                    "Alert", "resolved");
                }
            } else {
                // Average rate (alerts per period), adjust based on desired frequency
                double lambda = 0.1; //line wrapping below, rename to lambda
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), //line wrapping
                    "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            //line wrapping
            System.err.println("An error occurred while generating alert data for patient " 
            + patientId);
            e.printStackTrace();
        }
    }
}
