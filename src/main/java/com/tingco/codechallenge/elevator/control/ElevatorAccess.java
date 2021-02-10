package com.tingco.codechallenge.elevator.control;

import com.tingco.codechallenge.elevator.entity.ElevatorId;
import com.tingco.codechallenge.elevator.entity.MovableElevator;

import java.util.Collection;
import java.util.Optional;

public interface ElevatorAccess {
    Collection<MovableElevator> findElevators();

    Optional<MovableElevator> findById(final ElevatorId id);
}
