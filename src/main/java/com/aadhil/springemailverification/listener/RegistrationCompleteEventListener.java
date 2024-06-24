package com.aadhil.springemailverification.listener;

import org.springframework.context.ApplicationListener;

import com.aadhil.springemailverification.event.RegistrationCompleteEvent;

public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

    }
}
