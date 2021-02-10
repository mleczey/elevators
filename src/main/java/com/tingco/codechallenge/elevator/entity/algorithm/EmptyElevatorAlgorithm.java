package com.tingco.codechallenge.elevator.entity.algorithm;

import com.tingco.codechallenge.elevator.entity.Elevator;

class EmptyElevatorAlgorithm implements DistanceAlgorithm {

    private final DistanceAlgorithm distanceAlgorithm;

    private final Elevator.Direction direction;

    EmptyElevatorAlgorithm(DistanceAlgorithm distanceAlgorithm, Elevator.Direction direction) {
        this.distanceAlgorithm = distanceAlgorithm;
        this.direction = direction;
    }

    @Override
    public int calculateDistance(final int requestedFloor) {
        return Elevator.Direction.NONE == direction ? -1 : distanceAlgorithm.calculateDistance(requestedFloor);
    }
}
