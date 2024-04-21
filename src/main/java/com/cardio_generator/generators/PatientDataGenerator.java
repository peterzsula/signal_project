package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
    * Generates patient data for the given patient ID using the specified output strategy.
    *
    * @param patientId      the ID of the patient
    * @param outputStrategy the output strategy to use for generating the data
    */
public interface PatientDataGenerator {
    void generate(int patientId, OutputStrategy outputStrategy);
}
