package com.rdhxb.smart_building.eventlog.service;

import ch.qos.logback.core.joran.event.EndEvent;
import com.rdhxb.smart_building.eventlog.entity.EventLog;
import com.rdhxb.smart_building.eventlog.entity.EventType;
import com.rdhxb.smart_building.eventlog.entity.LogType;
import com.rdhxb.smart_building.eventlog.entity.Source;
import com.rdhxb.smart_building.eventlog.repo.EventLogRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class EventLogService {
    private final EventLogRepo eventLogRepo;


    public void log(EventType eventType, Source source,
                    String entityType, Long entityId,
                    String oldValue, String newValue,
                    String description, LogType logType, Long userId){

        EventLog eventLog = new EventLog();

        eventLog.setTimeStamp(Instant.now());
        eventLog.setEventType(eventType);
        eventLog.setSource(source);
        eventLog.setEntityType(entityType);
        eventLog.setEntityId(entityId);
        eventLog.setOldValue(oldValue);
        eventLog.setNewValue(newValue);
        eventLog.setDescription(description);
        eventLog.setLogType(logType);
        eventLog.setUserId(userId);

        eventLogRepo.save(eventLog);

    }

    public void logUserAction(EventType type, String entityType, Long entityId,
                              String description, Long userId) {
        log(type, Source.SYSTEM, entityType, entityId, null, null,
                description, LogType.INFO, userId);
    }

    public void logStateChange(String entityType, Long entityId,
                               String oldValue, String newValue,
                               Source source, Long userId) {
        log(EventType.DEVICE_STATE_CHANGED, source, entityType, entityId,
                oldValue, newValue, null, LogType.INFO, userId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAutomation(String entityType, Long entityId, String description) {
        log(EventType.AUTOMATION_TRIGGERED, Source.AUTOMATION, entityType, entityId,
                null, null, description, LogType.INFO, null);
    }

    public void logAlert(String description, String entityType, Long entityId) {
        log(EventType.ALERT_RAISED, Source.SYSTEM, entityType, entityId,
                null, null, description, LogType.WARNING, null);
    }



    public List<EventLog> getLogs(){
        return eventLogRepo.findAll();
    }






}
