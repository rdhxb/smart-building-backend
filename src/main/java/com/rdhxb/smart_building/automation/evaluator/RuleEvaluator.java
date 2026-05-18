package com.rdhxb.smart_building.automation.evaluator;

import com.rdhxb.smart_building.automation.entity.AutomationRule;
import com.rdhxb.smart_building.sensor.entity.SensorReading;
import org.springframework.stereotype.Component;



@Component
public class RuleEvaluator {

    public boolean evaluate(AutomationRule rule, SensorReading latestReading){
        final double EPSILON = 0.001;

        if (!rule.isEnabled()){
            return false;
        }
        if (latestReading.getValue() == null) return false;
        double value = latestReading.getValue();

        return switch (rule.getOperator()) {
            case LT  -> value < rule.getThreshold();
            case GT  -> value > rule.getThreshold();
            case LTE -> value <= rule.getThreshold();
            case GTE -> value >= rule.getThreshold();
            case EQ  -> Math.abs(value - rule.getThreshold()) < EPSILON;
            case NEQ -> Math.abs(value - rule.getThreshold()) >= EPSILON;
            default  -> false;
        };

    }
}
