package com.alerts.factories;

import com.alerts.BloodPressureAlert;

public class BloodPressureAlertFactory extends AlertFactory {
    @Override
    public BloodPressureAlert createAlert(String patientId, String condition, long timestamp) {
        return new BloodPressureAlert(patientId, condition, timestamp);
    }
}
