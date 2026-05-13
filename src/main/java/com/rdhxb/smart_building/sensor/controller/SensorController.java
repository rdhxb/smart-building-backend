package com.rdhxb.smart_building.sensor.controller;

import com.rdhxb.smart_building.sensor.DTO.SensorRequest;
import com.rdhxb.smart_building.sensor.entity.Sensor;
import com.rdhxb.smart_building.sensor.entity.SensorReading;
import com.rdhxb.smart_building.sensor.repo.SensorRepo;
import com.rdhxb.smart_building.sensor.service.SensorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    @GetMapping
    public List<Sensor> getSensors(){
        return sensorService.getSensors();
    }

    @GetMapping("/rooms/{id}")
    public List<Sensor> getSensorsInRoom(@PathVariable Long id){
        return sensorService.getAllSensorsInRoom(id);
    }

    @GetMapping("/{id}")
    public Sensor getSensor(@PathVariable Long id){
        return sensorService.getSensor(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'BUILDING_MENAGER')")
    @Transactional
    public void addSensor(@RequestBody SensorRequest sensor){
        sensorService.addSensor(sensor);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    
    public void deleteSensor(@PathVariable Long id){
        sensorService.deleteSensor(id);
    }

    @GetMapping("/readings/{id}")
    public List<SensorReading> getSensorReading(@PathVariable Long id){
        return sensorService.getSensorReading(id);
    }

    @PatchMapping("/{id}")
    @Transactional
    public void changeStatus(@PathVariable Long id){
        sensorService.changeStatus(id);
    }





}
