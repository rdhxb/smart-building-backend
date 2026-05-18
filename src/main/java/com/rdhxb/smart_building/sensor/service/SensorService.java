package com.rdhxb.smart_building.sensor.service;

import com.rdhxb.smart_building.device.repo.DeviceRepo;
import com.rdhxb.smart_building.eventlog.entity.EventType;
import com.rdhxb.smart_building.eventlog.entity.LogType;
import com.rdhxb.smart_building.eventlog.entity.Source;
import com.rdhxb.smart_building.eventlog.service.EventLogService;
import com.rdhxb.smart_building.room.repo.RoomRepo;
import com.rdhxb.smart_building.sensor.DTO.SensorRequest;
import com.rdhxb.smart_building.sensor.entity.Sensor;
import com.rdhxb.smart_building.sensor.entity.SensorReading;
import com.rdhxb.smart_building.sensor.repo.SensorReadingRepo;
import com.rdhxb.smart_building.sensor.repo.SensorRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepo sensorRepo;
    private final SensorReadingRepo sensorReadingRepo;
    private final RoomRepo roomRepo;
    private final DeviceRepo deviceRepo;
    private final EventLogService logService;




    public List<Sensor> getSensors(){
        return sensorRepo.findAll();
    }

    public List<Sensor> getAllSensorsInRoom(long roomId){
        return sensorRepo.findAllByRoom_Id(roomId);
    }

    public Sensor getSensor(long id){
        return sensorRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("No sensor with id: " + id));
    }

    public void addSensor(SensorRequest sensor){
        Sensor newSensor = new Sensor(
                null,
                sensor.getName(),
                sensor.getType(),
                roomRepo.findById(sensor.getRoomId()).orElseThrow(() -> new EntityNotFoundException("Cant add sensor to room does not exist !")),
                deviceRepo.findByName(sensor.getDeviceName()),
                sensor.getUnit(),
                false
        );
        sensorRepo.save(newSensor);
        logService.log(EventType.CREATED,Source.USER,"SENSOR", newSensor.getId(),null,newSensor.toString(),"NEW SENSOR HAS BEEN ADDED !",LogType.INFO,null);
    }

    @Transactional
    public void deleteSensor(long id){
        logService.log(EventType.DELETED,Source.USER,"SENSOR",id,sensorRepo.findById(id).toString(),null,"SENSOR HAS BEEN DELETED !", LogType.INFO,null);
        sensorReadingRepo.detachFromSensor(id);
        sensorRepo.deleteById(id);
    }


    public List<SensorReading> getSensorReading(Long id){
        Sensor s = sensorRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("No sensor id" + id));
        List<SensorReading> lsr = sensorReadingRepo.findAllBySensor(s);
        return lsr;
    }

    public void changeStatus(Long id){
        Sensor sensor = getSensor(id);
        String oldValue = String.valueOf(sensor.isEnabled());

        if (!sensor.isEnabled()) {
            sensor.setEnabled(true);
        } else {
            sensor.setEnabled(false);
        }
        sensorRepo.save(sensor);
        logService.logStateChange("SENSOR", id, oldValue,String.valueOf(sensor.isEnabled()), Source.USER,null);

    }




}
