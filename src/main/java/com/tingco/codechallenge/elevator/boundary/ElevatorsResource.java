package com.tingco.codechallenge.elevator.boundary;

import com.tingco.codechallenge.elevator.control.ElevatorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Resource.
 *
 * @author Sven Wesley
 *
 */
@RestController
@RequestMapping("/v1/elevators")
public final class ElevatorsResource {

    private static final Logger logger = LoggerFactory.getLogger(ElevatorsResource.class);

    private final ElevatorController elevatorController;

    public ElevatorsResource(ElevatorController elevatorController) {
        this.elevatorController = elevatorController;
    }

    @GetMapping
    public String getElevators() {
        logger.info("{}", elevatorController.getElevators());
        return "elevators";
    }
}
