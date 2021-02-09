package com.tingco.codechallenge.elevator.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
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

    // pass factory that will create algorithm for tick and distance calculation
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
            throw new IllegalArgumentException("Requested floor " + floor + " not in range " + minFloor + "-" + maxFloor + ".");
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
        logElevatorStatus();
        switch (direction) {
            case NONE:
                startMoving();
                break;
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
        }
        logElevatorStatus();
    }

    private void startMoving() {
        if (!requests.isEmpty()) {
            direction = Direction.UP;
        }
    }

    private void moveUp() {
        currentFloor++;
        requests.remove(currentFloor);

        if (maxFloor == currentFloor) {
            direction = Direction.DOWN;
            goDownToMinFloorWhenAllServed();
        }
    }

    private void goDownToMinFloorWhenAllServed() {
        if (requests.isEmpty()) {
            requests.add(minFloor);
        }
    }

    private void moveDown() {
        currentFloor--;
        requests.remove(currentFloor);

        if (minFloor == currentFloor) {
            direction = Direction.UP;
            holdStillWhenAllServed();
        }
    }

    private void holdStillWhenAllServed() {
        if (requests.isEmpty()) {
            direction = Direction.NONE;
        }
    }

    // move to seperate class RankAlgorithm
    @Override
    public synchronized int calculateDistance(final int requestedFloor) {
        guardFloor(requestedFloor);

        final var delta = currentFloor - requestedFloor;
        var result = Math.abs(delta);
        if (delta > 0 && Direction.UP == direction) {
            result = (requests.last() - currentFloor) + (requests.last() - requestedFloor);
        } else if (delta < 0 && Direction.DOWN == direction) {
            result = (currentFloor + requests.first()) + (requestedFloor - requests.first());
        }
        return result;
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
