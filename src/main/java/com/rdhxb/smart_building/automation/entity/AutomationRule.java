package com.rdhxb.smart_building.automation.entity;

import com.rdhxb.smart_building.common.AuditingEntity;
import com.rdhxb.smart_building.device.entity.Device;
import com.rdhxb.smart_building.device.entity.DeviceStatus;
import com.rdhxb.smart_building.sensor.entity.Sensor;
import com.rdhxb.smart_building.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "automation_rules")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AutomationRule extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @Enumerated(EnumType.STRING)
    private Operator operator;

    private Double threshold;

    @ManyToOne
    @JoinColumn(name = "target_device_id")
    private Device targetDevice;

//    @Enumerated(EnumType.STRING)
//    private ActionType actionType;
    @Enumerated(EnumType.STRING)
    private DeviceStatus targetStatus;

//   np. actionPayload = "temperature=22.5"
    private String actionPayload;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

}

