package com.rdhxb.smart_building.eventlog.controller;

import com.rdhxb.smart_building.eventlog.entity.EventLog;
import com.rdhxb.smart_building.eventlog.service.EventLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class EventLogController {

    private final EventLogService logService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','BUILDING_MANAGER')")
    public List<EventLog> getLogs(){
        return logService.getLogs();
    }

}
