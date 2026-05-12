package com.rdhxb.smart_building.sensor.DTO;

import com.rdhxb.smart_building.room.entity.Room;
import com.rdhxb.smart_building.sensor.entity.SensorType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorRequest {
    private String name;
    @Enumerated
    private SensorType type;
    private Long roomId;
    private String deviceName;
    private String unit;
}
