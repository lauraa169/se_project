package com.data_management;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.lang.Record;
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
        System.out.println("Disconnected from server");
    }

    @Override
    public void onMessage(String s) {
        PatientRecord record = parseMessage(s);
        dataStorage.addPatientData(record.getPatientId(), record.getMeasurementValue(), record.getRecordType(), record.getTimestamp());
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("Error: " + ex.getMessage());
    }

    public PatientRecord parseMessage(String message) {
        String[] parts = message.split(",");
        int patientId = Integer.parseInt(parts[0]);
        long timestamp = Long.parseLong(parts[1]);
        String recordType = parts[2];
        double measurementValue = Double.parseDouble(parts[3]);
        return new PatientRecord(patientId, measurementValue, recordType, timestamp);
    }
}
