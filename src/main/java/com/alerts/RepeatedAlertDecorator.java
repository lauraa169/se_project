package com.alerts;

public class RepeatedAlertDecorator extends AlertDecorator {
    private int count = 0;

    public RepeatedAlertDecorator(Alert decoratedAlert, int count) {
        super(decoratedAlert);
        this.count = count;

    }

    public void sendAlert(Alert decoratedAlert) {
        for (int i = 0; i < count; i++) {
            super.sendAlert(decoratedAlert);

        }
    }

}
