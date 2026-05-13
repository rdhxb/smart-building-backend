package com.rdhxb.smart_building.schedule.service;

import com.rdhxb.smart_building.eventlog.entity.EventType;
import com.rdhxb.smart_building.eventlog.entity.LogType;
import com.rdhxb.smart_building.eventlog.entity.Source;
import com.rdhxb.smart_building.eventlog.service.EventLogService;
import com.rdhxb.smart_building.schedule.entity.Schedule;
import com.rdhxb.smart_building.schedule.repo.ScheduleRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepo scheduleRepo;
    private final EventLogService logService;

    public List<Schedule> getSchedules(){
        return scheduleRepo.findAll();
    }

    public List<Schedule> getSchedulesInRoom(Long roomId){
        return scheduleRepo.findAllByTargetId(roomId);
    }


    public Schedule getScheduleById(Long id){
        return scheduleRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("No Schedule with id: " + id));
    }

    public void addSchedule(Schedule schedule){
        scheduleRepo.save(schedule);
        logService.log(EventType.CREATED,Source.USER,"SCHEDULE",schedule.getId(),null,schedule.toString(),"ADD NEW SHCEDULE !",LogType.INFO,1L);
    }

    public void turnOnOff(Long id){
        Schedule schedule = getScheduleById(id);
        String oldValue = String.valueOf(schedule.isEnabled());
        schedule.setEnabled(schedule.isEnabled() ? false: true);
        scheduleRepo.save(schedule);
        logService.logStateChange("SCHEDULE",id,oldValue,String.valueOf(schedule.isEnabled()),Source.USER,1L);

    }

    public void delete(Long id){
        scheduleRepo.delete(getScheduleById(id));
        logService.log(EventType.DELETED, Source.USER,"SCHDULE",id,getScheduleById(id).toString(),null,"DELETE SCHEDULE !", LogType.INFO,1L);
    }
}
