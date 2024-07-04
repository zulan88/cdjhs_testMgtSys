package net.wanji.common.common;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-06-30 5:46 下午
 */
@Data
public class MainCarTrajectory {
    private Double longitude;
    private Double latitude;
    private Double courseAngle;
    private Integer speed;
}
