package com.tingco.codechallenge.elevator.entity.algorithm;

import com.tingco.codechallenge.elevator.entity.Elevator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;

import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import static com.tingco.codechallenge.elevator.entity.Elevator.Direction.DOWN;
import static com.tingco.codechallenge.elevator.entity.Elevator.Direction.NONE;
import static com.tingco.codechallenge.elevator.entity.Elevator.Direction.UP;
import static java.util.Collections.emptyNavigableSet;
import static org.assertj.core.api.Assertions.assertThat;

class AlwaysMinFloorAlgorithmTest {

    private static final int MIN_FLOOR = 0;

    private static final int MAX_FLOOR = 9;

    @Test
    void shouldNotMoveWhenNoRequests() {
        // given
        final var testedObject = new AlwaysMinFloorAlgorithm(MIN_FLOOR, MIN_FLOOR, MAX_FLOOR, NONE, emptyNavigableSet());

        // when
        testedObject.calculateNextMove();

        // then
        assertThat(testedObject.getCurrentFloor()).isEqualTo(0);
        assertThat(testedObject.getDirection()).isEqualTo(NONE);
        assertThat(testedObject.getRequests()).isEmpty();
    }

    @Test
    void shouldOpenTheElevatorFloorOnlyWhenAskedAboutMinFloor() {
        // given
        final var requestedFloor = 0;
        final var testedObject = new AlwaysMinFloorAlgorithm(MIN_FLOOR, MIN_FLOOR, MAX_FLOOR, NONE, set(requestedFloor));

        // when
        testedObject.calculateNextMove();

        // then
        assertThat(testedObject.getCurrentFloor()).isEqualTo(0);
        assertThat(testedObject.getDirection()).isEqualTo(NONE);
        assertThat(testedObject.getRequests()).isEmpty();
    }

    @Test
    void shouldChangeOnlyDirectionWhenRequestedNewFloor() {
        // given
        final var requestedFloor = 1;
        final var testedObject = new AlwaysMinFloorAlgorithm(MIN_FLOOR, MIN_FLOOR, MAX_FLOOR, NONE, set(requestedFloor));

        // when
        testedObject.calculateNextMove();

        // then
        assertResultState(testedObject, 0, UP, requestedFloor);
    }

    private void assertResultState(final NextFloorAlgorithm algorithm, final int currentFloor, final Elevator.Direction direction, final Integer... requests) {
        assertThat(algorithm.getCurrentFloor()).isEqualTo(currentFloor);
        assertThat(algorithm.getDirection()).isEqualTo(direction);
        assertThat(algorithm.getRequests()).containsOnly(requests);
    }

    @ParameterizedTest
    @ValueSource(ints = {5, MAX_FLOOR})
    void shouldGoDownWhenReachedRequestedFloor(final int requestedFloor) {
        // given
        final var testedObject = new AlwaysMinFloorAlgorithm(MIN_FLOOR, requestedFloor - 1, MAX_FLOOR, UP, set(requestedFloor));

        // when
        testedObject.calculateNextMove();

        // then
        assertResultState(testedObject, requestedFloor, DOWN, 0);
    }

    private NavigableSet set(Integer... elements) {
        return new TreeSet<>(Set.of(elements));
    }
}
