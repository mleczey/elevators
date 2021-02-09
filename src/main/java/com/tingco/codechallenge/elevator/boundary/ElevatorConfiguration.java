package com.tingco.codechallenge.elevator.boundary;

import com.tingco.codechallenge.elevator.control.ElevatorAccess;
import com.tingco.codechallenge.elevator.entity.ElevatorId;
import com.tingco.codechallenge.elevator.entity.OtisElevator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
public class ElevatorConfiguration {

    private final int numberOfElevators;

    private final int numberOfFloors;

    private final long timeBetweenFloorsInMilliseconds;

    public ElevatorConfiguration(@Value("${com.tingco.elevator.numberOfElevators}") int numberOfElevators,
                                 @Value("${com.tingco.elevator.numberOfFloors}") int numberOfFloors,
                                 @Value("${com.tingco.elevator.timeBetweenFloorsInMilliseconds}") long timeBetweenFloorsInMilliseconds) {
        this.numberOfElevators = numberOfElevators;
        this.numberOfFloors = numberOfFloors;
        this.timeBetweenFloorsInMilliseconds = timeBetweenFloorsInMilliseconds;
    }

    @Bean
    public ElevatorAccess elevatorAccess(final ThreadPoolTaskExecutor elevatorTaskExecutor) {
        final var elevators = IntStream.range(0, numberOfElevators)
                .mapToObj(i -> new OtisElevator(ElevatorId.valueOf(i), numberOfFloors, Duration.of(timeBetweenFloorsInMilliseconds, ChronoUnit.MILLIS)))
                .collect(Collectors.toSet());

        elevators.forEach(elevatorTaskExecutor::execute);

        return new ElevatorStore(elevators);
    }

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor elevatorTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(numberOfElevators);
        taskExecutor.setMaxPoolSize(numberOfElevators);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(false);
        taskExecutor.initialize();
        return taskExecutor;
    }
}
