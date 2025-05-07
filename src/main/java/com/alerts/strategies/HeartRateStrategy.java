package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.ECGAlert;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class HeartRateStrategy implements AlertStrategy {
    public Queue<PatientRecord> ECG = new LinkedList<>();

    @Override
    public Alert checkAlert(PatientRecord record){
        if (ECG.size() < 5){
            ECG.add(record);
        } else{
            // convert queue to a list for easier access
            List<PatientRecord> ECGList = new ArrayList<>(ECG);
            double sum = 0;
            for (int j = 0; j < ECGList.size(); j++) {
                sum += ECGList.get(j).getMeasurementValue();
            }
            double average = sum / ECGList.size();
            // check if the current record is abnormal (more than 0.5 from the average of the last 5 records)
            if (Math.abs(record.getMeasurementValue()-average) > 0.5){
                // Output the alert
                System.out.println("Alert: Abnormal ECG detected for patient " + record.getPatientId());
                ECGAlert alert = new ECGAlert(String.valueOf(record.getPatientId()), "Abnormal ECG detected", System.currentTimeMillis());
                return alert;
            }
            // remove the oldest record from the queue
            ECG.remove();
            // add the new record to the queue
            ECG.add(record);
        }
        return null;
    }
}
