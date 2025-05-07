package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.BloodOxygenAlert;
import com.alerts.BloodPressureAlert;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BloodPressureStrategy implements AlertStrategy {
    public Queue<PatientRecord> BP = new LinkedList<>();
    // stores queue of 3 previous blood pressure records
    public boolean hypotensive = false;

    @Override
    public Alert checkAlert(PatientRecord record) {

        if (BP.size() < 2) {
            BP.add(record);
        } else {
            List<PatientRecord> BPList = new ArrayList<>(BP);

            double val1 = BPList.get(0).getMeasurementValue();
            double val2 = BPList.get(1).getMeasurementValue();
            double val3 = record.getMeasurementValue();

            double delta1 = val2 - val1;
            double delta2 = val3 - val2;


            // check if all changes are > 10 mmHg and in same direction
            if (Math.abs(delta1) > 10 && Math.abs(delta2) > 10 &&
                    ((delta1 > 0 && delta2 > 0) || (delta1 < 0 && delta2 < 0))) {

                System.out.println("Alert: BP trend detected for patient " + record.getPatientId());
                BloodPressureAlert alert = new BloodPressureAlert(String.valueOf(record.getPatientId()),
                        "Sudden change in blood pressure detected", System.currentTimeMillis());
                return alert;
            }

            // Update queue
            BP.remove();
            BP.add(record);
        }
        if (record.getRecordType().equals("Systolic Blood Pressure")) {
            // check if the patient is hypotensive (systolic BP < 90 mmHg)
            if (record.getMeasurementValue() < 90) {
                hypotensive = true;
            } else {
                hypotensive = false;
            }
            // CRITICAL TRESHOLD ALERT
            if (record.getMeasurementValue() > 180 || record.getMeasurementValue() < 90) {
                // Output the alert
                System.out.println("Alert: Critical blood pressure level detected for patient " + record.getPatientId());
                BloodPressureAlert alert = new BloodPressureAlert(String.valueOf(record.getPatientId()), "Critical blood pressure level detected", System.currentTimeMillis());
                return alert;
            }
        } else if (record.getRecordType().equals("Diastolic Blood Pressure")){
        // CRITICAL TRESHOLD ALERT
            if (record.getMeasurementValue() > 120 || record.getMeasurementValue() < 60) {
                // Output the alert
                System.out.println("Alert: Critical blood pressure level detected for patient " + record.getPatientId());
                BloodPressureAlert alert = new BloodPressureAlert(String.valueOf(record.getPatientId()), "Critical blood pressure level detected", System.currentTimeMillis());
                return alert;
            }
        }

        return null;
    }
}
