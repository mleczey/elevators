package com.tingco.codechallenge.elevator.entity.algorithm;

import com.tingco.codechallenge.elevator.entity.Elevator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ClosestDistanceAlgorithmTest {

    private static final int MIN_FLOOR = 0;
    private static final int MAX_FLOOR = 2;


    @ParameterizedTest
    @MethodSource("floorsFromMinFloorProvider")
    void shouldCalculateDistanceFromMinFloor(final int requestedFloor, final int expected) {
        // given
        final var testedObject = new ClosestDistanceAlgorithm(MIN_FLOOR, MIN_FLOOR, MAX_FLOOR, Elevator.Direction.UP);

        // when
        final var actual = testedObject.calculateDistance(requestedFloor);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> floorsFromMinFloorProvider() {
        return Stream.of(
                arguments(MIN_FLOOR, MIN_FLOOR),
                arguments(MAX_FLOOR - 1, MAX_FLOOR - 1),
                arguments(MAX_FLOOR, MAX_FLOOR)
        );
    }

    @ParameterizedTest
    @MethodSource("floorsFromVariousFloorProvider")
    void shouldCalculateDistanceFromGivenFloor(final int requestedFloor, final Elevator.Direction direction, final int expected) {
        // given
        final var minFloor = 0;
        final var currentFloor = 5;
        final var maxFloor = 10;
        final var testedObject = new ClosestDistanceAlgorithm(minFloor, currentFloor, maxFloor, direction);

        // when
        final var actual = testedObject.calculateDistance(requestedFloor);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> floorsFromVariousFloorProvider() {
        return Stream.of(
                arguments(8, Elevator.Direction.UP, 3),
                arguments(2, Elevator.Direction.UP, 13),
                arguments(8, Elevator.Direction.DOWN, 13),
                arguments(2, Elevator.Direction.DOWN, 3)
        );
    }
}
