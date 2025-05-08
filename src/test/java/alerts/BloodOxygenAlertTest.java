package alerts;

import com.alerts.Alert;
import com.alerts.Alertable;
import com.alerts.BloodOxygenAlert;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BloodOxygenAlertTest {

    @Test
    public void bloodOxygenAlertTest() {
        String patientId = "007";
        String condition = "Blood Oxygen Low";
        long timestamp = System.currentTimeMillis();

        BloodOxygenAlert alert = new BloodOxygenAlert(patientId, condition, timestamp);

        assertEquals(patientId, alert.getPatientId());
        assertEquals(condition, alert.getCondition());
        assertEquals(timestamp, alert.getTimestamp());
    }

    @Test
    public void testInstanceOf() {
        BloodOxygenAlert alert = new BloodOxygenAlert("001", "O2 Drop", 123456789L);

        assertTrue(alert instanceof Alert);
        assertTrue(alert instanceof Alertable);
    }
}
