package com.data_management;
import java.io.BufferedReader;
import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class FileReader implements DataReader{
    private String fileName;

    public FileReader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException{
        try {
            File file = new File(fileName);
            Reader reader = new java.io.FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length != 4) {
                    bufferedReader.close();
                    throw new IOException("Invalid data format in file: " + fileName);
                }
                parts[0] = parts[0].substring("Patient ID: ".length());
                parts[1] = parts[1].substring("Timestamp: ".length());
                parts[2] = parts[2].substring("Label: ".length());
                parts[3] = parts[3].substring("Data: ".length());
                int patientId = Integer.parseInt(parts[0]);
                long timestamp = Long.parseLong(parts[1]);
                String recordType = parts[2];
                double measurementValue = Double.parseDouble(parts[3]);
                dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
            }
            bufferedReader.close();               
        } catch (Exception e) {
            throw new IOException("Error reading data from file: " + fileName, e);
        }
    }
    
    public static void main(String[] args) {
        DataStorage storage = new DataStorage();
        DataReader reader = new FileReader("output\\ECG.txt");
        System.out.println(storage.getAllPatients().size());
        try {
            reader.readData(storage);
        } catch (Exception e) {
            // TODO: handle exception
        }
        List<Patient> patients = storage.getAllPatients();
        System.out.println(patients.size());
        }
}
