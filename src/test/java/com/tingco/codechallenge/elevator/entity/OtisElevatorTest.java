package com.tingco.codechallenge.elevator.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class OtisElevatorTest {

    private static final ElevatorId ID = ElevatorId.valueOf(1);
    private static final int MIN_FLOOR = 0;
    private static final int MAX_FLOOR = 2;
    private static final Duration TIME_BETWEEN_FLOORS = Duration.of(100L, ChronoUnit.MILLIS);

    @Test
    void shouldInitializeElevator() {
        // given
        final var id = ElevatorId.valueOf(1);
        final var numberOfFloors = 2;
        final var timeBetweenFloors = Duration.of(1L, ChronoUnit.SECONDS);

        // when
        final var actual = new OtisElevator(id, numberOfFloors, timeBetweenFloors);

        // then
        assertThat(actual.getId()).isEqualTo(id.value());
    }

    @ParameterizedTest
    @ValueSource(ints = {MIN_FLOOR, MAX_FLOOR - 1, MAX_FLOOR})
    void shouldMoveElevatorToRequestedFloor(final int requestedFloor) {
        // given
        final var elevator = elevator();

        // when
        elevator.moveElevator(requestedFloor);
        elevator.tick();

        // then
        assertThat(elevator.getAddressedFloor()).isEqualTo(requestedFloor);
    }

    @Test
    void shouldDoNothingWhenNoRequestedElevators() {
        // given
        final var elevator = elevator();

        // when
        elevator.tick();

        // then
        assertThat(elevator.getDirection()).isEqualTo(Elevator.Direction.NONE);
        assertThat(elevator.isBusy()).isFalse();
    }

    @Test
    void shouldMoveWhenRequestedFloor() {
        // given
        final var elevator = elevator();
        elevator.moveElevator(MAX_FLOOR);

        // when
        elevator.tick();

        // then
        assertThat(elevator.getDirection()).isEqualTo(Elevator.Direction.UP);
        assertThat(elevator.isBusy()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("floorsFromMinFloorProvider")
    void shouldCalculateDistanceFromMinFloor(final int requestedFloor, final int expected) {
        // given
        final var elevator = elevator();

        // when
        final var actual = elevator.calculateDistance(requestedFloor);

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
    void shouldCalculateDistanceFromGivenFloor(final int numberOfFloors, final int numberOfTraveledFloors, final int requestedFloor, final int expected) {
        // given
        final var elevator = elevator(numberOfFloors, numberOfTraveledFloors);

        // when
        final var actual = elevator.calculateDistance(requestedFloor);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> floorsFromVariousFloorProvider() {
        final var numberOfFloors = 10;

        return Stream.of(
                arguments(numberOfFloors, 5, 8, 3),
                arguments(numberOfFloors, 5, 2, 11),
                arguments(numberOfFloors, 13, 8, 13),
                arguments(numberOfFloors, 13, 2, 3)
        );
    }

    @Nested
    static class WhileMoving {
        private ThreadPoolTaskExecutor taskExecutor;

        @BeforeEach
        void setUp() {
            taskExecutor = new ThreadPoolTaskExecutor();
            taskExecutor.setCorePoolSize(1);
            taskExecutor.setMaxPoolSize(1);
            taskExecutor.setWaitForTasksToCompleteOnShutdown(false);
            taskExecutor.initialize();
        }

        @AfterEach
        void tearDown() {
            taskExecutor.shutdown();
        }

        @Test
        void shouldGoToTheLastFloor() {
            // given
            final var elevator = elevator();
            final var lastFloor = MAX_FLOOR;

            // when
            elevator.moveElevator(lastFloor);
            taskExecutor.execute(elevator);

            // then
            await().atMost(2, TimeUnit.SECONDS)
                    .until(() -> elevator.getDirection() == Elevator.Direction.DOWN);
        }

        @Test
        void shouldGoToTheLastFloor2() {
            // given
            final var elevator = elevator();
            final var middleFloor = MAX_FLOOR - 1;
            final var lastFloor = MAX_FLOOR;

            // when
            elevator.moveElevator(middleFloor);
            taskExecutor.execute(elevator);
            elevator.moveElevator(lastFloor);

            // then
            await().atMost(2, TimeUnit.SECONDS)
                    .until(() -> elevator.getDirection() == Elevator.Direction.DOWN);
            assertThat(elevator.getAddressedFloor()).isEqualTo(MIN_FLOOR);
        }

        @Test
        void shouldAllowToAddMoreFloorsWhenTheElevatorStartedMoving() {
            // given
            final var elevator = elevator();
            final var lastFloor = MAX_FLOOR;

            // when
            elevator.moveElevator(lastFloor);
            elevator.tick();
            taskExecutor.execute(elevator);

            // then
            await().atMost(2, TimeUnit.SECONDS)
                    .until(() -> elevator.getDirection() == Elevator.Direction.NONE);
            assertThat(elevator.getAddressedFloor()).isEqualTo(MIN_FLOOR);
        }
    }

    private static OtisElevator elevator() {
        return new OtisElevator(ID, MAX_FLOOR + 1, TIME_BETWEEN_FLOORS);
    }

    private static OtisElevator elevator(final int numberOfFloors, final int numberOfTraveledFloors) {
        final var elevator = new OtisElevator(ID, numberOfFloors, TIME_BETWEEN_FLOORS);
        elevator.moveElevator(numberOfFloors - 1);
        for (int i = 0; i < numberOfTraveledFloors + 1; i++) {
            elevator.tick();
        }
        return elevator;
    }
}
