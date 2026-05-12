package com.rdhxb.smart_building.simulator;


import com.rdhxb.smart_building.event.SensorReadingCreatedEvent;
import com.rdhxb.smart_building.sensor.entity.Sensor;
import com.rdhxb.smart_building.sensor.entity.SensorReading;
import com.rdhxb.smart_building.sensor.repo.SensorReadingRepo;
import com.rdhxb.smart_building.sensor.repo.SensorRepo;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@ConditionalOnProperty(prefix="app.simulator", name="enabled", havingValue="true")
@Component
@Slf4j
public class SensorSimulator {
    private final SensorRepo sensorRepo ;
    private final SensorReadingRepo sensorReadingRepo;

    private final ReadingGenerator readingGenerator;
    private final ApplicationEventPublisher eventPublisher;

    public SensorSimulator(SensorRepo sensorRepo, SensorReadingRepo sensorReadingRepo, ReadingGenerator readingGenerator, ApplicationEventPublisher eventPublisher) {
        this.sensorRepo = sensorRepo;
        this.sensorReadingRepo = sensorReadingRepo;
        this.readingGenerator = readingGenerator;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedDelayString = "${app.simulator.interval-ms:5000}")
    @Transactional
    public void Simulation(){
        List<Sensor> sensors =  sensorRepo.findAllByEnabledTrue();
        ArrayList<SensorReading> readings = new ArrayList<>();
        if (sensors.isEmpty()) {
            log.debug("No enabled sensors to simulate");
            return;
        }

        for (Sensor s: sensors){
            GeneratedReading gen = readingGenerator.generateFor(s);

            SensorReading sensorReading = new SensorReading();
            sensorReading.setSensor(s);
            sensorReading.setValue(gen.value());
            sensorReading.setValueText(gen.valueText());
            sensorReading.setRecordedAt(Instant.now());

            readings.add(sensorReading);
        }

        List<SensorReading> saved = sensorReadingRepo.saveAll(readings);
        eventPublisher.publishEvent(new SensorReadingCreatedEvent(saved));

        log.debug("Generated {} sensor readings", saved.size());

    }



}
