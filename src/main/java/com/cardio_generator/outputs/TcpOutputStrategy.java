package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class TcpOutputStrategy implements OutputStrategy {

    /**
     * * The {@code TcpOutputStrategy} class implements the {@link OutputStrategy}
     * interface to output health data over a TCP socket. It listens for incoming
     */
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    /**
     * * Constructor for TcpOutputStrategy.
     * Initializes the server socket to listen for incoming connections on the
     * specified port.
     *
     * @param port The port number to listen on for incoming TCP connections.
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
     * * Outputs health data over the TCP socket.
     * * @param patientId The ID of the patient.
     * * @param timestamp The timestamp of the data.
     * * @param label The label of the data (e.g., "ECG", "Blood Pressure").
     * * @param data The actual data to be sent over the TCP socket.
     * * This method formats the data as a CSV string and sends it to the connected
     * * client. The format is: "Patient ID,Timestamp,Label,Data".
     */

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
