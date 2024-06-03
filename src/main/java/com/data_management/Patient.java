package com.data_management;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient and manages their medical records.
 * This class stores patient-specific data, allowing for the addition and
 * retrieval
 * of medical records based on specified criteria.
 */
public class Patient {
    private int patientId;
    private List<PatientRecord> patientRecords;

    /**
     * Constructs a new Patient with a specified ID.
     * Initializes an empty list of patient records.
     *
     * @param patientId the unique identifier for the patient
     */
    public Patient(int patientId) {
        this.patientId = patientId;
        this.patientRecords = new ArrayList<>();
    }

    /**
     * Adds a new record to this patient's list of medical records.
     * The record is created with the specified measurement value, record type, and
     * timestamp.
     *
     * @param measurementValue the measurement value to store in the record
     * @param recordType       the type of record, e.g., "HeartRate",
     *                         "BloodPressure"
     * @param timestamp        the time at which the measurement was taken, in
     *                         milliseconds since UNIX epoch
     */
    public void addRecord(double measurementValue, String recordType, long timestamp) {
        PatientRecord record = new PatientRecord(this.patientId, measurementValue, recordType, timestamp);
        this.patientRecords.add(record);
    }

    /**
     * Retrieves a list of PatientRecord objects for this patient that fall within a
     * specified time range.
     * The method filters records based on the start and end times provided.
     *
     * @param startTime the start of the time range, in milliseconds since UNIX
     *                  epoch
     * @param endTime   the end of the time range, in milliseconds since UNIX epoch
     * @return a list of PatientRecord objects that fall within the specified time
     *         range
     */
    public List<PatientRecord> getRecords(long startTime, long endTime) {
        List<PatientRecord> recordsInRange = new ArrayList<>();
        for (PatientRecord record : this.patientRecords) {
            if (record.getTimestamp() >= startTime && record.getTimestamp() <= endTime) {
                recordsInRange.add(record);
            }
        }
        return recordsInRange;
    }

    /**
     * Retrieves the last n records for this patient.
     *
     * @return a list of the last n PatientRecord objects for this patient
     */
    public List<PatientRecord> getLastNRecords(int n) {
        ArrayList<PatientRecord> lastNRecords = new ArrayList<>();
        int size = this.patientRecords.size();
        for (int i = 0; i < n && i < size; i++) {
            lastNRecords.add(this.patientRecords.get(size - 1 - i));
        }
        return lastNRecords;
    }
    /**
     * Adds all the records that match the given label to a new list
     * @param label the label to filter the records by
     * @param records the list of records to filter
     * @return a new list of records that match the given label
     */
    public static List<PatientRecord> filterRecordsBasedOnLabel(String label, List<PatientRecord> records) {
        List<PatientRecord> filteredRecords = new ArrayList<>();
        for (PatientRecord record : records) {
            if (record.getRecordType().equals(label)) {
                filteredRecords.add(record);
            }
        }
        return filteredRecords;
    }
    /**
     * Adds all the records that match the given labels to a new list
     * @param labels the labels to filter the records by
     * @param records the list of records to filter
     * @return a new list of records that match the given labels
     */
    public static List<PatientRecord> filterRecordsBasedOnLabels(List<String> labels, List<PatientRecord> records) {
        List<PatientRecord> filteredRecords = new ArrayList<>();
        for (String label : labels) {
            filteredRecords.addAll(filterRecordsBasedOnLabel(label, records));
        }
        return filteredRecords;
    }

    public List<PatientRecord> getLastTenMinutes() {
        List<PatientRecord> recordsInRange = getRecords(System.currentTimeMillis()-1000*60*10, System.currentTimeMillis());
        return recordsInRange;
    }
}
