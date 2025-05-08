package com.alerts;

public interface Alertable {
    String getPatientId();
    String getCondition();
    long getTimestamp();
}
