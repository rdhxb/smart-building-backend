package com.rdhxb.smart_building.automation.controller;

import com.rdhxb.smart_building.automation.DTO.RuleRequest;
import com.rdhxb.smart_building.automation.entity.AutomationRule;
import com.rdhxb.smart_building.automation.service.AutomationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/automation/rules")
@RequiredArgsConstructor
public class AutomationController {
    private final AutomationService automationService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','BUILDING_MANAGER')")
    public List<AutomationRule> getRules(){
        return automationService.getRules();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','BUILDING_MANAGER')")
    public AutomationRule getRule(@PathVariable Long id){
        return automationService.getRule(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','BUILDING_MANAGER')")
    @Transactional
    public void addRule(@RequestBody RuleRequest rule){
        automationService.addRule(rule);
    }

    @PostMapping("/{id}")
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','BUILDING_MANAGER')")
    public void toggleOffOn(@PathVariable Long id){
        automationService.toggleEnabled(id);
    }


    @DeleteMapping("/{id}")
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','BUILDING_MANAGER')")
    public void deleteRule(@PathVariable Long id){
        automationService.delete(id);
    }


}
