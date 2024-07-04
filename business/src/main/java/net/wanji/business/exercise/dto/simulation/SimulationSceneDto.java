package net.wanji.business.exercise.dto.simulation;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-07-02 9:57 上午
 */
@Data
public class SimulationSceneDto {
    //当前时间戳 ms 级
    private Long timestamp;

    private Integer type;

    private SimulationSceneParam params;

}
