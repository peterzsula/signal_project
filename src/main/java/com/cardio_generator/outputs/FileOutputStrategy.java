package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

//it was lowercase, the program couldn't compile.
/**
 * FileOutputStrategy is an implementation of the OutputStrategy interface that outputs data to files.
 * It writes data to files in the specified base directory.
 * The data is written in the format: "Patient ID: [patientId], Timestamp: [timestamp], Label: [label], Data: [data]"
 
 */
public class FileOutputStrategy implements OutputStrategy {

    //BaseDirectory -> baseDirectory. The first letter of a non constant variable needs to be lowercase
    private String baseDirectory;

    //file_map was lowercase. Since it is final, it needs to be uppercase.
    public final ConcurrentHashMap<String, String> FILE_MAP = new ConcurrentHashMap<>();

    /**
     * Creates a new FileOutputStrategy with the specified base directory.
     *
     * @param baseDirectory the base directory where the files will be written
     */
    //it was lowercase, the program couldn't compile.
    public FileOutputStrategy(String baseDirectory) { //removed an empty line from below
        this.baseDirectory = baseDirectory;
    }

    /**
     * The output method tries to create a directory, if it fails, it prints an error message.
     * It sets the filePath variable and writes the data to the file.
     * This class creates the files in the output directory.
     * The data is written in the format: "Patient ID: [patientId], Timestamp: [timestamp], Label: [label], Data: [data]"
     * If an error occurs while writing to the file, it prints an error message.
     * @param patientId the ID of the patient
     * @param timestamp the timestamp of the data
     * @param label     the label of the data
     * @param data      the data to output
     * @throws IOException if an error occurs while writing to the file
     * @return void
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the filePath variable
        //file_map was lowercase. Since it is final, it needs to be uppercase.
        //FilePath -> filepath. The first letter of a non constant variable needs to be lowercase
        String filePath = FILE_MAP.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                /**the line was longer than 100 columns, so I divided it in 2 lines
                * needs to be lowercase
                */
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, 
                StandardOpenOption.APPEND))) {
            //the line was longer than 100 columns, so I divided it in 2 lines
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId,
            timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}