package alerts;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AlertGenerationTest {
    @Test
    void testBloodPressureIncrease() {
        Patient patient = new Patient(1);
        patient.addRecord(180, "BloodPressure", 1714376789050L);
        patient.addRecord(200, "BloodPressure", 1714376789050L);
        patient.addRecord(220, "BloodPressure", 1714376789050L);
        DataStorage dataStorage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(patient);
        Alert alert = alertGenerator.getLastAlert();
        assertNotNull(alert);
    }
    @Test
    void testBloodPressureDecrease() {
        Patient patient = new Patient(1);
        patient.addRecord(100, "BloodPressure", 1714376789050L);
        patient.addRecord(80, "BloodPressure", 1714376789050L);
        patient.addRecord(60, "BloodPressure", 1714376789050L);
        DataStorage dataStorage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(patient);
        Alert alert = alertGenerator.getLastAlert();
        assertNotNull(alert);
    }
    @Test
    void testCriticalThresholds() {
        Patient patient = new Patient(1);
        DataStorage dataStorage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);

        patient.addRecord(159, "SystolicBloodPressure", 1714376789050L);
        patient.addRecord(159, "SystolicBloodPressure", 1714376789050L);
        patient.addRecord(119, "DiastolicBloodPressure", 1714376789050L);
        patient.addRecord(61, "DiastolicBloodPressure", 1714376789050L);

        alertGenerator.evaluateData(patient);

        Alert alert = alertGenerator.getLastAlert();
        assertNotNull(alert);
    }
}
