package com.tingco.codechallenge.elevator.entity.algorithm;

import com.tingco.codechallenge.elevator.entity.Elevator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class EmptyElevatorAlgorithmTest {

    @Test
    void shouldReturnLowValueWhenElevatorIsNotOperating() {
        // given
        final var distanceAlgorithm = mock(DistanceAlgorithm.class);
        final var direction = Elevator.Direction.NONE;
        final var requestedFloor = 0;
        final var testedObject = new EmptyElevatorAlgorithm(distanceAlgorithm, direction);

        // when
        final var actual = testedObject.calculateDistance(requestedFloor);

        // then
        assertThat(actual).isEqualTo(-1);
    }

    @EnumSource(value = Elevator.Direction.class, names = {"DOWN", "UP"})
    @ParameterizedTest
    void shouldExecuteOtherAlgorithmWhenElevatorIsMoving(final Elevator.Direction direction) {
        // given
        final var distanceAlgorithm = mock(DistanceAlgorithm.class);
        final var requestedFloor = 0;
        final var testedObject = new EmptyElevatorAlgorithm(distanceAlgorithm, direction);

        // when
        testedObject.calculateDistance(requestedFloor);

        // then
        verify(distanceAlgorithm).calculateDistance(requestedFloor);
    }
}
