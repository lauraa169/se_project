package com.alerts;

public class PriorityAlertDecorator extends AlertDecorator {
    private int priority = 0;

    public PriorityAlertDecorator(Alert decoratedAlert, int priority) {
        super(decoratedAlert);
        this.priority = priority;
    }

    @Override
    public void sendAlert(Alert decoratedAlert) {
        super.sendAlert(decoratedAlert);
    }
}
