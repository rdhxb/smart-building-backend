package com.rdhxb.smart_building.schedule.repo;

import com.rdhxb.smart_building.device.entity.Device;
import com.rdhxb.smart_building.schedule.entity.Schedule;
import com.rdhxb.smart_building.schedule.entity.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepo extends JpaRepository<Schedule,Long> {
    List<Schedule> findAllByEnabledTrue();
}
