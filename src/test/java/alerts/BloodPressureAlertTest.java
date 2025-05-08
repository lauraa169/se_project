package alerts;

import com.alerts.Alert;
import com.alerts.Alertable;
import com.alerts.BloodPressureAlert;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BloodPressureAlertTest {

    @Test
    public void bloodPressureAlertTest() {
        String patientId = "123";
        String condition = "High Blood Pressure";
        long timestamp = System.currentTimeMillis();

        BloodPressureAlert alert = new BloodPressureAlert(patientId, condition, timestamp);

        assertEquals(patientId, alert.getPatientId());
        assertEquals(condition, alert.getCondition());
        assertEquals(timestamp, alert.getTimestamp());
    }

    @Test
    public void testInstanceOf() {
        BloodPressureAlert alert = new BloodPressureAlert("456", "Hypertension Alert", 987654321L);

        assertTrue(alert instanceof Alert);
        assertTrue(alert instanceof Alertable);
    }
}
