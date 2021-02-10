package com.tingco.codechallenge.elevator.entity;

import com.tingco.codechallenge.elevator.entity.algorithm.AlgorithmFactory;
import com.tingco.codechallenge.elevator.entity.exception.FloorNotInRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.NavigableSet;
import java.util.Random;
import java.util.TreeSet;

public class OtisElevator implements MovableElevator {

    private static final Logger logger = LoggerFactory.getLogger(OtisElevator.class);

    private final ElevatorId id;

    private final int minFloor;

    private final int maxFloor;

    private final Duration timeBetweenFloors;

    private int currentFloor;

    private Direction direction;

    private final NavigableSet<Integer> requests;

    public OtisElevator(final ElevatorId id, final int numberOfFloors, final Duration timeBetweenFloors) {
        this.id = id;
        minFloor = 0;
        maxFloor = numberOfFloors - 1;
        this.timeBetweenFloors = timeBetweenFloors;
        currentFloor = minFloor;
        direction = Direction.NONE;
        requests = new TreeSet<>();
    }

    @Override
    public synchronized Direction getDirection() {
        return direction;
    }

    @Override
    public synchronized int getAddressedFloor() {
        Integer addressedFloor = 0;
        if (!requests.isEmpty()) {
            if (Direction.UP == direction) {
                addressedFloor = requests.first();
            } else if (Direction.DOWN == direction) {
                addressedFloor = requests.last();
            }
        }
        return addressedFloor;
    }

    @Override
    public int getId() {
        return id.value();
    }

    @Override
    public synchronized void moveElevator(int toFloor) {
        guardFloor(toFloor);
        requests.add(toFloor);
        logElevatorStatus();
    }

    private void guardFloor(final int floor) {
        if (minFloor > floor || floor > maxFloor) {
            throw new FloorNotInRange(floor, minFloor, maxFloor);
        }
    }

    private void logElevatorStatus() {
        logger.info("Elevator {}, current floor {}, direction {}, requested floors {}", id, currentFloor, direction, requests);
    }

    @Override
    public synchronized boolean isBusy() {
        return direction != Direction.NONE;
    }

    @Override
    public synchronized int currentFloor() {
        return currentFloor;
    }

    @Override
    public ElevatorId getElevatorId() {
        return id;
    }

    @Override
    public synchronized void tick() {
        logger.debug("Elevator {}, current floor {}, direction {}, requested floors {}", id, currentFloor, direction, requests);
        final var nextFloorAlgorithm = AlgorithmFactory.create(minFloor, currentFloor, maxFloor, direction, Collections.unmodifiableNavigableSet(requests));
        nextFloorAlgorithm.calculateNextMove();
        currentFloor = nextFloorAlgorithm.getCurrentFloor();
        direction = nextFloorAlgorithm.getDirection();
        requests.clear();
        requests.addAll(nextFloorAlgorithm.getRequests());
        logElevatorStatus();
    }

    @Override
    public synchronized void reset() {
        this.requests.clear();
    }

    @Override
    public synchronized int calculateDistance(final int requestedFloor) {
        guardFloor(requestedFloor);
        return AlgorithmFactory.create(this.minFloor, this.currentFloor, this.maxFloor, this.direction)
                .calculateDistance(requestedFloor);
    }

    @Override
    public void run() {
        boolean interrupted = false;
        while (!interrupted) {
            tick();
            final var timeToNextFloor = timeToNextFloor();

            logger.debug("Elevator {}, time to next action {}", id, timeToNextFloor);

            try {
                Thread.sleep(timeToNextFloor);
            } catch (InterruptedException x) {
                interrupted = true;
            }
        }
    }

    private long timeToNextFloor() {
        return timeBetweenFloors.toMillis() + timeOfPassengerExchange();
    }

    private long timeOfPassengerExchange() {
        return new Random().nextInt(100);
    }
}
