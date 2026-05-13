package com.rdhxb.smart_building.device.service;

import com.rdhxb.smart_building.device.DTO.DeviceRequest;
import com.rdhxb.smart_building.device.entity.Device;
import com.rdhxb.smart_building.device.entity.DeviceStatus;
import com.rdhxb.smart_building.device.repo.DeviceRepo;
import com.rdhxb.smart_building.eventlog.entity.EventType;
import com.rdhxb.smart_building.eventlog.entity.LogType;
import com.rdhxb.smart_building.eventlog.entity.Source;
import com.rdhxb.smart_building.eventlog.service.EventLogService;
import com.rdhxb.smart_building.room.repo.RoomRepo;
import com.rdhxb.smart_building.user.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepo deviceRepo;
    private final RoomRepo roomRepo;
    private final EventLogService logService;
    private final UserRepo userRepo;


    public List<Device> getDevices(){
        return deviceRepo.findAll();
    }

    public List<Device> getAllDevicesFromRoom(long roomId){
        return deviceRepo.findByRoom_Id(roomId);
    }

    public Device getDeviceById(long id){
        return deviceRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("No deivice with id: " +  id ));
    }

    @Transactional
    public void addDeviceToRoomId(DeviceRequest device){
        Device device1 = new Device(
                null,
                device.getName(),
                device.getDeviceType(),
                device.getDeviceStatus(),
                roomRepo.findById(device.getRoomId()).orElseThrow(() -> new EntityNotFoundException("No room with id: " + device.getRoomId())),
                device.getProperties()

        );
        deviceRepo.save(device1);
        logService.log(EventType.CREATED,Source.USER,"Device",device1.getId(),null,
                " Name :" + device1.getName() +
                        " DeviceType: " + device1.getDeviceType() +
                        " Room: " + device1.getRoom() +
                        " Properties :" + device1.getProperties(),"Adding new Device",LogType.INFO,1L);
    }

    @Transactional
    public void deleteDevice(Long id) {
        Device device = deviceRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No device with id: " + id));

        String oldValue = String.format(
                "{\"name\":\"%s\",\"type\":\"%s\",\"state\":\"%s\"}",
                device.getName(), device.getDeviceType(), device.getDeviceStatus()
        );
        String description = "Usunięto urządzenie: " + device.getName();

        deviceRepo.delete(device);

        logService.log(
                EventType.DELETED,
                Source.USER,
                "Device",
                id,
                oldValue,
                null,
                description,
                LogType.INFO,
                1L
        );
    }



}
