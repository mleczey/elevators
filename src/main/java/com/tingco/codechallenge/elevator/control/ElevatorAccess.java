package com.tingco.codechallenge.elevator.control;

import com.tingco.codechallenge.elevator.entity.MovableElevator;

import java.util.Collection;

public interface ElevatorAccess {
    Collection<MovableElevator> findElevators();
}
