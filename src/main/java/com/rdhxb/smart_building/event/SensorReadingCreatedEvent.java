package com.rdhxb.smart_building.event;

import com.rdhxb.smart_building.sensor.entity.SensorReading;

import java.util.List;


public record SensorReadingCreatedEvent(List<SensorReading> readings) {
}
