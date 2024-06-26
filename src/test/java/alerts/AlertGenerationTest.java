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
    void testLowBloodSaturation() {
        Patient patient = new Patient(4);
        DataStorage dataStorage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);

        long time = System.currentTimeMillis() - 1000*60*9;
        patient.addRecord(91, "Saturation", time);
        alertGenerator.evaluateData(patient);
        Alert alert1 = alertGenerator.getLastAlert();
        assert alert1.equals(new Alert("4", "Low Saturation", time));

        time = System.currentTimeMillis() - 1000*60*7;
        patient.addRecord(92, "Saturation", time);
        alertGenerator.evaluateData(patient);
        Alert alert2 = alertGenerator.getLastAlert();
        assert alert2.equals(new Alert("4", "Low Saturation", time));

        time = System.currentTimeMillis() - 1000*60*3;
        patient.addRecord(93, "Saturation", time);
        alertGenerator.evaluateData(patient);
        Alert alert3 = alertGenerator.getLastAlert();
        assert alert3.equals(new Alert("4", "Low Saturation", time));

        time = System.currentTimeMillis();
        patient.addRecord(94, "Saturation", time);
        alertGenerator.evaluateData(patient);
        Alert alert4 = alertGenerator.getLastAlert();
        assert alert4.equals(new Alert("4", "Low Saturation", time));
    }

    @Test
    void testRapidSaturationDrop() {
        Patient patient = new Patient(5);
        DataStorage dataStorage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);

        long time = System.currentTimeMillis() - 1000*60*9;
        patient.addRecord(100, "Saturation", time);
        alertGenerator.evaluateData(patient);
        Alert alert = alertGenerator.getLastAlert();
        assert alert == null;

        time = System.currentTimeMillis() - 1000*60*7;
        patient.addRecord(99, "Saturation", time);
        alertGenerator.evaluateData(patient);
        alert = alertGenerator.getLastAlert();
        assert alert == null;

        time = System.currentTimeMillis() - 1000*60*3;
        patient.addRecord(98, "Saturation", time);
        alertGenerator.evaluateData(patient);
        alert = alertGenerator.getLastAlert();
        assert alert == null;

        time = System.currentTimeMillis();
        patient.addRecord(95, "Saturation", time);
        alertGenerator.evaluateData(patient);
        alert = alertGenerator.getLastAlert();
        assert alert.equals(new Alert("5", "Rapid drop", time));
    }

    @Test
    void testHypotensiveHypoxemia(){
        DataStorage dataStorage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        long time = System.currentTimeMillis();
        Patient patient = new Patient(6);

        patient.addRecord(91, "Saturation", time);
        alertGenerator.evaluateData(patient);
        Alert alert = alertGenerator.getLastAlert();
        assert alert.equals(new Alert("6", "Low Saturation", time));

        patient.addRecord(89, "SystolicPressure", time);
        alertGenerator.evaluateData(patient);
        alert = alertGenerator.getLastAlert();
        assert alert.equals(new Alert("6", "Hypotensive Hypoxemia", time));
    }

    @Test
    void testECG() {
        DataStorage dataStorage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        Patient patient = new Patient(7);
        long time = System.currentTimeMillis();
        patient.addRecord(0, "ECG", time);
        patient.addRecord(2, "ECG", time+1);
        alertGenerator.evaluateData(patient);
        Alert alert = alertGenerator.getLastAlert();
        assert alert.equals(new Alert("7", "Abnormal ECG", time+1));
    }

}
