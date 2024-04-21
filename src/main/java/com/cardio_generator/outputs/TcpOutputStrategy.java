package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * TcpOutputStrategy is an implementation of the OutputStrategy interface that outputs data 
  to a TCP socket.
 * It listens for incoming connections on the specified port and sends data to the connected 
  client.
 * this class makes it possible to stream  the simulated data to WebSocket clients connected 
  on the specified port.
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    /**
     * Creates a new TcpOutputStrategy that listens for incoming connections on the specified port.
     * It tries to connect to the client in a new thread to not block the main thread.
     * @param port the port to listen for incoming connections
     * @throws IOException if an I/O error occurs when creating the ServerSocket
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Outputs the patient data to the connected client.
     * The data is written in the format: "Patient ID: [patientId], Timestamp: [timestamp], Label: [label], Data: [data]"
     * @param patientId the ID of the patient
     * @param timestamp the timestamp of the data
     * @param label the label of the data
     * @param data the data
     * @return void
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
