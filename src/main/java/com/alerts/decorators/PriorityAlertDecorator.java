package com.alerts.decorators;

import com.alerts.Alert;

public class PriorityAlertDecorator extends AlertDecorator {
    private int priority = 0;

    public PriorityAlertDecorator(Alert decoratedAlert, int priority) {
        super(decoratedAlert);
        this.priority = priority;
    }

    @Override
    public void sendAlert(Alert decoratedAlert) {
        if (this.priority > 1){
            priority--;
        } else if (this.priority==1) {
            decoratedAlert.sent++;
            // decoratedAlert.send();
        } else{
            System.out.println("Invalid priority");
        }

    }

    public int getPriority() {
        return priority;
    }
}
