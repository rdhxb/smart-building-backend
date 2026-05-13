package com.rdhxb.smart_building.schedule.controller;

import com.rdhxb.smart_building.schedule.entity.Schedule;
import com.rdhxb.smart_building.schedule.service.ScheduleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'BUILDING_MANAGER')")
    public List<Schedule> getSchedules(){
        return service.getSchedules();
    }
    @GetMapping("/room/{roomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'BUILDING_MANAGER')")
    public List<Schedule> getSchedulesInRoom(@PathVariable Long roomId){
        return service.getSchedulesInRoom(roomId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'BUILDING_MANAGER')")
    public Schedule getSchedule(@PathVariable Long id){
        return service.getScheduleById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'BUILDING_MANAGER')")
    @Transactional
    public void addSchedule(@RequestBody Schedule schedule){
        service.addSchedule(schedule);
    }

    @PostMapping("/toggle/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'BUILDING_MANAGER')")
    @Transactional
    public void turnOnOff(@PathVariable Long id){
        service.turnOnOff(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'BUILDING_MANAGER')")
    @Transactional
    public void deleteSchedule(@PathVariable Long id){
        service.delete(id);
    }

}
