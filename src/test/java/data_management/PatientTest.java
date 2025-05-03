package data_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.data_management.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    private Patient patient;

    // creates a new patient with id 1 for testing
    // (executed before each test)
    @BeforeEach
    void setUp() {
        patient = new Patient(1);
    }

    // tests if the patient ID is set correctly
    @Test
    void testGetPatientId_returnsCorrectId() {
        assertEquals(1, patient.getPatientId());
    }

    // tests if records can be added and retrieved correctly
    @Test
    void testAddRecord_AndRetrieveRecord() {
        long timestamp = System.currentTimeMillis();
        patient.addRecord(120.0, "Systolic Blood Pressure", timestamp);

        List<PatientRecord> records = patient.getRecords(timestamp - 1000, timestamp + 1000);
        assertEquals(1, records.size());

        PatientRecord record = records.get(0);
        assertEquals(120.0, record.getMeasurementValue());
        assertEquals("Systolic Blood Pressure", record.getRecordType());
        assertEquals(timestamp, record.getTimestamp());
        assertEquals(1, record.getPatientId());
    }

    // tests if records outside the specified time range are not retrieved
    @Test
    void testGetRecords_OutsideTimeRange() {
        long now = System.currentTimeMillis();
        patient.addRecord(98.0, "Blood Saturation", now - 10_000); // too old
        patient.addRecord(101.0, "Blood Saturation", now + 10_000); // too new

        List<PatientRecord> records = patient.getRecords(now - 5000, now + 5000);
        assertTrue(records.isEmpty(), "No records should be within the time range");
    }

    // tests if records within the specified time range are retrieved correctly
    @Test
    void testGetRecords_InsideTimeRange() {
        long now = System.currentTimeMillis();
        patient.addRecord(98.6, "Temperature", now - 2000); // in range
        patient.addRecord(120.0, "Systolic Blood Pressure", now); // in range
        patient.addRecord(99.0, "Temperature", now + 6000); // out of range

        List<PatientRecord> records = patient.getRecords(now - 5000, now + 1000);
        assertEquals(2, records.size());

        assertTrue(records.stream().anyMatch(r -> r.getRecordType().equals("Temperature")));
        assertTrue(records.stream().anyMatch(r -> r.getRecordType().equals("Systolic Blood Pressure")));
    }


}
