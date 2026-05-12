package com.rdhxb.smart_building.sensor.DTO;

import com.rdhxb.smart_building.room.entity.Room;
import com.rdhxb.smart_building.sensor.entity.Sensor;
import com.rdhxb.smart_building.sensor.entity.SensorType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorResponse {
    private String name;
    @Enumerated
    private SensorType type;
    private Room room;
    private String unit;
    private boolean enabled;


    public static SensorResponse from(Sensor sensor){
        return new SensorResponse(
                sensor.getName(),
                sensor.getSensorType(),
                sensor.getRoom(),
                sensor.getUnit(),
                sensor.isEnabled()
        );
    }
}
