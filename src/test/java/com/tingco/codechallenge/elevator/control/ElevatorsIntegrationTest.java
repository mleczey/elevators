package com.tingco.codechallenge.elevator.control;

import com.tingco.codechallenge.elevator.boundary.ElevatorConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Random;

import static org.awaitility.Awaitility.await;

class ElevatorsIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(ElevatorsIntegrationTest.class);

    @Test
    void should() throws InterruptedException {
        final int numberOfElevators = 2;
        final int numberOfFloors = 10;
        final int timeBetweenFloorsInMilliseconds = 10;
        final int numberOfPassengers = 20;

        final var configuration = new ElevatorConfiguration(numberOfElevators, numberOfFloors, timeBetweenFloorsInMilliseconds);
        final var elevatorAccess = configuration.elevatorAccess(configuration.elevatorTaskExecutor());
        final var elevatorController = new ElevatorDispatcher(elevatorAccess);

        final var random = new Random();
        for (int i = 0; i < numberOfPassengers; i++) {
            final int from = random.nextInt(numberOfFloors);
            final int to = random.nextInt(numberOfFloors);

            logger.debug("Requesting elevator from floor {} to floor {}.", from, to);

            elevatorController.requestElevator(from).moveElevator(to);
            Thread.sleep(timeBetweenFloorsInMilliseconds * random.nextInt(numberOfFloors));
        }

        await().atMost(Duration.ofSeconds(25))
                .until(() -> elevatorAccess.findElevators().stream()
                        .allMatch(elevator -> !elevator.isBusy()));
    }
}
