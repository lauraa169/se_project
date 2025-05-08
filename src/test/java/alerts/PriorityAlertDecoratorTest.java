package alerts;

import com.alerts.Alert;
import com.alerts.Alertable;
import com.alerts.decorators.PriorityAlertDecorator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriorityAlertDecoratorTest {



    @Test
    void sendAlert() {
        Alert testAlert = new Alert("3", "test", 123456L);
        PriorityAlertDecorator decorator = new PriorityAlertDecorator(testAlert, 2);
        assertEquals(2, decorator.getPriority(), "Priority should be stored correctly");

        decorator.sendAlert(testAlert); // shouldn't be sent yet

        assertEquals(0, testAlert.sent, "Alert should not be sent");

        decorator.sendAlert(testAlert); // should be sent now

        assertEquals(1, testAlert.sent, "Alert should be marked as sent");

    }
}
