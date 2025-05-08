package data_management;
import com.data_management.DataReader;
import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import com.data_management.PatientRecord;
import com.data_management.Patient;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FileDataReaderTest {

    private Path tempFile;
    private FileDataReader reader;
    private DataStorage storage;

    // creates a temporary file with sample patient data for testing
    // (executed before the test)
    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("patient_data", ".txt");
        List<String> lines = Arrays.asList(
                "1,98.6,ECG,1714376789050",
                "2,120.5,Systolic Blood Pressure,1714376789051"
        );
        Files.write(tempFile, lines);
        storage = DataStorage.getInstance();
        reader = new FileDataReader(tempFile.toString());
    }

    // tests if the file is read correctly and the data is stored in the DataStorage
    @Test
    void testReadData() throws IOException {
        reader.readData(storage);
    
        int totalRecords = 0;
        for (Patient p : storage.getAllPatients()) {
            int pid = p.getPatientId();
            // checks if the patient ID is correct
            totalRecords += storage.getRecords(pid, 0, Long.MAX_VALUE).size();
        }
    
        assertEquals(4, totalRecords);
    
        // verifies individual records
        List<PatientRecord> patient1 = storage.getRecords(1, 0, Long.MAX_VALUE);
        List<PatientRecord> patient2 = storage.getRecords(2, 0, Long.MAX_VALUE);
    
        assertEquals(3, patient1.size());
        assertEquals(1, patient2.size());
    
        PatientRecord r1 = patient1.get(0);
        assertEquals(100.0, r1.getMeasurementValue(), 0.001);
    
        PatientRecord r2 = patient2.get(0);
        assertEquals("Systolic Blood Pressure", r2.getRecordType());
    }

    // tests if the line is parsed correctly into a PatientRecord object
    // and if the values are assigned correctly
    @Test
    void testParseLineToRecord() throws IOException {
        FileDataReader reader = new FileDataReader(tempFile.toString());
        String line = "2,120.0,Systolic Blood Pressure,1714376789050";

        PatientRecord record = reader.parseLineToRecord(line);
        assertEquals(2, record.getPatientId());
        assertEquals(120.0, record.getMeasurementValue());
        assertEquals("Systolic Blood Pressure", record.getRecordType());
        assertEquals(1714376789050L, record.getTimestamp());
    }
    
    // deletes the temporary file after the test is executed
    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    
}
