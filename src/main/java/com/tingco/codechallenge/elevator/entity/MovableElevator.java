package com.tingco.codechallenge.elevator.entity;

public interface MovableElevator extends Elevator, Runnable {

    ElevatorId getElevatorId();

    void tick();

    void reset();

    int calculateDistance(final int requestedFloor);
}
