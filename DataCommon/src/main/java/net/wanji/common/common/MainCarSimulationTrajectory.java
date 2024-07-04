package net.wanji.common.common;

import lombok.Data;

import java.util.List;

/**
 * @author: jenny
 * @create: 2024-06-30 5:46 下午
 */
@Data
public class MainCarSimulationTrajectory {
    private String timestampType;

    /**
     * 实际值（TrajectoryValueDto.class）
     */
    private List<MainCarTrajectory> value;

    /**
     * 时间戳
     */
    private String timestamp;
}
