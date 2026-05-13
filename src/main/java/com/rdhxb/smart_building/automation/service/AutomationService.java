package com.rdhxb.smart_building.automation.service;

import com.rdhxb.smart_building.automation.DTO.RuleRequest;
import com.rdhxb.smart_building.automation.entity.AutomationRule;
import com.rdhxb.smart_building.automation.repo.AutomationRuleRepository;
import com.rdhxb.smart_building.device.repo.DeviceRepo;
import com.rdhxb.smart_building.sensor.repo.SensorRepo;
import com.rdhxb.smart_building.user.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class AutomationService {
    private final AutomationRuleRepository ruleRepository;
    private final UserRepo userRepo;
    private final DeviceRepo deviceRepo;
    private final SensorRepo sensorRepo;

    public List<AutomationRule> getRules(){
        return ruleRepository.findAll();
    }

    public AutomationRule getRule(Long id){
        return ruleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No rule with id:" + id));
    }

    public void addRule(RuleRequest rule){
        AutomationRule r = new AutomationRule(
                null,
                rule.getName(),
                true,
                sensorRepo.findById(rule.getSensorId()).orElse(null),
                rule.getOperator(),
                rule.getThreshold(),
                deviceRepo.findById(rule.getTargetDeviceId()).orElse(null),
                rule.getTargetStatus(),
                rule.getActionPayload(),
                userRepo.findUserById(rule.getUserId())
        );
        ruleRepository.save(r);
    }

    public void offon(Long id){
        AutomationRule rule = getRule(id);

        if (!rule.isEnabled()){
            rule.setEnabled(true);
        }
        else {
            rule.setEnabled(false);
        }
        ruleRepository.save(rule);
    }

    public void delete(Long id){
        ruleRepository.delete(getRule(id));
    }


}
