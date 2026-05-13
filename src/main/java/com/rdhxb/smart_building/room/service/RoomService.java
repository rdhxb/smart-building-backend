package com.rdhxb.smart_building.room.service;

import com.rdhxb.smart_building.device.entity.Device;
import com.rdhxb.smart_building.device.repo.DeviceRepo;
import com.rdhxb.smart_building.eventlog.entity.EventType;
import com.rdhxb.smart_building.eventlog.entity.LogType;
import com.rdhxb.smart_building.eventlog.entity.Source;
import com.rdhxb.smart_building.eventlog.service.EventLogService;
import com.rdhxb.smart_building.room.DTO.RoomRequest;
import com.rdhxb.smart_building.room.DTO.RoomResponse;
import com.rdhxb.smart_building.room.entity.Room;
import com.rdhxb.smart_building.room.repo.RoomRepo;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepo roomRepo;
    private final DeviceRepo deviceRepo;
    private final EventLogService logService;



//    get all rooms
    public List<RoomResponse> getRooms(){
        return roomRepo.findAll()
                .stream()
                .map(RoomResponse::from)
                .toList();
    }

//    get one room
    public RoomResponse getRoom(long id){
        Room room = roomRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No room with id: " + id));
        return RoomResponse.from(room);
    }

//    add new room
    public void addRoom(RoomRequest room) {
        if (roomRepo.existsByName(room.getName())) {
            throw new EntityExistsException(
                    "Room with name '" + room.getName() + "' already exists"
            );
        }
        Room newRoom = new Room(
                null,
                room.getName(),
                room.getDescription(),
                room.getFloor(),
                room.getAreaInSquareM()
        );
        roomRepo.save(newRoom);
        logService.log(EventType.CREATED,Source.USER,"ROOM", newRoom.getId(), null,newRoom.toString(),"New room ADDED !", LogType.INFO,1L);
    }
    
//    delete room 
    public void deleteRoom(long id){
        Room room = roomRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("No room with id: " + id));
        logService.log(EventType.DELETED, Source.USER,"Room",id,room.toString(),null,"Deleting room", LogType.INFO,1L);
        roomRepo.delete(room);
    }

    public List<Room> getRoomsWithDevice(){
        List<Device> devices = deviceRepo.findAll();
        List<Room> rooms = new ArrayList<>();
        for (Device d: devices){
            rooms.add(d.getRoom());
        }
        return rooms;
    }


}
