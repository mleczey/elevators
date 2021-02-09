package com.tingco.codechallenge.elevator;

import com.tingco.codechallenge.elevator.boundary.ElevatorConfiguration;
import com.tingco.codechallenge.elevator.control.ElevatorDispatcher;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.awaitility.Awaitility.await;

class ElevatorsIntegrationTest {
    @Test
    void should() {
        final var configuration = new ElevatorConfiguration(1, 5, 1);
        final var elevatorAccess = configuration.elevatorAccess(configuration.elevatorTaskExecutor());
        final var elevatorController = new ElevatorDispatcher(elevatorAccess);

        elevatorController.requestElevator(1).moveElevator(4);

        await().atMost(Duration.ofSeconds(25))
                .until(() -> elevatorAccess.findElevators().stream()
                .allMatch(elevator -> !elevator.isBusy()));
    }
}
