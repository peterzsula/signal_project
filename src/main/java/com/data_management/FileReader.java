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
                String[] parts = line.split(",");
                if (parts.length != 4) {
                    bufferedReader.close();               
                    throw new IOException("Invalid data format in file: " + fileName);
                }
                System.out.println(parts[0]);
                parts[0] = parts[0].
                int patientId = Integer.parseInt(parts[0]);
                System.out.println(patientId);
                double measurementValue = Double.parseDouble(parts[1]);
                String recordType = parts[2];
                long timestamp = Long.parseLong(parts[3]);
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
        try {
            reader.readData(storage);
        } catch (Exception e) {
            // TODO: handle exception
        }
        List<Patient> patients = (ArrayList<Patient>)storage.getAllPatients();
        for (Patient patient : patients) {
            System.out.println(patient);
            }
        }
}
