package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.testutil.MockScanner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LoginScreenTest {
    @Test
    void successLoginAsAdmin() {
        MockScanner.input("1");

        final var loginScreen = new LoginScreen();
        final var actualResult = loginScreen.start();

        Assertions.assertThat(actualResult).isEqualTo(LoginScreen.LoginType.ADMIN);
    }

    @Test
    void successLoginAsPassenger() {
        MockScanner.input("2");

        final var loginScreen = new LoginScreen();
        final var actualResult = loginScreen.start();

        Assertions.assertThat(actualResult).isEqualTo(LoginScreen.LoginType.PASSENGER);
    }
}
