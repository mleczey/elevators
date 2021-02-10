package com.tingco.codechallenge.elevator.entity.algorithm;

import com.tingco.codechallenge.elevator.entity.Elevator;

import java.util.Collections;
import java.util.NavigableSet;
import java.util.TreeSet;

import static com.tingco.codechallenge.elevator.entity.Elevator.Direction.DOWN;
import static com.tingco.codechallenge.elevator.entity.Elevator.Direction.NONE;
import static com.tingco.codechallenge.elevator.entity.Elevator.Direction.UP;

class AlwaysMinFloorAlgorithm implements NextFloorAlgorithm {

    private int minFloor;
    private int currentFloor;
    private int maxFloor;
    private Elevator.Direction direction;
    private NavigableSet<Integer> requests;

    AlwaysMinFloorAlgorithm(final int minFloor, final int currentFloor, final int maxFloor, final Elevator.Direction direction, final NavigableSet<Integer> requests) {
        this.minFloor = minFloor;
        this.currentFloor = currentFloor;
        this.maxFloor = maxFloor;
        this.direction = direction;
        this.requests = new TreeSet<>(requests);
    }

    @Override
    public int getCurrentFloor() {
        return currentFloor;
    }

    @Override
    public Elevator.Direction getDirection() {
        return direction;
    }

    @Override
    public NavigableSet<Integer> getRequests() {
        return Collections.unmodifiableNavigableSet(requests);
    }

    @Override
    public void calculateNextMove() {
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
    }

    private void startMoving() {
        if (!requests.isEmpty()) {
            if (requests.first().equals(this.minFloor)) {
                requests.remove(currentFloor);
            } else {
                direction = UP;
            }
        }
    }

    private void moveUp() {
        currentFloor++;
        requests.remove(currentFloor);

        if (maxFloor == currentFloor || requests.isEmpty()) {
            direction = DOWN;
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
            direction = UP;
            holdStillWhenAllServed();
        }
    }

    private void holdStillWhenAllServed() {
        if (requests.isEmpty()) {
            direction = NONE;
        }
    }
}
