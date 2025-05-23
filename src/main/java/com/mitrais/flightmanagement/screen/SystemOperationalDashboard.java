package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.entity.SystemOperational;

public class SystemOperationalDashboard extends Screen<Void>{
    private final SystemOperational currentState;

    public SystemOperationalDashboard(SystemOperational currentState) {
        this.currentState = currentState;
    }

    @Override
    protected Void renderScreen() {
        print("Day: %s State: %s".formatted(currentState.getOperationalDay().getValue(), currentState.getState().name()));
        return null ;
    }
}
