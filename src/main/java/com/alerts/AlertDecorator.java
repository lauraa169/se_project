package com.alerts;

public class AlertDecorator implements Alertable{
    private Alert decoratedAlert;

    public AlertDecorator(Alert decoratedAlert) {
        this.decoratedAlert = decoratedAlert;
    }

    public void sendAlert(Alert decoratedAlert){
        //TODO implementation here
    }

}
