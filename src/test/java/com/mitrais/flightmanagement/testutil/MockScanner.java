package com.mitrais.flightmanagement.testutil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MockScanner {
    /**
     * To simulate hit anything for hold screen
     */
    public static final String HIT_ANY = "any";

    private MockScanner() {}

    public static void input(String... input) {
        var command = String.join("\n", input);
        System.setIn(new ByteArrayInputStream(command.getBytes()));
    }

    public static String retrieveErrorMessage(Runnable runnable) {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.err;
        System.setErr(new PrintStream(outContent));

        try {
            runnable.run();
        } finally {
            System.setErr(originalOut); // restore
        }

        return outContent.toString();
    }
}
