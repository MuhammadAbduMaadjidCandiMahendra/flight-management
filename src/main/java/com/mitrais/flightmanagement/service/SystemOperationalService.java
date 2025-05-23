package com.mitrais.flightmanagement.service;

import com.mitrais.flightmanagement.entity.SystemOperational;
import com.mitrais.flightmanagement.entity.SystemOperational.SystemOperationalState;
import com.mitrais.flightmanagement.model.SystemDay;
import com.mitrais.flightmanagement.repository.SystemOperationalRepository;

import java.time.LocalDateTime;
import java.util.List;

import static com.mitrais.flightmanagement.entity.SystemOperational.SystemOperationalState.*;

public class SystemOperationalService {

    private final SystemOperationalRepository systemOperationalRepository;

    public SystemOperationalService(SystemOperationalRepository systemOperationalRepository) {
        this.systemOperationalRepository = systemOperationalRepository;
    }

    /**
     * This method is used to get the current system operational status.
     * If there is no operational history, it will create a new one with the first day of operation.
     * This the example of created SystemOperational data:
     * +----+------------------+----------+---------------------+
     * | ID | Operational Day  | State    | Timestamps          |
     * +----+------------------+----------+---------------------+
     * | 1  | 1                | PENDING  | 2025-05-23 07:50:00 |
     * | 2  | 1                | RUNNING  | 2025-05-23 08:00:00 |
     * | 3  | 1                | STOP     | 2025-05-23 18:00:00 |
     * | 4  | 2                | PENDING  | 2025-05-24 07:55:00 |
     * | 5  | 2                | RUNNING  | 2025-05-24 08:05:00 |
     * | 6  | 2                | STOP     | 2025-05-24 18:10:00 |
     * | 7  | 3                | PENDING  | 2025-05-25 07:45:00 |
     * | 8  | 3                | RUNNING  | 2025-05-25 08:10:00 |
     * +----+------------------+----------+---------------------+
     *
     * @return SystemOperational
     */
    public SystemOperational runningSystemOperational() {
        final List<SystemOperational> currentDaySystemStates = findCurrentDaySystemStates();
        if (currentDaySystemStates.isEmpty()) {
            return saveFirstSystemState(RUNNING);
        }

        final SystemOperational pendingSystem = validateSystemIsRunning(currentDaySystemStates);
        final SystemOperational runningSystem = buildSystemOperational(pendingSystem.getOperationalDay(), RUNNING);
        return systemOperationalRepository.save(runningSystem);
    }

    public SystemOperational stopSystemOperational(SystemOperational currentState) {
        final SystemOperational stopState = buildSystemOperational(currentState.getOperationalDay(), STOP);
        return systemOperationalRepository.save(stopState);
    }

    public SystemOperational toNextDay() {
        final List<SystemOperational> currentStates = findCurrentDaySystemStates();
        SystemOperational currentState = currentStates.get(0);
        if (!(STOP.equals(currentState.getState()) || PENDING.equals(currentState.getState()))) {
            currentState = stopSystemOperational(currentState);
        }

        final SystemDay dayNow = currentState.getOperationalDay();
        return systemOperationalRepository.save(buildSystemOperational(dayNow.plus(1), PENDING));
    }

    public SystemOperational loadSystemOperational() {
        final List<SystemOperational> currentDaySystemStates = findCurrentDaySystemStates();
        if (currentDaySystemStates.isEmpty()) {
            return saveFirstSystemState(PENDING);
        }

        final SystemOperational currentState = currentDaySystemStates.get(0);
        if (currentState.getState().equals(RUNNING)) {
            return stopSystemOperational(currentState);
        }
        return currentState;
    }

    private SystemOperational saveFirstSystemState(SystemOperationalState systemOperationalState) {
        final SystemOperational soFirstDay = buildSystemOperational(SystemDay.dayOf(1), systemOperationalState);
        systemOperationalRepository.save(soFirstDay);
        return soFirstDay;
    }

    private static SystemOperational validateSystemIsRunning(List<SystemOperational> currentDayStates) {
        final SystemOperational currentState = currentDayStates.get(0);
        validateSystemIsRunning(currentState);
        return currentState; // possible state: PENDING or STOP
    }

    private static SystemOperational buildSystemOperational(SystemDay operationalDay, SystemOperationalState systemOperationalState) {
        return SystemOperational.builder()
                .operationalDay(operationalDay)
                .state(systemOperationalState)
                .timestamps(LocalDateTime.now())
                .build();
    }

    public SystemDay getDayNow() {
        return systemOperationalRepository.findLatestOperationalDay().orElse(SystemDay.dayOf(1));
    }

    public void isSystemRunning() {
        final List<SystemOperational> currentSystemStates = findCurrentDaySystemStates();
        validateSystemIsNotRunning(currentSystemStates.get(0));
    }

    private List<SystemOperational> findCurrentDaySystemStates() {
        return systemOperationalRepository.findAllByLatestOperationalDay();
    }

    private static void validateSystemIsRunning(SystemOperational systemOperational) {
        if (RUNNING.equals(systemOperational.getState())) {
            throw new IllegalStateException("System already running!");
        }
    }

    private static void validateSystemIsNotRunning(SystemOperational systemOperational) {
        if (!RUNNING.equals(systemOperational.getState())) {
            throw new IllegalStateException("System is not running!");
        }
    }
}
