package com.cardio_generator.generators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import com.alerts.Alert;
import com.cardio_generator.outputs.OutputStrategy;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class AlertGenerator implements PatientDataGenerator {

    public static final Random randomGenerator = new Random();
    private boolean[] AlertStates; // false = resolved, true = pressed

    public AlertGenerator(int patientCount) {
        AlertStates = new boolean[patientCount + 1];
    }

    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (AlertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    AlertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                double Lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-Lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    AlertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
    public List<Alert> evaluateData(Patient patient, long startTime, long endTime) {
        // retrieves all the records for the patient within the specified time range to be evaluated
        List<PatientRecord> records = patient.getRecords(startTime, endTime); 
        Queue<PatientRecord> BP = new LinkedList<>();// stores queue of 3 previous blood pressure records
        Queue<PatientRecord> BS = new LinkedList<>(); // stores queue previous blood saturation records
        Queue<PatientRecord> ECG = new LinkedList<>();// stores queue previous ECG records

        List <Alert> alerts = new ArrayList<>(); // stores all alerts detected for the patient
        boolean hypotensive = false ; // flag to check if the patient is hypotensive
        boolean hypoxemic = false; // flag to check if the patient is hypoxemic

        for (int i = 0; i < records.size(); i++) {
        PatientRecord record = records.get(i);
            
            // TREND ALERT 
            // check if the record is a blood pressure record (diastolic or systolic)
            if (record.getRecordType().equals("Systolic Blood Pressure") || 
                record.getRecordType().equals("Diastolic Blood Pressure")) {
    
                // use same queue for both systolic and diastolic
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
    
                        System.out.println("Alert: BP trend detected for patient " + patient.getPatientId());
                        Alert alert = new Alert(String.valueOf(patient.getPatientId()), 
                            "Sudden change in blood pressure detected", System.currentTimeMillis());
                        alerts.add(alert);
                    }
    
                    // Update queue
                    BP.remove();
                    BP.add(record);
                }
            }
            // ALERT 1.1 FOR SYOSTOLIC BLOOD PRESSURE & ALERT 3.1 FOR HYPOTENSIVE HYPOXEMIA
            if (record.getRecordType().equals("Systolic Blood Pressure")){
                // check if the patient is hypotensive (systolic BP < 90 mmHg)
                if (record.getMeasurementValue() < 90) {
                    hypotensive = true;
                } else {
                    hypotensive = false;
                }
                // CRITICAL TRESHOLD ALERT
                if (record.getMeasurementValue() > 180 || record.getMeasurementValue() < 90) {
                    // Output the alert
                    System.out.println("Alert: Critical blood pressure level detected for patient " + patient.getPatientId());
                    Alert alert = new Alert(String.valueOf(patient.getPatientId()), "Critical blood pressure level detected", System.currentTimeMillis());
                    alerts.add(alert);
                }
            // ALERT 1.2 FOR DIASTOLIC BLOOD PRESSURE
            } else if (record.getRecordType().equals("Diastolic Blood Pressure")){
                // CRITICAL TRESHOLD ALERT
                if (record.getMeasurementValue() > 120 || record.getMeasurementValue() < 60) {
                    // Output the alert
                    System.out.println("Alert: Critical blood pressure level detected for patient " + patient.getPatientId());
                    Alert alert = new Alert(String.valueOf(patient.getPatientId()), "Critical blood pressure level detected", System.currentTimeMillis());
                    alerts.add(alert);
                }
            // ALERT 2 FOR BLOOD SATURATION & ALERT 3.2 FOR HYPOTENSIVE HYPOXEMIA
            } else if (record.getRecordType().equals("Blood Saturation")){
                // check if the patient is hypoxemic (blood saturation < 92%)
                if (record.getMeasurementValue() < 92) {
                    hypoxemic = true;
                } else {
                    hypoxemic = false;
                }
                // LOW SATURATION ALERT
                if (record.getMeasurementValue() < 90) {
                    // Output the alert
                    System.out.println("Alert: Low blood saturation level detected for patient " + patient.getPatientId());
                    Alert alert = new Alert(String.valueOf(patient.getPatientId()), "Low blood saturation level detected", System.currentTimeMillis());
                    alerts.add(alert);
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
                                System.out.println("Alert: Rapid drop in blood saturation level detected for patient " + patient.getPatientId());
                                Alert alert = new Alert(String.valueOf(patient.getPatientId()), "Rapid drop in blood saturation level detected", System.currentTimeMillis());
                                alerts.add(alert);
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
                // ALERT 4 FOR ECG
            } else if (record.getRecordType().equals("ECG")){
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
                        System.out.println("Alert: Abnormal ECG detected for patient " + patient.getPatientId());
                        Alert alert = new Alert(String.valueOf(patient.getPatientId()), "Abnormal ECG detected", System.currentTimeMillis());
                        alerts.add(alert);
                    }
                    // remove the oldest record from the queue
                    ECG.remove();
                    // add the new record to the queue 
                    ECG.add(record);
                }
            // ALERT 5 FOR BUTTON PRESSED
            // check if the record is a button pressed record
            } else if (record.getRecordType().equals("Button Pressed")){
                // check if the button was pressed
                if (record.getMeasurementValue() == 1) {
                    // Output the alert
                    System.out.println("Alert: Button pressed for patient " + patient.getPatientId());
                    Alert alert = new Alert(String.valueOf(patient.getPatientId()), "Button pressed", System.currentTimeMillis());
                    alerts.add(alert);
                }
            } else {
                System.out.println("Unknown record type: " + record.getRecordType());
            }
            // ALERT 3 FOR HYPOTENSIVE HYPOXEMIA
            if (hypotensive && hypoxemic) {
                // Output the alert
                System.out.println("Alert: Hypotensive hypoxemia detected for patient " + patient.getPatientId());
                Alert alert = new Alert(String.valueOf(patient.getPatientId()), "Hypotensive hypoxemia detected", System.currentTimeMillis());
                alerts.add(alert);
            }
           //sendAlerts(alerts); // send the alerts to the output strategy
           
        }
        return alerts; // return the list of alerts generated for the patient
    }
    public static void sendAlerts(List<Alert> alerts) {
        for (Alert alert : alerts) {
            // Output the alert
            // TODO: send them to AlertManager class to notify the staff
        }
    }
}
