package com.tingco.codechallenge.elevator.entity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FloorNotInRange extends RuntimeException {
    public FloorNotInRange(final int floor, final int minFloor, final int maxFloor) {
        super("Requested floor " + floor + " not in range " + minFloor + "-" + maxFloor + ".");
    }
}
