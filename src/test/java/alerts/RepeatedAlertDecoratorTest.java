package alerts;

import com.alerts.Alert;
import com.alerts.decorators.RepeatedAlertDecorator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepeatedAlertDecoratorTest {



    @Test
    void sendAlert() {
        Alert testAlert = new Alert("1", "test", 123456L);
        RepeatedAlertDecorator decorator = new RepeatedAlertDecorator(testAlert, 2);
        assertEquals(2, decorator.getCount(), "Priority should be stored correctly");

        decorator.sendAlert(testAlert);

        assertEquals(2, testAlert.sent, "Alert should not be sent twice");



    }
}