package com.mitrais.flightmanagement.util;

public record Triple<S,U,V> (S first, U second, V third) {

    public static <S,U,V> Triple<S,U,V> of(S first, U second, V third) {
        return new Triple<>(first, second, third);
    }
}
