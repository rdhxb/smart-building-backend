package com.rdhxb.smart_building.simulator;

import com.rdhxb.smart_building.sensor.entity.Sensor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.clamp;
import static java.lang.Math.round;


public class ReadingGenerator {
    private final Map<Long, Double> lastValues = new HashMap<>();

    public GeneratedReading generateFor(Sensor sensor) {
        return switch (sensor.getSensorType()) {
            case TEMPERATURE -> generateTemperature(sensor);
            case HUMIDITY    -> generateHumidity(sensor);
            case MOTION      -> generateMotion();
            case DOOR_WINDOW -> generateDoorWindow(sensor);
        };
    }

    private GeneratedReading generateTemperature(Sensor sensor){
        double prev = lastValues.getOrDefault(sensor.getId(),22.0);
        double delta = ThreadLocalRandom.current().nextDouble(-0.5,0.5);
        double next = clamp(prev + delta, 18.0, 26.0);
        lastValues.put(sensor.getId(), next);
        return new GeneratedReading(round(next), null);
    }

    private GeneratedReading generateHumidity(Sensor sensor){
        double prev = lastValues.getOrDefault(sensor.getId(), 30.0);
        double delta = ThreadLocalRandom.current().nextDouble(-0.5,0.5);
        double next = clamp(prev + delta, 30.0, 70.0);
        lastValues.put(sensor.getId(), next);
        return new GeneratedReading((double) round(next), null);
    }

    private GeneratedReading generateMotion(){
        boolean detected = ThreadLocalRandom.current().nextDouble() < 0.15;
        return new GeneratedReading(null, detected ? "DETECTED" : "IDLE");

    }

    private GeneratedReading generateDoorWindow(Sensor sensor){
        double previous = lastValues.getOrDefault(sensor.getId(), 0.0);
        boolean wasOpen = previous == 1.0;

        boolean change = ThreadLocalRandom.current().nextDouble() < 0.05;
        boolean nowOpen = change != wasOpen;

        lastValues.put(sensor.getId(), nowOpen ? 1.0 : 0.0);
        return new GeneratedReading(null, nowOpen ? "OPEN" : "CLOSED");
    }

    public double round(Double num){
        return Math.round(num * 10.0) / 10.0;
    }
}

