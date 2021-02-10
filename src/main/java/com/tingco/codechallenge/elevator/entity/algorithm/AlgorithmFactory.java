package com.tingco.codechallenge.elevator.entity.algorithm;

import com.tingco.codechallenge.elevator.entity.Elevator;

import java.util.NavigableSet;

public final class AlgorithmFactory {

    private AlgorithmFactory() throws InstantiationException {
        throw new InstantiationException("This class should not be instantiated.");
    }

    public static NextFloorAlgorithm create(final int minFloor, final int currentFloor, final int maxFloor, final Elevator.Direction direction, final NavigableSet<Integer> requests) {
        return new AlwaysMinFloorAlgorithm(minFloor, currentFloor, maxFloor, direction, requests);
    }

    public static DistanceAlgorithm create(final int firstFloor, final int currentFloor, final int lastFloor, final Elevator.Direction direction) {
        return new EmptyElevatorAlgorithm(new ClosestDistanceAlgorithm(firstFloor, currentFloor, lastFloor, direction), direction);
    }
}
