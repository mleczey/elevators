package com.tingco.codechallenge.elevator.control;

import com.tingco.codechallenge.elevator.entity.Elevator;
import com.tingco.codechallenge.elevator.entity.ElevatorId;
import com.tingco.codechallenge.elevator.entity.MovableElevator;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * This is singleton on purpose - there should be only one elevator controller.
 */
@Service
class ElevatorDispatcher implements ElevatorController {

    private final ElevatorAccess elevatorAccess;

    public ElevatorDispatcher(final ElevatorAccess elevatorAccess) {
        this.elevatorAccess = elevatorAccess;
    }

    @Override
    public Elevator requestElevator(final int toFloor) {
        final var elevator = elevatorAccess.findElevators().stream()
                .sorted(Comparator.comparingInt(e -> e.calculateDistance(toFloor)))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No elevator available."));
        elevator.moveElevator(toFloor);
        return elevator;
    }

    @Override
    public List<Elevator> getElevators() {
        return List.copyOf(elevatorAccess.findElevators());
    }

    @Override
    public void releaseElevator(Elevator elevator) {
        elevatorAccess.findById(ElevatorId.valueOf(elevator.getId()))
                .ifPresent(MovableElevator::reset);
    }
}
