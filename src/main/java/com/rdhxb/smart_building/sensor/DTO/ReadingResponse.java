package com.rdhxb.smart_building.sensor.DTO;

import com.rdhxb.smart_building.sensor.entity.Sensor;
import com.rdhxb.smart_building.sensor.entity.SensorReading;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadingResponse {
    private Sensor sensor;
    private Double value;


    public static ReadingResponse from(SensorReading sensorReading){
        return new ReadingResponse(
                sensorReading.getSensor(),
                sensorReading.getValue()
        );
    }
}
