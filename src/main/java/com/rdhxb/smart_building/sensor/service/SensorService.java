package com.rdhxb.smart_building.sensor.service;

import com.rdhxb.smart_building.device.repo.DeviceRepo;
import com.rdhxb.smart_building.room.repo.RoomRepo;
import com.rdhxb.smart_building.sensor.DTO.SensorRequest;
import com.rdhxb.smart_building.sensor.entity.Sensor;
import com.rdhxb.smart_building.sensor.entity.SensorReading;
import com.rdhxb.smart_building.sensor.repo.SensorReadingRepo;
import com.rdhxb.smart_building.sensor.repo.SensorRepo;
import jakarta.persistence.EntityNotFoundException;
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
    }

    public void deleteSensor(long id){
        Sensor sensor = getSensor(id);
        sensorRepo.delete(sensor);
    }

    public List<SensorReading> getSensorReading(Long id){
        SensorReading sensor =  sensorReadingRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("No sensor with id: " + id));
        return sensorReadingRepo.findAllBySensor(sensor);
    }

    public void changeStatus(Long id){
        Sensor sensor = getSensor(id);

        if (!sensor.isEnabled()) {
            sensor.setEnabled(true);
        } else {
            sensor.setEnabled(false);
        }
        sensorRepo.save(sensor);


    }




}
