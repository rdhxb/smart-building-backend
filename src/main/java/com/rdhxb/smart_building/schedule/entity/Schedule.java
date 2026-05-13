package com.rdhxb.smart_building.schedule.entity;

import com.rdhxb.smart_building.common.AuditingEntity;
import com.rdhxb.smart_building.device.entity.Device;
import com.rdhxb.smart_building.device.entity.DeviceStatus;
import com.rdhxb.smart_building.device.entity.DeviceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.support.CronExpression;

import java.time.LocalDateTime;
import java.util.Date;

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
    private @Enumerated TargetType targetType;

    private DeviceType deviceType;

    private Long targetId;

    boolean enabled;

    @Transient
    private CronExpression parsedCron;

    @PostLoad
    @PostPersist
    @PostUpdate
    private void parseCron() {
        if (cronExpression != null) {
            this.parsedCron = CronExpression.parse(cronExpression);
        }
    }

}
