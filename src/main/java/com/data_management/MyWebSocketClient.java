package com.data_management;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
public class MyWebSocketClient extends WebSocketClient implements DataReader {
    private DataStorage dataStorage;

    public MyWebSocketClient(URI serverUri, DataStorage dataStorage) {
        super(serverUri);
        this.dataStorage = dataStorage;
    }
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to server");
    }

    @Override
    public void connect(DataStorage dataStorage) {
        // calls connect method from super class
        this.connect();
    }
    public void onClose(int code, String reason, boolean remote) {
        // close client
        System.out.println("Disconnected from server");
    }

    @Override
    public void onMessage(String message) {
        // receives a message from websocket, parses the data, and stores in dataStorage
        PatientRecord record = parseMessage(message);
        // adds the patient record to storage
        dataStorage.addPatientData(record.getPatientId(), record.getMeasurementValue(), record.getRecordType(), record.getTimestamp());
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("Error: " + ex.getMessage());
    }

    public PatientRecord parseMessage(String message) {
        // receives a message from the websocket and parses onto a patient record object
        String[] parts = message.split(",");
        // message comes in a comma-separated list
        // check the amount of arguemnts in the message (should be 4) (error handling)
        if (parts.length != 4) {
            System.err.println("Invalid message format: " + message);
            throw new IllegalArgumentException("Invalid message format: " + message);
        } else{
            // if message is valid parse onto a PatientRecord
            int patientId = Integer.parseInt(parts[0]);
            long timestamp = Long.parseLong(parts[1]);
            String recordType = parts[2];
            double measurementValue = Double.parseDouble(parts[3]);
            return new PatientRecord(patientId, measurementValue, recordType, timestamp);
        }

    }
}
