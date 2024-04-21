package com.cardio_generator.generators;

import java.util.Random; //deleted an empty line
import com.cardio_generator.outputs.OutputStrategy;

public class AlertGenerator implements PatientDataGenerator {

    public static final Random randomGenerator = new Random();
    private boolean[] alertStates; // false = resolved, true = pressed 
    //renamed AlertStates to alertStates to fit the camelCase naming convention

    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

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
