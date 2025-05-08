package alerts;



import com.alerts.Alert;
import com.alerts.BloodOxygenAlert;
import com.alerts.factories.BloodOxygenAlertFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BloodOxygenAlertFactoryTest {

    @Test
    void createAlert() {
        BloodOxygenAlertFactory factory = new BloodOxygenAlertFactory();
        String patientId = "patient42";
        String condition = "Low Oxygen";
        long timestamp = System.currentTimeMillis();

        Alert alert = factory.createAlert(patientId, condition, timestamp);

        assertNotNull(alert);
        assertTrue(alert instanceof BloodOxygenAlert, "Alert should be instance of BloodOxygenAlert");
        assertEquals(patientId, alert.getPatientId());
        assertEquals(condition, alert.getCondition());
        assertEquals(timestamp, alert.getTimestamp());
    }
}
