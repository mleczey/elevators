package com.tingco.codechallenge.elevator.entity;

import java.util.Objects;

public class ElevatorId {
    private final int id;

    private ElevatorId(final int id) {
        this.id = id;
    }

    public int value() {
        return id;
    }

    public static ElevatorId valueOf(final int id) {
        return new ElevatorId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElevatorId that = (ElevatorId) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }
}
