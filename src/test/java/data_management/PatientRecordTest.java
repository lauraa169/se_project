package data_management;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.data_management.PatientRecord;

class PatientRecordTest {

    // tests if the PatientRecord constructor initializes the object correctly
    // and if the getter methods return the expected values
    @Test
    void testPatientRecordConstructor() {
        int expectedPatientId = 101;
        double expectedMeasurementValue = 98.6;
        String expectedRecordType = "ECG";
        long expectedTimestamp = System.currentTimeMillis();

        PatientRecord record = new PatientRecord(expectedPatientId, expectedMeasurementValue, expectedRecordType, expectedTimestamp);

        assertEquals(expectedPatientId, record.getPatientId(), "Patient ID should match");
        assertEquals(expectedMeasurementValue, record.getMeasurementValue(), 0.001, "Measurement value should match");
        assertEquals(expectedRecordType, record.getRecordType(), "Record type should match");
        assertEquals(expectedTimestamp, record.getTimestamp(), "Timestamp should match");
    }
}
