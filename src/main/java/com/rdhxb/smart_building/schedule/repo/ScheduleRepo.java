package com.rdhxb.smart_building.schedule.repo;

import com.rdhxb.smart_building.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepo extends JpaRepository<Schedule,Long> {
    List<Schedule> findAllByEnabledTrue();

    List<Schedule> findAllByTargetId(Long id);
}
