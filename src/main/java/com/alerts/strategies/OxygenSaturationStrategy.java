package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.BloodOxygenAlert;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class OxygenSaturationStrategy implements AlertStrategy {
    public Queue<PatientRecord> BS = new LinkedList<>();

    @Override
    public Alert checkAlert(PatientRecord record){
        if (record.getMeasurementValue() < 90) {
            // Output the alert
            System.out.println("Alert: Low blood saturation level detected for patient " + record.getPatientId());
            BloodOxygenAlert alert = new BloodOxygenAlert(String.valueOf(record.getPatientId()), "Low blood saturation level detected", System.currentTimeMillis());
            return alert;
        }
        // RAPID DROP ALERT
        // if less than two previous records, add the current record to the queue
        if (BS.size() < 2){
            BS.add(record);
        } else{
            // convert queue to a list for easier access
            List<PatientRecord> BSList = new ArrayList<>(BS);
            long time = 0;
            double drop = 0;
            boolean timeExceeded = false;
            // check if there's a drop of 5% in blood saturation level within 10m
            for (int j = 0; j < BSList.size()-1; j++) {
                time += BSList.get(j).getTimestamp() - BSList.get(j+1).getTimestamp();
                if (time <= 600000) { // 10 minutes in milliseconds
                    drop += BSList.get(j).getMeasurementValue() - BSList.get(j+1).getMeasurementValue();
                    if (drop <= -5){
                        // Output the alert
                        System.out.println("Alert: Rapid drop in blood saturation level detected for patient " + record.getPatientId());
                        BloodOxygenAlert alert = new BloodOxygenAlert(String.valueOf(record.getPatientId()), "Rapid drop in blood saturation level detected", System.currentTimeMillis());
                        return alert;
                    }
                    timeExceeded = true;
                }
            }
            if (timeExceeded){
                // remove the oldest record from the queue
                BS.remove();
            }
            // add the new record to the queue
            BS.add(record);
        }

        return null;
    }
}
