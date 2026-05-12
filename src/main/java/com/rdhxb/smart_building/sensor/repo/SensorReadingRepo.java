package com.rdhxb.smart_building.sensor.repo;

import com.rdhxb.smart_building.sensor.entity.Sensor;
import com.rdhxb.smart_building.sensor.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SensorReadingRepo extends JpaRepository<SensorReading, Long>{
    List<SensorReading> findAllBySensor(Sensor sensor);
    SensorReading findBySensor_Id(Long sensorId);

    @Modifying
    @Query("update SensorReading sr set sr.sensor = null where sr.sensor.id = :sensorId")
    void detachFromSensor(@Param("sensorId") Long sensorId);
}
