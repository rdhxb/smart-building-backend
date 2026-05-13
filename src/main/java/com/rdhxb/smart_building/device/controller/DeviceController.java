package com.rdhxb.smart_building.device.controller;

import com.rdhxb.smart_building.device.DTO.DeviceRequest;
import com.rdhxb.smart_building.device.entity.Device;
import com.rdhxb.smart_building.device.entity.DeviceStatus;
import com.rdhxb.smart_building.device.service.DeviceService;
import com.rdhxb.smart_building.device.service.DeviceStateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;
    private final DeviceStateService deviceStateService;




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
        deviceStateService.changeState(id,deviceStatus,"Test");
    }


}
