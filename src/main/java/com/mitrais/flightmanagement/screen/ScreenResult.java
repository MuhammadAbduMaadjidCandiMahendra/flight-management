package com.mitrais.flightmanagement.screen;

import lombok.Getter;

// todo is this used?
@Getter
public class ScreenResult<T> {
    private final Screen nextScreen;
    private final T resultData;

    private ScreenResult(Screen nextScreen, T resultData) {
        this.nextScreen = nextScreen;
        this.resultData = resultData;
    }

    public static <T> ScreenResult<T> next(Screen nextScreen, T resultData) {
        return new ScreenResult<>(nextScreen, resultData);
    }

    public static <T> ScreenResult<T> next(Screen nextScreen) {
        return new ScreenResult<>(nextScreen, null);
    }
}
