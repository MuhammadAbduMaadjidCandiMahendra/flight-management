package com.mitrais.flightmanagement;

import com.mitrais.flightmanagement.entity.Passenger;
import com.mitrais.flightmanagement.repository.*;
import com.mitrais.flightmanagement.screen.AdminPanelScreen;
import com.mitrais.flightmanagement.screen.LoginScreen;
import com.mitrais.flightmanagement.screen.PassengerLoginScreen;
import com.mitrais.flightmanagement.screen.PassengerPanelScreen;
import com.mitrais.flightmanagement.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlightBookingAndManagementSystemApplication implements CommandLineRunner {

	private final AircraftService aircraftService;
	private final CityService cityService;
	private final RouteService routeService;
	private final SystemOperationalService systemOperationalService;
	private final PassengerService passengerService;
	private final BookingService bookingService;

	public FlightBookingAndManagementSystemApplication(AircraftRepository aircraftRepository,
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
    }

	public static void main(String[] args) {
		SpringApplication.run(FlightBookingAndManagementSystemApplication.class, args);
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
		do {
			final var loginScreen = new LoginScreen();
			final LoginScreen.LoginType loginType = loginScreen.start();

			if (LoginScreen.LoginType.ADMIN.equals(loginType)) {
				final AdminPanelScreen adminPanelScreen = new AdminPanelScreen(aircraftService, cityService, routeService, systemOperationalService);
				adminPanelScreen.start();
			} else if (LoginScreen.LoginType.PASSENGER.equals(loginType)) {
				final PassengerLoginScreen passengerLoginScreen = new PassengerLoginScreen(passengerService);
				final Passenger passenger = passengerLoginScreen.start();

				final PassengerPanelScreen passengerPanelScreen = new PassengerPanelScreen(passenger, cityService, routeService, bookingService, systemOperationalService);
				passengerPanelScreen.start();
			} else if (LoginScreen.LoginType.EXIT.equals(loginType)) {
				System.exit(0);
			}
		} while (true);
	}
}
