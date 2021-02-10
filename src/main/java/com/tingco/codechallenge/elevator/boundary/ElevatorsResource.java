package com.tingco.codechallenge.elevator.boundary;

import com.tingco.codechallenge.elevator.control.ElevatorController;
import com.tingco.codechallenge.elevator.entity.ElevatorSnapshot;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

/**
 * Rest Resource.
 *
 * @author Sven Wesley
 *
 */
@RestController
@RequestMapping("/v1/elevators")
public final class ElevatorsResource {

    private final ElevatorController elevatorController;

    public ElevatorsResource(ElevatorController elevatorController) {
        this.elevatorController = elevatorController;
    }

    @GetMapping
    public Collection<ElevatorSnapshot> getElevators() {
        return elevatorController.getElevators().stream()
                .map(ElevatorSnapshot::from)
                .collect(toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElevatorSnapshot> getElevator(@PathVariable("id") final int id) {
        return elevatorController.getElevators().stream()
                .filter(elevator -> elevator.getId() == id)
                .map(ElevatorSnapshot::from)
                .findFirst()
                .map(snapshot -> ResponseEntity.ok().body(snapshot))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/requests/{floor}")
    public ResponseEntity<ElevatorSnapshot> requestElevator(@PathVariable("floor") final int floor) {
        final var elevator = elevatorController.requestElevator(floor);
        return ResponseEntity.ok().body(ElevatorSnapshot.from(elevator));
    }

    @PostMapping("{id}/requests/{floor}")
    public ResponseEntity<ElevatorSnapshot> requestFloor(@PathVariable("id") final int id, @PathVariable("floor") final int floor) {
        final var elevator = elevatorController.getElevators().stream()
                .filter(e -> e.getId() == id)
                .findFirst();

        elevator.ifPresent(e -> e.moveElevator(floor));

        return elevator.map(ElevatorSnapshot::from)
                .map(snapshot -> ResponseEntity.ok().body(snapshot))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}/requests")
    public ResponseEntity<ElevatorSnapshot> removeRequestsForElevator(@PathVariable("id") final int id) {
        final var elevator = elevatorController.getElevators().stream()
                .filter(e -> e.getId() == id)
                .findFirst();

        elevator.ifPresent(elevatorController::releaseElevator);

        return elevator.map(ElevatorSnapshot::from)
                .map(snapshot -> ResponseEntity.ok().body(snapshot))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
