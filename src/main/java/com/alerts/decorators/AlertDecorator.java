package com.alerts.decorators;

import com.alerts.Alert;
import com.alerts.Alertable;

public class AlertDecorator implements Alertable {
    public Alert decoratedAlert;

    public AlertDecorator(Alert decoratedAlert) {
        this.decoratedAlert = decoratedAlert;
    }


    @Override
    public String getPatientId() {
        return decoratedAlert.getPatientId();
    }

    @Override
    public String getCondition() {
        return decoratedAlert.getCondition();
    }

    @Override
    public long getTimestamp() {
        return decoratedAlert.getTimestamp();
    }
    public void sendAlert(Alert decoratedAlert) {
        decoratedAlert.sent++;
    }

}
