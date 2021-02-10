package com.tingco.codechallenge.elevator.boundary;

import com.tingco.codechallenge.elevator.control.ElevatorAccess;
import com.tingco.codechallenge.elevator.entity.ElevatorId;
import com.tingco.codechallenge.elevator.entity.MovableElevator;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

class ElevatorStore implements ElevatorAccess {

    private final Map<ElevatorId, MovableElevator> elevators;

    public ElevatorStore(final Collection<? extends MovableElevator> elevators) {
        this.elevators = elevators.stream()
                .collect(toMap(MovableElevator::getElevatorId, Function.identity()));
    }

    @Override
    public Collection<MovableElevator> findElevators() {
        return elevators.values();
    }

    @Override
    public Optional<MovableElevator> findById(ElevatorId id) {
        return Optional.ofNullable(elevators.get(id));
    }
}
