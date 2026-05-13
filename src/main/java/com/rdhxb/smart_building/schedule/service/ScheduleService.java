package com.rdhxb.smart_building.schedule.service;

import com.rdhxb.smart_building.schedule.entity.Schedule;
import com.rdhxb.smart_building.schedule.repo.ScheduleRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepo scheduleRepo;

    public List<Schedule> getScheduls(){
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
    }

    public void turnOnOff(Long id){
        Schedule schedule = getScheduleById(id);
        schedule.setEnabled(schedule.isEnabled() ? fsalse: true);
        scheduleRepo.save(schedule);
    }

    public void delete(Long id){
        scheduleRepo.delete(getScheduleById(id));
    }
}
