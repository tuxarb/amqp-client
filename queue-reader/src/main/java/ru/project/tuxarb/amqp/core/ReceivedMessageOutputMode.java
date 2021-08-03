package ru.project.tuxarb.amqp.core;

import java.util.Arrays;

import static java.lang.String.format;

/**
 * Енум для определения, что необходимо сделать с полученным сообщением. По умолчанию пишем контент в консоль.
 */
public enum ReceivedMessageOutputMode {
    FILE,
    CONSOLE;

    public static ReceivedMessageOutputMode from(String value) {
        if (value == null) {
            return CONSOLE;
        }
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(format("output_mode %s is unknown!", value)));
    }
}
