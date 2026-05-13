package com.rdhxb.smart_building.schedule.entity;

import com.rdhxb.smart_building.common.AuditingEntity;
import com.rdhxb.smart_building.device.entity.Device;
import com.rdhxb.smart_building.device.entity.DeviceStatus;
import com.rdhxb.smart_building.device.entity.DeviceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Schedule extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String cronExpression;

    private @Enumerated DeviceStatus targetStatus;
    private @Enumerated TargetType targetType;      // BY_TYPE, BY_ROOM, ALL

    private DeviceType deviceType;

    private Long targetId;                     // "LIGHT" albo "3" albo null

    boolean enabled;


}

//chce teraz zrobic schedule ale struktura w modelu danych wydaje mi sie chujowa bo przeciez chcemy zrobic cos w style (codziennie o 19 zgas wsszystkie switla ) a przyjmujermy tylko Device zamiast List<Device> to ja mam ustwiac harmonogrm dla kazdego uzadzenia ?? xD