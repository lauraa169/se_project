package data_management;

import com.data_management.DataStorage;
import com.data_management.MyWebSocketClient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyWebSocketClientTest {

    private DataStorage storage;
    private MyWebSocketClient client;

    @BeforeEach
    void setUp() throws Exception {
        storage = DataStorage.getInstance();  // Use real DataStorage
        client = new MyWebSocketClient(new URI("ws://localhost:8080"), storage);
    }

    @Test
    void testOnMessage() {
        String message = "1,1715000000000,Blood Pressure,120.5";

        // receiving a message
        client.onMessage(message);

        // checks if the data is stored in the data storage
        List<PatientRecord> records = storage.getRecords(1, 0, Long.MAX_VALUE);
        assertEquals(4, records.size());

        PatientRecord record = records.get(3);
        assertEquals(1, record.getPatientId());
        assertEquals("Blood Pressure", record.getRecordType());
        assertEquals(120.5, record.getMeasurementValue(), 0.001);
        assertEquals(1715000000000L, record.getTimestamp());
    }

    @Test
    void testParseValidMessage() {
        // checks if messages are parsed correctly
        MyWebSocketClient client = new MyWebSocketClient(URI.create("ws://localhost:8080"), storage);
        String input = "1,1700000000000,HeartRate,85.5";
        PatientRecord record = client.parseMessage(input);
        assertEquals(1, record.getPatientId());
        assertEquals(1700000000000L, record.getTimestamp());
        assertEquals("HeartRate", record.getRecordType());
        assertEquals(85.5, record.getMeasurementValue(), 0.01);
    }

    @Test
    void testParseMessageInvalidSize(){
        MyWebSocketClient client = new MyWebSocketClient(URI.create("ws://localhost:8080"), storage);
        String input = "1,1700000000000,HeartRate"; // one argument missing (invalid message)
        // checks if error message is triggered
        assertThrows(IllegalArgumentException.class, () -> {
            client.parseMessage(input);
        });
    }

    @Test
    void testParseMessageInvalidDataType() {
        MyWebSocketClient client = new MyWebSocketClient(URI.create("ws://localhost:8080"), storage);
        String invalidMessage = "abc,1700000000000,HeartRate,75.5"; // patientID should be a number,not abc

        assertThrows(NumberFormatException.class, () -> {
            client.parseMessage(invalidMessage);
        });
    }



}
