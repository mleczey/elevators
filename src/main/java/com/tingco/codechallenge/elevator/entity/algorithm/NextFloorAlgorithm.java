package com.tingco.codechallenge.elevator.entity.algorithm;

import com.tingco.codechallenge.elevator.entity.Elevator;

import java.util.NavigableSet;

public interface NextFloorAlgorithm {

    int getCurrentFloor();

    Elevator.Direction getDirection();

    NavigableSet<Integer> getRequests();

    void calculateNextMove();
}
