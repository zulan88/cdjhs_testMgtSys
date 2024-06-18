package net.wanji.business.domain;

import lombok.Data;

@Data
public class TrafficFlowConfig {

    Integer id;

    String name;

    String safeInterval;

    String stopInterval;

    String maxAcceleration;

    String expectedDeceleration;

    String expectedSpeed;

}
