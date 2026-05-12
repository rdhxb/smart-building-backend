package com.rdhxb.smart_building.device.entity;

import com.rdhxb.smart_building.common.AuditingEntity;
import com.rdhxb.smart_building.room.entity.Room;
import com.rdhxb.smart_building.sensor.entity.Sensor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "devices")
public class Device extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;
    @Enumerated(EnumType.STRING)
    private DeviceStatus deviceStatus;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    private String properties;



}
