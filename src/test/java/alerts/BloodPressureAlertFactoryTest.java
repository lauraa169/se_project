package alerts;


import com.alerts.Alert;
import com.alerts.BloodPressureAlert;
import com.alerts.factories.BloodPressureAlertFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BloodPressureAlertFactoryTest {

    @Test
    void createAlert() {
        BloodPressureAlertFactory factory = new BloodPressureAlertFactory();
        String patientId = "patient123";
        String condition = "High Blood Pressure";
        long timestamp = System.currentTimeMillis();

        Alert alert = factory.createAlert(patientId, condition, timestamp);

        assertNotNull(alert);
        assertTrue(alert instanceof BloodPressureAlert);
        assertEquals(patientId, alert.getPatientId());
        assertEquals(condition, alert.getCondition());
        assertEquals(timestamp, alert.getTimestamp());
    }
}
