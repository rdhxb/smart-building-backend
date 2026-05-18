package com.rdhxb.smart_building.automation.evaluator;

import com.rdhxb.smart_building.automation.entity.AutomationRule;
import com.rdhxb.smart_building.automation.repo.AutomationRuleRepository;
import com.rdhxb.smart_building.device.service.DeviceStateService;
import com.rdhxb.smart_building.event.SensorReadingCreatedEvent;
import com.rdhxb.smart_building.eventlog.service.EventLogService;
import com.rdhxb.smart_building.sensor.entity.SensorReading;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class RuleEngine {

    private static final Duration DEBOUNCE_PERIOD = Duration.ofSeconds(30);

    private final AutomationRuleRepository ruleRepository;
    private final RuleEvaluator ruleEvaluator;
    private final DeviceStateService deviceStateService;
    private final EventLogService logService;

    private final AtomicReference<Map<Long, List<AutomationRule>>> rulesBySensor
            = new AtomicReference<>(Map.of());

    private final Map<Long, Instant> lastFiredAt = new ConcurrentHashMap<>();


    @PostConstruct
    public void initialLoad() {
        refreshRules();
    }


    @Scheduled(fixedDelay = 30_000)
    public void refreshRules(){
        List<AutomationRule> rulesEnabled = ruleRepository.findAllByEnabledTrue();
        Map<Long, List<AutomationRule>> grouped = rulesEnabled.stream()
                .filter(r -> r.getSensor() != null)
                .collect(Collectors.groupingBy(r -> r.getSensor().getId()));
        rulesBySensor.set(grouped);
        log.debug("Refreshed automation rules: rules total: {}  sensors : {}", rulesEnabled.size(), grouped.size());

    }

    @TransactionalEventListener
    public void handleReadings(SensorReadingCreatedEvent event) {
        Map<Long, List<AutomationRule>> snapshot = rulesBySensor.get();

        for (SensorReading reading : event.readings()) {
            Long sensorId = reading.getSensor().getId();
            List<AutomationRule> rules = snapshot.get(sensorId);

            if (rules == null || rules.isEmpty()) {
                continue;
            }

            for (AutomationRule rule : rules) {
                if (!ruleEvaluator.evaluate(rule, reading)) {
                    continue;
                }
                if (isDebounced(rule)) {
                    log.debug("Rule {} debounced, skipping", rule.getId());
                    continue;
                }
                fireRule(rule, reading);
            }
        }
    }

    private boolean isDebounced(AutomationRule rule) {
        Instant last = lastFiredAt.get(rule.getId());
        if (last == null) {
            return false;
        }
        return Duration.between(last, Instant.now()).compareTo(DEBOUNCE_PERIOD) < 0;
    }

    private void fireRule(AutomationRule rule, SensorReading reading) {
        Long ruleId = rule.getId();
        Long deviceId = rule.getTargetDevice().getId();
        var oldStatus = rule.getTargetDevice().getDeviceStatus();

        try {
            deviceStateService.changeState(
                    deviceId,
                    rule.getTargetStatus(),
                    "Automation rule #" + ruleId + " (" + rule.getName() + ")"
            );

            if (oldStatus != rule.getTargetStatus()){
                logService.logAutomation(
                        "DEVICE",
                        deviceId,
                        String.format("Device %d set to %s by rule '%s' (#%d)",
                                deviceId, rule.getTargetStatus(), rule.getName(), ruleId)
                );
            }


            log.info("Fired rule {} ({}): device {} -> {}",
                    ruleId, rule.getName(), deviceId, rule.getTargetStatus());

        } catch (Exception e) {
            log.error("Failed to fire rule {} on device {}: {}",
                    ruleId, deviceId, e.getMessage(), e);
        } finally {
            lastFiredAt.put(ruleId, Instant.now());
        }
    }



}
