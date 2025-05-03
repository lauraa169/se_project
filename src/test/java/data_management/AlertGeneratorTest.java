package data_management;
import com.cardio_generator.generators.AlertGenerator;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.alerts.Alert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlertGeneratorTest {
    // BLOOD PRESSURE ALERT: TREND ALERT
    // checks if the evaluateData method generates blood pressure trend alerts correctly
    @Test
    void testEvaluateData_TrendAlert() {
        AlertGenerator generator = new AlertGenerator(1);
        Patient mockPatient1 = new Patient(1);
        Patient mockPatient2 = new Patient(2);

        long now = System.currentTimeMillis();

        // decreasing trend alert
        //alert is triggered with 3 consecutive records of a 10 decrease in blood pressure (any type)
        mockPatient1.addRecord(85.0, "Systolic Blood Pressure", now); 
        mockPatient1.addRecord( 73, "Diastolic Blood Pressure", now); 
        mockPatient1.addRecord( 60, "Systolic Blood Pressure", now); 

        // increasing trend alert
        //alert is triggered with 3 consecutive records of a 10 increase in blood pressure (any type)
        mockPatient2.addRecord(85, "Systolic Blood Pressure", now); 
        mockPatient2.addRecord( 100, "Diastolic Blood Pressure", now); 
        mockPatient2.addRecord( 120, "Systolic Blood Pressure", now); 

        List<Alert> alerts1 = generator.evaluateData(mockPatient1, now - 10000, now + 10000);
        List<Alert> alerts2 = generator.evaluateData(mockPatient2, now - 10000, now + 10000);

        // validate that an alert was triggered
        assertNotNull(alerts1);
        assertFalse(alerts1.isEmpty());
        assertNotNull(alerts2);
        assertFalse(alerts2.isEmpty());

        // validate if its the correct alert
        // checks if the alerts BOTH contain the expected condition
        boolean found = (alerts1.stream().anyMatch(alert -> alert.getCondition().contains("change in blood pressure")) 
        && alerts2.stream().anyMatch(alert -> alert.getCondition().contains("change in blood pressure")));
        assertTrue(found, "Expected trend alerts not found");
    }
    // BLOOD PRESSURE ALERT: CRITICAL TRESHOLD ALERT
    // checks if the evaluateData method generates blood pressure critical treshold alerts correctly
    @Test
    void testEvaluateData_CritTresAlert() {
        AlertGenerator generator = new AlertGenerator(1);
        Patient mockPatient1 = new Patient(1);
        Patient mockPatient2 = new Patient(2);

        long now = System.currentTimeMillis();

        // systolic critical treshold alert
        //alert is triggered if systolic blood pressure is < 90 or > 180
        mockPatient1.addRecord(85.0, "Systolic Blood Pressure", now); // triggers
        mockPatient1.addRecord( 150, "Systolic Blood Pressure", now); // not triggers
        mockPatient1.addRecord( 200, "Systolic Blood Pressure", now); // triggers

        // dyastolic critical treshold alert
        //alert is triggered if diastolic blood pressure is < 60 or > 120
        mockPatient2.addRecord(50, "Diastolic Blood Pressure", now); // triggers
        mockPatient2.addRecord( 100, "Diastolic Blood Pressure", now); // not triggers
        mockPatient2.addRecord( 121, "Diastolic Blood Pressure", now); // triggers
        List<Alert> alerts1 = generator.evaluateData(mockPatient1, now - 10000, now + 10000);
        List<Alert> alerts2 = generator.evaluateData(mockPatient2, now - 10000, now + 10000);

        // validate that an alert was triggered
        assertNotNull(alerts1);
        assertFalse(alerts1.isEmpty());
        assertNotNull(alerts2);
        assertFalse(alerts2.isEmpty());

        // validate if its the correct alert
        // checks if the alerts BOTH contain the expected condition
        boolean found = (alerts1.stream().anyMatch(alert -> alert.getCondition().contains("Critical blood pressure")) 
        && alerts2.stream().anyMatch(alert -> alert.getCondition().contains("Critical blood pressure")));
        assertTrue(found, "Expected trend alerts not found");
    }
    // BLOOD SATURATION: LOW SATURATION ALERT
    // checks if the evaluateData method generates blood saturation low saturation alerts correctly
    @Test
    void testEvaluateData_LowSatAlert() {
        AlertGenerator generator = new AlertGenerator(1);
        Patient mockPatient1 = new Patient(1);
        

        long now = System.currentTimeMillis();

        //alert is triggered if sblood saturation level is < 92 
        mockPatient1.addRecord(85.0, "Blood Saturation", now); // triggers
        mockPatient1.addRecord( 150, "Blood Saturation", now); // not triggers
        mockPatient1.addRecord( 200, "Blood Saturation", now); // not triggers

       
        List<Alert> alerts = generator.evaluateData(mockPatient1, now - 1000, now + 1000);
        
        // validate that an alert was triggered
        assertNotNull(alerts);
        assertFalse(alerts.isEmpty());

        // validate if its the correct alert
        // checks if the alert contains the expected condition
        boolean found = alerts.stream().anyMatch(alert -> alert.getCondition().contains("Low blood saturation"));
        
        assertTrue(found, "Expected trend alerts not found");
    }
    // BLOOD SATURATION: RAPID DROP ALERT
    // checks if the evaluateData method generates blood saturation rapid drop alerts correctly
    @Test
    void testEvaluateData_DropAlert() {
        AlertGenerator generator = new AlertGenerator(1);
        Patient mockPatient1 = new Patient(1);
        

        long now = System.currentTimeMillis();
        System.out.println("now: " + now);

        //alert is triggered if blood saturation level drops more than 5% in 10 minutes
        mockPatient1.addRecord(85.0, "Blood Saturation", now); 
        mockPatient1.addRecord( 82, "Blood Saturation", now + 500000); 
        mockPatient1.addRecord( 75, "Blood Saturation", now + 300000); 

       
        List<Alert> alerts = generator.evaluateData(mockPatient1, now - 1000, now + 1000);
        
        // validate that an alert was triggered
        assertNotNull(alerts);
        assertFalse(alerts.isEmpty());

        // validate if its the correct alert
        // checks if the alerts BOTH contain the expected condition
        boolean found = alerts.stream().anyMatch(alert -> alert.getCondition().contains("Low blood saturation"));
        
        assertTrue(found, "Expected trend alerts not found");
    }
    // COMBINED ALERT: HYPOTENSIVE HYPOXEMIA
    // checks if the evaluateData method generates hypotensive hypoxemia alerts correctly
    @Test
    void testEvaluateData_HypoHypoAlert() {
        AlertGenerator generator = new AlertGenerator(1);
        Patient mockPatient = new Patient(1);

        long now = System.currentTimeMillis();

        // systolic BP < 90 triggers hypotension alert
        // blood saturation < 92% triggers hypoxemia alert
        mockPatient.addRecord(85.0, "Systolic Blood Pressure", now); // Hypotension
        mockPatient.addRecord( 93.0, "Blood Saturation", now); // Not hypoxemic
        mockPatient.addRecord( 89.0, "Blood Saturation", now + 1000); // Hypoxemia

        List<Alert> alerts = generator.evaluateData(mockPatient, now - 10000, now + 10000);

        // validate that an alert was triggered
        assertNotNull(alerts);
        assertFalse(alerts.isEmpty());

        // validate if its the correct alert
        // checks if the alert contains the expected condition
        boolean found = alerts.stream().anyMatch(alert -> alert.getCondition().contains("Hypotensive hypoxemia"));
        assertTrue(found, "Expected hypotensive hypoxemia alerts not found");
    }
    // ECG: ABNORMAL DATA ALERTS
    // checks if the evaluateData method generates the abnormal data alerts correctly
    @Test
    void testEvaluateData_ECGAlert() {
        AlertGenerator generator = new AlertGenerator(1);
        Patient mockPatient = new Patient(1);

        long now = System.currentTimeMillis();

        // systolic BP < 90 triggers hypotension alert
        // blood saturation < 92% triggers hypoxemia alert
        mockPatient.addRecord(85.0, "ECG", now); 
        mockPatient.addRecord( 93.0, "ECG", now);
        mockPatient.addRecord( 89.0, "ECG", now);
        mockPatient.addRecord(85.0, "ECG", now);
        mockPatient.addRecord( 93.0, "ECG", now);
        mockPatient.addRecord( 120.0, "ECG", now + 1000); // triggers

        List<Alert> alerts = generator.evaluateData(mockPatient, now - 10000, now + 10000);

        // validate that an alert was triggered
        assertNotNull(alerts);
        assertFalse(alerts.isEmpty());

        // validate if its the correct alert
        // checks if the alert contains the expected condition
        boolean found = alerts.stream().anyMatch(alert -> alert.getCondition().contains("Abnormal ECG"));
        assertTrue(found, "Expected ECG alerts not found");
    }
    // TRIGGER ALERT: BUTTON PRESSED ALERTS
    // checks if the evaluateData method generates button pressed alerts correctly
    @Test
    void testEvaluateData_ButtonAlert() {
        AlertGenerator generator = new AlertGenerator(1);
        Patient mockPatient = new Patient(1);

        long now = System.currentTimeMillis();

        // systolic BP < 90 triggers hypotension alert
        // blood saturation < 92% triggers hypoxemia alert
        mockPatient.addRecord(0, "Button Pressed", now); 
        mockPatient.addRecord( 1, "Button Pressed", now); // triggrs
        mockPatient.addRecord( 0, "Button Pressed", now + 1000); 

        List<Alert> alerts = generator.evaluateData(mockPatient, now - 10000, now + 10000);

        // validate that an alert was triggered
        assertNotNull(alerts);
        assertFalse(alerts.isEmpty());

        // validate if its the correct alert
        // checks if the alert contains the expected condition
        boolean found = alerts.stream().anyMatch(alert -> alert.getCondition().contains("Button pressed"));
        assertTrue(found, "Expected trigger alerts not found");
    }
}
