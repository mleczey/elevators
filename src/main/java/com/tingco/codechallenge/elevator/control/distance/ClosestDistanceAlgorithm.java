package com.tingco.codechallenge.elevator.control.distance;

import com.tingco.codechallenge.elevator.entity.Elevator;

public class ClosestDistanceAlgorithm implements DistanceAlgorithm {

    private final int firstFloor;

    private final int currentFloor;

    private final int lastFloor;

    private final Elevator.Direction direction;

    public ClosestDistanceAlgorithm(int firstFloor, int currentFloor, int lastFloor, Elevator.Direction direction) {
        this.firstFloor = firstFloor;
        this.currentFloor = currentFloor;
        this.lastFloor = lastFloor;
        this.direction = direction;
    }

    @Override
    public int calculateDistance(final int requestedFloor) {
        final var delta = currentFloor - requestedFloor;
        var result = Math.abs(delta);
        if (delta > 0 && Elevator.Direction.UP == direction) {
            result = (lastFloor - currentFloor) + (lastFloor - requestedFloor);
        } else if (delta < 0 && Elevator.Direction.DOWN == direction) {
            result = (currentFloor + firstFloor) + (requestedFloor - firstFloor);
        }
        return result;
    }
}
