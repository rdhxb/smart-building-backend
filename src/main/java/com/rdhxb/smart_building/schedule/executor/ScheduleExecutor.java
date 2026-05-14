package com.rdhxb.smart_building.schedule.executor;

import com.rdhxb.smart_building.device.entity.Device;
import com.rdhxb.smart_building.device.repo.DeviceRepo;
import com.rdhxb.smart_building.eventlog.entity.EventType;
import com.rdhxb.smart_building.eventlog.entity.LogType;
import com.rdhxb.smart_building.eventlog.entity.Source;
import com.rdhxb.smart_building.eventlog.service.EventLogService;
import com.rdhxb.smart_building.schedule.entity.Schedule;
import com.rdhxb.smart_building.schedule.repo.ScheduleRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class ScheduleExecutor {
    private final DeviceRepo deviceRepo;
    private final ScheduleRepo scheduleRepo;
    private final EventLogService logService;

    @Transactional
    public void execute(Schedule schedule){

        List<Device> devicesList = switch (schedule.getTargetType()){
            case BY_DEVICE_TYPE -> deviceRepo.findAllByDeviceType(schedule.getDeviceType());
            case BY_ROOM -> deviceRepo.findByRoom_Id(schedule.getTargetId());
            case ALL -> deviceRepo.findAll();
        };

        if (schedule.isEnabled()){
            for (Device device: devicesList){
                String oldValue = String.valueOf(device.getDeviceStatus());
                device.setDeviceStatus(schedule.getTargetStatus());
                logService.log(EventType.SCHEDULE_EXECUTED, Source.SCHEDULE, "DEVICE", device.getId(),oldValue,String.valueOf(device.getDeviceStatus()),"STAATE CHANGED USING SCHEDULE !", LogType.INFO,1L);
            }
            deviceRepo.saveAll(devicesList);
        }
        log.info("Executing schedule '{}' on {} devices", schedule.getName(), devicesList.size());



    }

    @Scheduled(cron = "0 * * * * *")
    public void tick() {
        LocalDateTime now = LocalDateTime.now();

        List<Schedule> schedules = scheduleRepo.findAllByEnabledTrue();
        for (Schedule schedule : schedules) {
            if (shouldRunNow(schedule, now)) {
                execute(schedule);
            }
        }

    }

    private boolean shouldRunNow(Schedule schedule, LocalDateTime now) {
        LocalDateTime tickStart = now.withSecond(0).withNano(0);
        LocalDateTime next = schedule.getParsedCron().next(tickStart.minusNanos(1));
        return next != null && next.equals(tickStart);
    }

}
