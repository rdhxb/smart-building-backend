package com.rdhxb.smart_building.schedule.entity;

import com.rdhxb.smart_building.common.AuditingEntity;
import com.rdhxb.smart_building.device.entity.DeviceStatus;
import com.rdhxb.smart_building.device.entity.DeviceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.support.CronExpression;


@EqualsAndHashCode(callSuper = false)
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

    @Enumerated(EnumType.STRING)
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
