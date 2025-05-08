package com.alerts;

// Represents an alert
public class Alert implements Alertable{
    public int sent;
    private String patientId;
    private String condition;
    private long timestamp;

    public Alert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
        this.sent = 0;
    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public String getCondition() {
        return condition;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }
    public void send() {
        sent++;
    }

    public int wasSent() {
        return sent;
    }
}
