package net.wanji.business.exercise.dto.simulation;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-07-02 10:04 上午
 */
@Data
public class TrajectoryPoint {
    //格式：[经度,纬度]
    private Double[] position;

    //速度（km/h）
    private Double speed;

    //距开始时的秒数
    private String time;

    //类型
    //（初始点：start；途径点：pathway；冲突点：conflict；结束点：end）
    private String type;

    private Double courseAngle;

    private Double carSource;

    private Double size_length;

    private Double size_width;

    private Double size_height;
}
