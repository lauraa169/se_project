package alerts;

import com.alerts.Alert;
import com.alerts.factories.AlertFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertFactoryTest {

    @Test
    void createAlert() {
        AlertFactory factory = new AlertFactory();
        String patientId = "patient123";
        String condition = "High BP";
        long timestamp = System.currentTimeMillis();

        Alert alert = factory.createAlert(patientId, condition, timestamp);

        assertNotNull(alert, "Alert should not be null");
        assertEquals(patientId, alert.getPatientId());
        assertEquals(condition, alert.getCondition());
        assertEquals(timestamp, alert.getTimestamp());
    }
}
