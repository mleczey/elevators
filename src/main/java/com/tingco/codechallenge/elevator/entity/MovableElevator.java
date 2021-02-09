package com.tingco.codechallenge.elevator.entity;

public interface MovableElevator extends Elevator, Runnable {

    ElevatorId getElevatorId();

    void tick();

    int calculateDistance(final int requestedFloor);
}
