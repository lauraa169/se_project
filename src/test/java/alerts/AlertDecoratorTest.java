package alerts;

import com.alerts.Alert;
import com.alerts.decorators.AlertDecorator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlertDecoratorTest {



    @Test
    void testSendAlertCallsSend() {
        Alert testAlert = new Alert("3", "Test", 1234567890L);
        AlertDecorator decorator = new AlertDecorator(testAlert);

        decorator.sendAlert(testAlert);

        assertEquals(1, testAlert.wasSent(), "Alert should have been sent.");
    }
}