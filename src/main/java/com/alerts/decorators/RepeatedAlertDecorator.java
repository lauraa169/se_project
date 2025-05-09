package com.alerts.decorators;

import com.alerts.Alert;

public class RepeatedAlertDecorator extends AlertDecorator {
    private int count = 0;

    public RepeatedAlertDecorator(Alert decoratedAlert, int count) {
        super(decoratedAlert);
        this.count = count;

    }

    public int getCount(){
        return this.count;
    }
    @Override
    public void sendAlert(Alert decoratedAlert) {
        for (int i = 0; i < count; i++) {
            decoratedAlert.sent++;
        }
    }


}
