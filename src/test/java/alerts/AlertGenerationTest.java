package alerts;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AlertGenerationTest {
    @Test
    void testBloodPressureIncreasingTrend() {
        DataStorage dataStorage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        Patient patient = new Patient(1);
        patient.addRecord(100, "SystolicPressure", 1);
        patient.addRecord(120, "SystolicPressure", 1);
        patient.addRecord(140, "SystolicPressure", 1);

        alertGenerator.evaluateData(patient);
        Alert alert = alertGenerator.getLastAlert();
        assert alert.equals(new Alert("1", "Trend", 1));
    }
    @Test
    void testBloodPressureDecreasingTrend() {
        DataStorage dataStorage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        Patient patient = new Patient(2);
        patient.addRecord(140, "SystolicPressure", 2);
        patient.addRecord(120, "SystolicPressure", 2);
        patient.addRecord(100, "SystolicPressure", 2);


        alertGenerator.evaluateData(patient);
        Alert alert = alertGenerator.getLastAlert();
        assert alert.equals(new Alert("2", "Trend", 2));
    }

    @Test
    void testCriticalThresholds() {
        DataStorage dataStorage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        Patient patient = new Patient(3);

        patient.addRecord(181, "SystolicPressure", 3);
        alertGenerator.evaluateData(patient);
        Alert alert = alertGenerator.getLastAlert();
        assert alert.equals(new Alert("3", "Critical threshold", 3));

        patient.addRecord(89, "SystolicPressure", 3);
        alertGenerator.evaluateData(patient);
        alert = alertGenerator.getLastAlert();
        assert alert.equals(new Alert("3", "Critical threshold", 3));

        patient.addRecord(121, "DiastolicPressure", 3);
        alertGenerator.evaluateData(patient);
        alert = alertGenerator.getLastAlert();
        assert alert.equals(new Alert("3", "Critical threshold", 3));

        patient.addRecord(59, "DiastolicPressure", 3);
        alertGenerator.evaluateData(patient);
        alert = alertGenerator.getLastAlert();
        assert alert.equals(new Alert("3", "Critical threshold", 3));
    }
    @Test
    void testBloodSaturationThresholds() {
        Patient patient = new Patient(1);
        DataStorage dataStorage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);

        patient.addRecord(91, "BloodSaturation", System.currentTimeMillis() - 1000*60*10);
        patient.addRecord(90, "BloodSaturation", System.currentTimeMillis() - 1000*60*7);
        patient.addRecord(89, "BloodSaturation", System.currentTimeMillis() - 1000*60*3);
        patient.addRecord(85, "BloodSaturation", System.currentTimeMillis());

        alertGenerator.evaluateData(patient);

        Alert alert = alertGenerator.getLastAlert();
        assertNotNull(alert);
    }
}
