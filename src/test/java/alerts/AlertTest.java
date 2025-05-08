package alerts;


import com.alerts.Alert;
import com.alerts.Alertable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AlertTest {

    @Test
    public void testAlert() {
        String patientId = "123";
        String condition = "Low Oxygen";
        long timestamp = System.currentTimeMillis();

        Alertable alert = new Alert(patientId, condition, timestamp);

        assertEquals(patientId, alert.getPatientId());
        assertEquals(condition, alert.getCondition());
        assertEquals(timestamp, alert.getTimestamp());
    }
}
