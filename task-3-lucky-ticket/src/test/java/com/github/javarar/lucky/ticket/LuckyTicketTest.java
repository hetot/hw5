package com.github.javarar.lucky.ticket;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LuckyTicketTest {
    private final static Double epsilon = 0.00001;

    @DisplayName("Задание 7. Счастливый билет")
    @ParameterizedTest
    @MethodSource("cases")
    public void moscowLuckyTicketProbabilityTest(int serialNumberLength, double probability) {
        var result = LuckyTicket.moscowLuckyTicketProbability(serialNumberLength);
        assertTrue(Math.abs(result - probability) < epsilon);
    }

    private static Stream<Arguments> cases() {
        return Stream.of(
                Arguments.of(6, 0.055252)
        );
    }

}