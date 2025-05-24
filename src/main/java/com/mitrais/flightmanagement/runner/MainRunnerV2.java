package com.mitrais.flightmanagement.runner;

import com.mitrais.flightmanagement.repository.*;
import com.mitrais.flightmanagement.screenv2.Screen;
import com.mitrais.flightmanagement.screenv2.ScreenFactory;
import com.mitrais.flightmanagement.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;

@Slf4j
@Component
public class MainRunnerV2 implements CommandLineRunner {

    private final AircraftService aircraftService;
    private final CityService cityService;
    private final RouteService routeService;
    private final SystemOperationalService systemOperationalService;
    private final PassengerService passengerService;
    private final BookingService bookingService;
    private final ScreenFactory screenFactory;

    public MainRunnerV2(AircraftRepository aircraftRepository,
                        CityRepository cityRepository,
                        RouteRepository routeRepository,
                        SystemOperationalRepository systemOperationalRepository,
                        PassengerRepository passengerRepository,
                        BookingRepository bookingRepository) {
        this.aircraftService = new AircraftService(aircraftRepository);
        this.cityService = new CityService(cityRepository);
        this.routeService = new RouteService(routeRepository);
        this.systemOperationalService = new SystemOperationalService(systemOperationalRepository);
        this.passengerService = new PassengerService(passengerRepository);
        this.bookingService = new BookingService(bookingRepository, routeRepository);

        this.screenFactory = new ScreenFactory(passengerService, cityService, aircraftService, routeService, bookingService, systemOperationalService);
    }

    @Override
    public void run(String... args) throws Exception {
        preparingSystemOperational();
        startFlightBookingCli();
    }

    private void preparingSystemOperational() {
        systemOperationalService.loadSystemOperational();
    }

    private void startFlightBookingCli() {
        Deque<Screen> screenStack = new ArrayDeque<>();
        screenStack.push(this.screenFactory.getMainMenuScreen());
        while (true) {
            logScreenStacks(screenStack);
            final Screen screen = screenStack.peek();
            screen.start();

            if(screen.nextScreen().isPresent()) {
                screenStack.push(screen.nextScreen().get());
                screen.resetNextScreen();
            } else {
                if (screenStack.size() > 1) { // to prevent Main Menu popped
                    screenStack.pop();
                }
            }
        }
    }

    private static void logScreenStacks(Deque<Screen> screenStack) {
        screenStack.forEach(s -> log.debug(s.getClass().getName()));
        log.debug("----");
    }
}
