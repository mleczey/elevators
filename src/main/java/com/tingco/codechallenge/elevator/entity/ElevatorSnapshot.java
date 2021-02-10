package com.tingco.codechallenge.elevator.entity;

public class ElevatorSnapshot {
    private final int id;
    private final int addressedFloor;
    private final String direction;

    private ElevatorSnapshot(int id, int addressedFloor, String direction) {
        this.id = id;
        this.addressedFloor = addressedFloor;
        this.direction = direction;
    }

    public int getId() {
        return id;
    }

    public int getAddressedFloor() {
        return addressedFloor;
    }

    public String getDirection() {
        return direction;
    }

    public static ElevatorSnapshot from(final Elevator elevator) {
        return new ElevatorSnapshot(elevator.getId(), elevator.getAddressedFloor(), elevator.getDirection().toString());
    }
}
