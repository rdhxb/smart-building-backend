package com.rdhxb.smart_building.eventlog.repo;

import com.rdhxb.smart_building.eventlog.entity.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventLogRepo extends JpaRepository<EventLog, Long> {
////    filetr by entity id
//    List<EventLog> findAllByEntityId();
////    filter by EventType
//    List<EventLog> findAllByEventType();
////    filter by LogType
//    List<EventLog> findAllByLogType();
////  filter by source
//    List<EventLog> findAllBySource();
////    filter by entity type
//    List<EventLog> findAllByEntityType();
////    filter by userid
//    List<EventLog> findAllByUserId();
}
