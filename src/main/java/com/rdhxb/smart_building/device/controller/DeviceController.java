package com.rdhxb.smart_building.device.controller;

import com.rdhxb.smart_building.device.DTO.DeviceRequest;
import com.rdhxb.smart_building.device.entity.Device;
import com.rdhxb.smart_building.device.entity.DeviceStatus;
import com.rdhxb.smart_building.device.service.DeviceService;
import com.rdhxb.smart_building.device.service.DeviceStateService;
import com.rdhxb.smart_building.eventlog.entity.Source;
import com.rdhxb.smart_building.eventlog.service.EventLogService;
import com.rdhxb.smart_building.user.repo.UserRepo;
import com.rdhxb.smart_building.user.service.CustomUserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;
    private final DeviceStateService deviceStateService;
    private final UserRepo userRepo;
    private final EventLogService logService;




    @GetMapping
    public List<Device> getDevices(){
        return deviceService.getDevices();
    }

    @GetMapping("/rooms/{id}")
    public List<Device> getDevicesInRoom(@PathVariable long id){
        return deviceService.getAllDevicesFromRoom(id);
    }

    @GetMapping("/{id}")
    public Device getDeviceById(@PathVariable long id){
        return deviceService.getDeviceById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','BUILDING_MANAGER')")
    @Transactional
    public void addDeviceToRoom(@RequestBody DeviceRequest deviceRequest){
        deviceService.addDeviceToRoomId(deviceRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteDevice(@PathVariable long id){
        deviceService.deleteDevice(id);
    }


    @PatchMapping("/{id}")
    @Transactional
    public void changeStatus(@PathVariable long id, @RequestParam DeviceStatus deviceStatus){
        logService.logStateChange("Device",id, deviceService.getDeviceById(id).getDeviceStatus().name(),deviceStatus.name(), Source.USER,1L);
        deviceStateService.changeState(id,deviceStatus,"Test");
    }


}
