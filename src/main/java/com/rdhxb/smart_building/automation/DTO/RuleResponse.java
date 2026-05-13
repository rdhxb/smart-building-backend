package com.rdhxb.smart_building.automation.DTO;

import com.rdhxb.smart_building.automation.entity.ActionType;
import com.rdhxb.smart_building.automation.entity.Operator;
import com.rdhxb.smart_building.device.entity.Device;
import com.rdhxb.smart_building.sensor.entity.Sensor;
import com.rdhxb.smart_building.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleResponse {
    private String name;
    private boolean enabled;
    private Sensor sensor;
    private Operator operator;
    private Double threshold;
    private Device targetDevice;
    private ActionType actionType;
    private String actionPayload;
    private User createdBy;


    public static RuleResponse from(RuleResponse rule){
        return new RuleResponse(
                rule.getName(),
                rule.isEnabled(),
                rule.getSensor(),
                rule.getOperator(),
                rule.getThreshold(),
                rule.getTargetDevice(),
                rule.getActionType(),
                rule.getActionPayload(),
                rule.getCreatedBy()
        );
    }
}
