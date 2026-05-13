package com.rdhxb.smart_building.device.service;

import com.rdhxb.smart_building.device.entity.Device;
import com.rdhxb.smart_building.device.entity.DeviceStatus;
import com.rdhxb.smart_building.device.repo.DeviceRepo;
import com.rdhxb.smart_building.eventlog.entity.Source;
import com.rdhxb.smart_building.eventlog.service.EventLogService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceStateService {
    private final DeviceRepo deviceRepo;
    private final EventLogService logService;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void changeState(Long deviceId, DeviceStatus newStatus, String reason) {
        Device device = deviceRepo.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("No device with id: " + deviceId));

        if (device.getDeviceStatus() == newStatus) {
            return;
        }

        DeviceStatus oldStatus = device.getDeviceStatus();
        device.setDeviceStatus(newStatus);
        deviceRepo.save(device);

//        log.info("Device {} state changed: {} -> {} (reason: {})",
//                deviceId, oldStatus, newStatus, reason);

//        Source s =
//        logService.logStateChange("Device",deviceId, oldStatus.name(),device.getDeviceStatus().name(), Source.USER,1L);

    }
}