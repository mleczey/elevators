package com.tingco.codechallenge.elevator.control;

import com.tingco.codechallenge.elevator.entity.MovableElevator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ElevatorDispatcherTest {

    @Test
    void shouldSelectTheClosestElevator() {
        // given
        final var toFloor = 0;
        final var elevatorAccess = mock(ElevatorAccess.class);
        final var elevator = elevator(1, -1);
        final var busyElevator = elevator(2, 0);
        when(elevatorAccess.findElevators())
                .thenReturn(Set.of(elevator, busyElevator));
        final var testedObject = new ElevatorDispatcher(elevatorAccess);

        // when
        final var actual = testedObject.requestElevator(toFloor);

        // then
        assertThat(actual.getId()).isEqualTo(elevator.getId());
    }

    private MovableElevator elevator(final int id, final int distance) {
        final var elevator = mock(MovableElevator.class);
        when(elevator.getId()).thenReturn(id);
        when(elevator.calculateDistance(anyInt())).thenReturn(distance);
        return elevator;
    }
}
