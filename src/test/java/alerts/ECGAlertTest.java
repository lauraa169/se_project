package alerts;

import com.alerts.Alert;
import com.alerts.Alertable;
import com.alerts.ECGAlert;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ECGAlertTest {

    @Test
    public void ECGAlertTest() {
        String patientId = "789";
        String condition = "Irregular ECG";
        long timestamp = System.currentTimeMillis();

        ECGAlert alert = new ECGAlert(patientId, condition, timestamp);

        assertEquals(patientId, alert.getPatientId());
        assertEquals(condition, alert.getCondition());
        assertEquals(timestamp, alert.getTimestamp());
    }

    @Test
    public void testInstanceOf() {
        ECGAlert alert = new ECGAlert("789", "ECG Spike", 123456789L);

        assertTrue(alert instanceof Alert);
        assertTrue(alert instanceof Alertable);
    }
}
