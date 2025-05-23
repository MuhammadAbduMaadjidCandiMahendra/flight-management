package com.mitrais.flightmanagement.screen;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class LoginScreen extends Screen<LoginScreen.LoginType> {
    private static final String OPTION_ADMIN = "1";
    private static final String OPTION_PASSENGER = "2";
    private static final String OPTION_EXIT = "3";

    @Override
    protected LoginType renderScreen() {
        do {
            clearScreen();
            print("=== SIMPLE FLIGHT BOOKING & RUNNING SYSTEM ===");
            print("");
            print("Login as:");
            print("");
            print("1. Admin");
            print("2. Passenger");
            print("3. Exit");
            final String input = doInput();

            if (!(OPTION_ADMIN.equals(input) || OPTION_PASSENGER.equals(input) || OPTION_EXIT.equals(input))) {
                printError("Invalid input!");
                holdScreen();
                continue;
            }
            return LoginType.fromValue(input);
        } while (true);
    }

    @Getter
    @AllArgsConstructor
    public enum LoginType {
        ADMIN(OPTION_ADMIN),
        PASSENGER(OPTION_PASSENGER),
        EXIT(OPTION_EXIT);

        private final String value;

        public static LoginType fromValue(String value) {
            for (LoginType type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid login type: " + value);
        }
    }
}
