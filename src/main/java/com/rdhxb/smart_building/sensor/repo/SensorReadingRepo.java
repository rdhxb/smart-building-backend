package com.rdhxb.smart_building.sensor.repo;

import com.rdhxb.smart_building.sensor.entity.Sensor;
import com.rdhxb.smart_building.sensor.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorReadingRepo extends JpaRepository<SensorReading, Long> {
    List<SensorReading> findAllBySensor(SensorReading sensorReading);
}
