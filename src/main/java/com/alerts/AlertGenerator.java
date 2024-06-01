package com.alerts;

import com.data_management.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private Alert lastAlert;
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        evaluateBloodPressure(patient);
        evaluateBloodSaturation(patient);

    }

    private void evaluateBloodSaturation(Patient patient) {
        List<PatientRecord> lastTenMinutes = patient.getLastTenMinutes();
        boolean flag = false;
        for(PatientRecord record : lastTenMinutes) {
            if(record.getMeasurementValue() < 92.0) {
                triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Low Saturation", System.currentTimeMillis()));
            }

            if((lastTenMinutes.get(0).getMeasurementValue() - record.getMeasurementValue() > 5 ||
            lastTenMinutes.get(0).getMeasurementValue() - record.getMeasurementValue() < -5) &&
            flag == false) {
                triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Rapid drop", System.currentTimeMillis()));
                flag = true;
            }
        }
    }

    /**
     * Evaluates the patient's LATEST(!) blood pressure data to determine if any alert
     * conditions are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert} method.
     *
     * @param patient the patient data to evaluate for blood pressure alert
     *                conditions
     */
    public void evaluateBloodPressure(Patient patient) {
        /* Critical Threshold Alert: Trigger an alert if the systolic blood pressure exceeds 180
         * mmHg or drops below 90 mmHg, or if diastolic blood pressure exceeds 120 mmHg or
         * drops below 60 mmHg
         */
        // Get the latest record
        PatientRecord record = patient.getLastNRecords(1).get(0);
        if (record.getRecordType().equals("SystolicPressure") &&
                (record.getMeasurementValue() > 180 || record.getMeasurementValue() < 90)){
            triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Critical threshold", record.getTimestamp()));
        }
        else if (record.getRecordType().equals("DiastolicPressure") &&
                (record.getMeasurementValue() > 120 || record.getMeasurementValue() < 60)){
            triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Critical threshold", record.getTimestamp()));
        }
        /*
         * Trend Alert: Trigger an alert if the patient's blood pressure (systolic or diastolic) shows a
         * consistent increase or decrease across three consecutive readings where each reading
         * changes by more than 10 mmHg from the last.
         */
        List<PatientRecord> records = Patient.filterRecordsBasedOnLabel(record.getRecordType(),patient.getLastNRecords(3));
        if (records.size() == 3) {
            double diff1 = Math.abs(records.get(0).getMeasurementValue() - records.get(1).getMeasurementValue());
            double diff2 = Math.abs(records.get(1).getMeasurementValue() - records.get(2).getMeasurementValue());
            if (diff1 > 10 && diff2 > 10) {
                triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Trend", record.getTimestamp()));
            }
        }
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */

    private void triggerAlert(Alert alert) {
        this.lastAlert = alert;
        System.out.println("ALERT: " + alert.toString());
    }

    public Alert getLastAlert() {
        return lastAlert;
    }

    public static void main(String[] args) {
        DataStorage storage = new DataStorage();
        DataReader reader = new FileReader("output\\ECG.txt");
        AlertGenerator alertGenerator = new AlertGenerator(storage);
        try {
            reader.readData(storage);
        } catch (Exception e) {}
        for (Patient patient : storage.getAllPatients()) {
            alertGenerator.evaluateData(patient);
        }
    }
}
