package com.rdhxb.smart_building.automation.DTO;


import com.rdhxb.smart_building.automation.entity.Operator;
import com.rdhxb.smart_building.device.entity.DeviceStatus;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RuleRequest {
    private String name;
    private Long sensorId;
    private Operator operator;
    private Double threshold;
    private Long targetDeviceId;
//    private ActionType actionType;
    private DeviceStatus targetStatus;
    private String actionPayload;
    private Long userId;
}
